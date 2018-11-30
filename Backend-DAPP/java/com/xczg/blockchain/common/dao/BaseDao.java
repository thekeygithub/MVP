package com.xczg.blockchain.common.dao;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.id.UUIDHexGenerator;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.xczg.blockchain.common.model.EntityInfo;
import com.xczg.blockchain.common.model.PageResult;
import com.xczg.blockchain.common.model.SqlParamModel;
import com.xczg.blockchain.common.utils.ClassUtils;
import com.xczg.blockchain.common.utils.RegexValidateUtils;
import com.xczg.blockchain.common.utils.WhereSQLInfo;

/**
 * 
 * @author zd
 */
@Service
public class BaseDao {
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbc;
	
	private NamedParameterJdbcTemplate nameJdbc;

	public NamedParameterJdbcTemplate getNameJdbc() {
		if(nameJdbc == null)
			nameJdbc = new NamedParameterJdbcTemplate(jdbc.getDataSource());
		return nameJdbc;
	}
	
	public int updateBySql(String sql, Object... args){
		return jdbc.update(sql, args);
	}
	
	public <T> T queryForObject(String sql,Map<String,Object> paramMap, Class<T> clazz){
		T t = null;
		try {
			if (ClassUtils.isBaseType(clazz)) {
				t = (T) getNameJdbc().queryForObject(sql, paramMap, clazz);

			} else {
				t = (T) getNameJdbc().queryForObject(sql, paramMap, BeanPropertyRowMapper.newInstance(clazz));
			}
		} catch (EmptyResultDataAccessException e) {
			t = null;
		}

		return t;
	}
	
	/**
	 * insert
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Object save(Object entity) throws Exception {
		// entity信息
		EntityInfo entityInfo = ClassUtils.getEntityInfoByEntity(entity);
		Class<?> returnType = entityInfo.getPkReturnType();

		Map<String, Object> paramMap = new HashMap<String, Object>();
		String sql = warpsavesql(entity, paramMap);

		if (returnType == String.class) {
			getNameJdbc().update(sql, paramMap);
			return ClassUtils.getPKValue(entity).toString();
		} else
			throw new Exception("PkReturnType is Error!");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Integer> save(List list) throws Exception {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}

		List<Integer> updateList = new ArrayList<Integer>();
		Map[] maps = new HashMap[list.size()];
		String sql = null;
		for (int i = 0; i < list.size(); i++) {
			Map paramMap = new HashMap();
			sql = warpsavesql(list.get(i), paramMap);
			maps[i] = paramMap;
		}
		int[] batchUpdate = getNameJdbc().batchUpdate(sql, SqlParameterSourceUtils.createBatch(maps));

		if (batchUpdate.length < 1) {
			return updateList;
		}
		for (int i : batchUpdate) {
			updateList.add(i);
		}
		return updateList;
	}
	
	/**
	 * 根据主键修改实体类中不为NULL的属性
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public Integer update(Object entity) throws Exception {
		return update(entity, true);
	}
	
	@SuppressWarnings("rawtypes")
	public List<Integer> update(List list) throws Exception {
		return update(list, false);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Integer> update(List list, boolean onlyupdatenotnull) throws Exception {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		List<Integer> updateList = new ArrayList<Integer>();
		Map[] maps = new HashMap[list.size()];
		String sql = null;
		for (int i = 0; i < list.size(); i++) {
			Map paramMap = new HashMap();
			sql = warpupdatesql(list.get(i), paramMap, onlyupdatenotnull);
			maps[i] = paramMap;
		}
		int[] batchUpdate = getNameJdbc().batchUpdate(sql, SqlParameterSourceUtils.createBatch(maps));

		if (batchUpdate.length < 1) {
			return updateList;
		}
		for (int i : batchUpdate) {
			updateList.add(i);
		}
		return updateList;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Integer update(Object entity, boolean onlyupdatenotnull) throws Exception {
		Map paramMap = new HashMap();
		String sql = warpupdatesql(entity, paramMap, onlyupdatenotnull);
		return getNameJdbc().update(sql.toString(), paramMap);
	}
	
	/**
	 * 根据主键查询信息
	 * @param id
	 * @param clazz
	 * @return
	 */
	public <T> T findByID(Object id, Class<T> clazz){
		EntityInfo entityInfo = ClassUtils.getEntityInfoByClass(clazz);
		String tableName = entityInfo.getTableName();
		String idName = (String) ClassUtils.propertyToField(entityInfo.getPkName());
		String sql = "SELECT * FROM " + tableName + " WHERE " + idName + "=:id";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		try{
			return getNameJdbc().queryForObject(sql, paramMap, BeanPropertyRowMapper.newInstance(clazz));
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T findObjectByEntity(T entity){
		SqlParamModel spm = getQueryMapFromEntity(entity);
		try{
			return (T) getNameJdbc().queryForObject(spm.getSql(), spm.getParamMap(), BeanPropertyRowMapper.newInstance(entity.getClass()));
		}catch(Exception e){
			return null;
		}
	}
	
	
	
	/**
	 * 根据实体类不为空的属性拼条件查询
	 * @param entity
	 * @return
	 */
	public <T> List<T> queryByEntity(T entity){
		return queryByEntity(entity, "");
	}
	
	/**
	 * 根据实体类不为空的属性拼条件查询按orderSql规则排序
	 * @param entity
	 * @param orderSql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> queryByEntity(T entity, String orderSql){
		SqlParamModel spm = getQueryMapFromEntity(entity);
		return (List<T>) getNameJdbc().query(spm.getSql() + " " +orderSql, spm.getParamMap(), BeanPropertyRowMapper.newInstance(entity.getClass()));
	}
	
	/**
	 * 根据实体类不为空的属性拼条件分页查询
	 * @param entity
	 * @param page
	 * @return
	 */
	public <T> PageResult<T> queryPageByEntity(T entity, PageResult<T> page){
		return queryPageByEntity(entity, page, "");
	}
	
	/**
	 * 根据实体类不为空的属性拼条件查询按orderSql规则排序分页查询
	 * @param entity
	 * @param page
	 * @param orderSql
	 * @return
	 */
	public <T> PageResult<T> queryPageByEntity(T entity, PageResult<T> page, String orderSql){
		SqlParamModel spm = getQueryMapFromEntity(entity);
		page.setRows(queryPageBySql(spm.getSql() + " " + orderSql, spm.getParamMap(), page, entity.getClass()));
		return page;
	}
	/**
	 * 根据实体类不为空的属性拼条件查询按orderSql规则排序分页查询
	 * 本方法适用于mysql数据库，由于mysql无法获取rowcount所以需要自己拼写对应的查询sql
	 */
	public <T> PageResult<T> queryPageByEntity(String sql,T entity, PageResult<T> page, String orderSql){
		SqlParamModel spm = getQueryMapFromEntity(entity);
		page.setRows(queryPageBySql(sql + " " + orderSql, spm.getParamMap(), page, entity.getClass()));
		return page;
	}
	
	
	
	
	/**
	 * SQL查询
	 * @param sql
	 * @param paramMap
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> queryBySql(String sql, Map<String, Object> paramMap, Class<T> clazz) throws Exception {
		return getNameJdbc().query(sql, paramMap, BeanPropertyRowMapper.newInstance(clazz));
	}
	
	/**
	 * SQL分页查询
	 * @param sql
	 * @param paramMap
	 * @param page
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> queryPageBySql(String sql, Map<String, Object> paramMap, PageResult<T> page, Class<? extends Object> clazz){
		String countSql = new String(sql);
		int order_int = RegexValidateUtils.getOrderByIndex(countSql);
		if (order_int > 1) {
			countSql = countSql.substring(0, order_int);
		}
		/**
		 * 特殊关键字过滤, distinct ,union ,group by
		 */
		if (countSql.toLowerCase().indexOf(" distinct ") > -1 || countSql.toLowerCase().indexOf(" union ") > -1
				|| RegexValidateUtils.getGroupByIndex(countSql) > -1) {
			countSql = "SELECT count(*)  frame_row_count FROM (" + countSql
					+ ") temp_frame_noob_table_name WHERE 1=1 ";
		} else {
			int fromIndex = RegexValidateUtils.getFromIndex(countSql);
			if (fromIndex > -1) {
				countSql = "SELECT COUNT(*) " + countSql.substring(fromIndex);
			} else {
				countSql = "SELECT count(*)  frame_row_count FROM (" + countSql
						+ ") temp_frame_noob_table_name WHERE 1=1 ";
			}

		}
		int count = queryCountBySql(countSql, paramMap);
		page.setTotal(count);
		if(count == 0) return new ArrayList<T>();
		else{
			return (List<T>) getNameJdbc().query(getPageSql(sql, page), paramMap, BeanPropertyRowMapper.newInstance(clazz));
		}
	}
	
	public int queryCountBySql(String countSql, Map<String, Object> paramMap){
		return getNameJdbc().queryForObject(countSql, paramMap, Integer.class);
	}
	
	/**
	 * 获取分页SQL
	 * 此方法适用于orcale数据库
	 * 适用于mysql数据库的方法为getPageSql
	 * @param sql
	 * @param page
	 * @return
	 */
	private <T> String getPageSqlForOrcale(String sql, PageResult<T> page) {
		// 设置分页参数
		int satrt = (int) ((page.getPageNo() - 1) * page.getPageSize() + 1);
		int end = (int) (page.getPageNo() * page.getPageSize());

		StringBuffer sb = new StringBuffer();
		sb.append("select * from ( select  rownum frame_page_sql_row_number ,frame_sql_temp_table1.* from (");
		sb.append(sql);
		sb.append(") frame_sql_temp_table1 where rownum <= ").append(end)
				.append(") frame_sql_temp_table2");
		sb.append(" where frame_sql_temp_table2.frame_page_sql_row_number >= ")
				.append(satrt);

		return sb.toString();
	}
	
	public <T> String getExportSqlByEntity(T entity){
		return getExportSqlByEntity(entity, null);
	}
	
	/**
	 * 根据实体类拼EXPORT EXCEL的SQL
	 * @param entity
	 * @return
	 */
	public <T> String getExportSqlByEntity(T entity, String orderSql){
		String tableName = ClassUtils.getTableName(entity);
		List<WhereSQLInfo> whereSQLInfo = ClassUtils.getWhereSQLInfo(entity.getClass());
		StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName + " where 1=1");
		if (CollectionUtils.isNotEmpty(whereSQLInfo)) {
			for (WhereSQLInfo whereinfo : whereSQLInfo) {
				String name = whereinfo.getName();
				Object value;
				try {
					value = ClassUtils.getPropertieValue(name, entity);
				} catch (Exception e) {
					break;
				}
				if (value == null || StringUtils.isBlank(value.toString())) {
					continue;
				}
				String wheresql = whereinfo.getWheresql();
				String pname = wheresql.substring(wheresql.lastIndexOf(":") + 1).trim();
				if (wheresql.toLowerCase().contains(" in ") && pname.endsWith(")")) {
					pname = pname.substring(0, pname.length() - 1).trim();
				}

				if (wheresql.toLowerCase().contains(" like ")) {
					boolean qian = pname.trim().startsWith("%");
					boolean hou = pname.trim().endsWith("%");
					wheresql = wheresql.replaceAll("%", "");
					pname = pname.replaceAll("%", "");
					if (qian) {
						value = "%" + value;
					}
					if (hou) {
						value = value + "%";
					}
				}
				sql.append(" and ").append(wheresql.replace(":" + pname, "'" + value + "'"));
			}
		}
		if(StringUtils.isNotBlank(orderSql))
			sql.append(" "+orderSql);
		return sql.toString();
	}
	
	
	public <T> SqlParamModel getWhereMapFromEntity(T entity, String prefix){
		SqlParamModel sqm = new SqlParamModel();
		List<WhereSQLInfo> whereSQLInfo = ClassUtils.getWhereSQLInfo(entity.getClass());
		StringBuffer sql = new StringBuffer(" where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (CollectionUtils.isNotEmpty(whereSQLInfo)) {
			for (WhereSQLInfo whereinfo : whereSQLInfo) {
				String name = whereinfo.getName();
				Object value;
				try {
					value = ClassUtils.getPropertieValue(name, entity);
				} catch (Exception e) {
					break;
				}
				if (value == null || StringUtils.isBlank(value.toString())) {
					continue;
				}
				String wheresql = whereinfo.getWheresql();
				if(prefix != null && StringUtils.isNotBlank(prefix)){
					wheresql = prefix + "." + wheresql.trim();
				}
				String pname = wheresql.substring(wheresql.lastIndexOf(":") + 1).trim();
				if (wheresql.toLowerCase().contains(" in ") && pname.endsWith(")")) {
					pname = pname.substring(0, pname.length() - 1).trim();
				}

				if (wheresql.toLowerCase().contains(" like ")) {
					boolean qian = pname.trim().startsWith("%");
					boolean hou = pname.trim().endsWith("%");
					wheresql = wheresql.replaceAll("%", "");
					pname = pname.replaceAll("%", "");
					if (qian) {
						value = "%" + value;
					}
					if (hou) {
						value = value + "%";
					}
				}
				sql.append(" and ").append(wheresql);
				paramMap.put(pname, value);
			}
		}
		sqm.setSql(sql.toString());
		sqm.setParamMap(paramMap);
		return sqm;
	}
	
	
	/**
	 * 根据实体类拼查询条件
	 * @param entity
	 * @return
	 */
	private <T> SqlParamModel getQueryMapFromEntity(T entity){
		SqlParamModel sqm = new SqlParamModel();
		String tableName = ClassUtils.getTableName(entity);
		List<WhereSQLInfo> whereSQLInfo = ClassUtils.getWhereSQLInfo(entity.getClass());
		StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName + " where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (CollectionUtils.isNotEmpty(whereSQLInfo)) {
			for (WhereSQLInfo whereinfo : whereSQLInfo) {
				String name = whereinfo.getName();
				Object value;
				try {
					value = ClassUtils.getPropertieValue(name, entity);
				} catch (Exception e) {
					break;
				}
				if (value == null || StringUtils.isBlank(value.toString())) {
					continue;
				}
				String wheresql = whereinfo.getWheresql();
				String pname = wheresql.substring(wheresql.lastIndexOf(":") + 1).trim();
				if (wheresql.toLowerCase().contains(" in ") && pname.endsWith(")")) {
					pname = pname.substring(0, pname.length() - 1).trim();
				}

				if (wheresql.toLowerCase().contains(" like ")) {
					boolean qian = pname.trim().startsWith("%");
					boolean hou = pname.trim().endsWith("%");
					wheresql = wheresql.replaceAll("%", "");
					pname = pname.replaceAll("%", "");
					if (qian) {
						value = "%" + value;
					}
					if (hou) {
						value = value + "%";
					}
				}
				sql.append(" and ").append(wheresql);
				paramMap.put(pname, value);
			}
		}
		sqm.setSql(sql.toString());
		sqm.setParamMap(paramMap);
		return sqm;
	}
	
	/**
	 * 根据实体类拼装INSERT_SQL
	 * @param entity
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String warpsavesql(Object entity, Map paramMap) throws Exception{
		Class clazz = entity.getClass();
		// entity信息
		EntityInfo entityInfo = ClassUtils.getEntityInfoByEntity(entity);
		List<String> fdNames = ClassUtils.getAllDBFields(clazz);
		UUIDHexGenerator idHex = new UUIDHexGenerator();
		String id = idHex.generate(null, null).toString();// 主键
		String tableName = entityInfo.getTableName();
		Class<?> returnType = entityInfo.getPkReturnType();
		String pkName = entityInfo.getPkName();

		StringBuffer sql = new StringBuffer("INSERT INTO ").append(tableName).append("(");

		StringBuffer valueSql = new StringBuffer(" values(");

		for (int i = 0; i < fdNames.size(); i++) {
			String fdName = fdNames.get(i);// 字段名称
			// fd.setAccessible(true);

			if (fdName.equals(pkName)) {// 如果是ID,自动生成UUID
				Object _getId = ClassUtils.getPKValue(entity); // 主键
				if (_getId == null || "".equals(_getId)) {
					if (returnType == String.class) {
						ClassUtils.setPropertieValue(pkName, entity, id);
					}
				} else {
					id = _getId.toString();
				}
			}

			String mapKey = ":" + fdName;// 占位符
			Object fdValue = ClassUtils.getPropertieValue(fdName, entity);
			paramMap.put(fdName, fdValue);

			if ((i + 1) == fdNames.size()) {
				sql.append(ClassUtils.propertyToField(fdName)).append(")");
				valueSql.append(mapKey).append(")");
				break;
			}

			sql.append(ClassUtils.propertyToField(fdName)).append(",");
			valueSql.append(mapKey).append(",");

		}
		sql.append(valueSql);// sql语句
		return sql.toString();
	}
	
	/**
	 * 根据实体类拼装UPDATE_SQL
	 * @param entity
	 * @param paramMap
	 * @param onlyupdatenotnull
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String warpupdatesql(Object entity,Map paramMap, boolean onlyupdatenotnull) throws Exception {
		Class clazz = entity.getClass();
		// entity的信息
		EntityInfo entityInfo = ClassUtils.getEntityInfoByEntity(entity);
		List<String> fdNames = ClassUtils.getAllDBFields(clazz);
		String tableName = entityInfo.getTableName();

		String pkName = entityInfo.getPkName();

		// 获取 分表的扩展
		StringBuffer sql = new StringBuffer("UPDATE ").append(tableName).append("  SET  ");

		StringBuffer whereSQL = new StringBuffer(" WHERE ").append(ClassUtils.propertyToField(pkName)).append("=:").append(pkName);
		for (int i = 0; i < fdNames.size(); i++) {
			String fdName = fdNames.get(i);// 字段名称
			Object fdValue = ClassUtils.getPropertieValue(fdName, entity);

			// 只更新不为null的字段
			if (onlyupdatenotnull && fdValue == null) {// 如果只更新不为null的字段,字段值为null
														// 不更新
				continue;
			}

			if (fdName.equals(pkName)) {// 如果是ID
				if (fdValue != null) {// id有值
					paramMap.put(fdName, fdValue);
				}
				continue;
			}
			// 设置字段
			paramMap.put(fdName, fdValue);
			// 添加需要更新的字段
			sql.append(ClassUtils.propertyToField(fdName)).append("=:").append(fdName).append(",");
		}

		String str = sql.toString();
		if (str.endsWith(",")) {
			str = str.substring(0, str.length() - 1);
		}
		return str + whereSQL;
	}
	
	public SimpleJdbcCall getJdbcCall(String proName){
		return new SimpleJdbcCall(jdbc).withProcedureName(proName);
	}
	
	/**
	 * 获取分页SQL
	 * @param sql
	 * @param page
	 * @return
	 */
	private <T> String getPageSql(String sql, PageResult<T> page) {
		// 设置分页参数
		int satrt = (int) ((page.getPageNo() - 1) * page.getPageSize() + 1);
		int end = (int) (page.getPageNo() * page.getPageSize());

		StringBuffer sb = new StringBuffer();
		sb.append("select * from ( select  @rownum:=@rownum+1 rownum ,frame_sql_temp_table1.* from (SELECT @rownum:=0) rows,(");
		sb.append(sql);
		sb.append(") frame_sql_temp_table1 ")
				.append(") frame_sql_temp_table2 where rownum <= ").append(end);
		sb.append(" and rownum >= ")
				.append(satrt);
		return sb.toString();
	}
	
}