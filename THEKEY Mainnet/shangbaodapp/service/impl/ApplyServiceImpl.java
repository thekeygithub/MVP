package com.xczg.blockchain.shangbaodapp.service.impl;

import com.xczg.blockchain.shangbaodapp.service.ApplyService;
import com.xczg.blockchain.yibaodapp.bean.*;
import com.xczg.blockchain.yibaodapp.mybatis.ApplyMapper;
import com.xczg.blockchain.yibaodapp.service.IApplyService;
import com.xczg.blockchain.yibaodapp.util.SqlSessionFactoryUtil;
import com.xczg.blockchain.yibaodapp.util.StringUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("applyServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class ApplyServiceImpl implements ApplyService {

	private ApplyMapper mapper=null;
	private SqlSession sqlSession; 
	
	/**
	 * 查询投保产品购买申请个数
	 * @param o 投保产品购买申请对象
	 * @param time1 开始时间
	 * @param time2 结束时间
	 * @return
	 */
	public int getProductApplyListCount(ProductApply o,long time1,long time2){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("o", o);
		if(!StringUtil.isEmpty(o.getApply_user_name())) {
			param.put("apply_user_name", "%"+o.getApply_user_name()+"%");
		}
		if(!StringUtil.isEmpty(o.getApply_phone())) {
			param.put("apply_phone", "%"+o.getApply_phone()+"%");
		}
		if(!StringUtil.isEmpty(o.getApply_id_card())) {
			param.put("apply_id_card", "%"+o.getApply_id_card()+"%");
		}
		if(!StringUtil.isEmpty(o.getCheck_user_code())) {
			param.put("check_user_code", "%"+o.getCheck_user_code()+"%");
		}
		if(!StringUtil.isEmpty(o.getProduct_insurance_area())) {
			param.put("product_insurance_area", "%"+o.getProduct_insurance_area()+"%");
		}
		if(!StringUtil.isEmpty(o.getStatus())) {
			param.put("status", "%"+o.getStatus()+"%");
		}
		if(!StringUtil.isEmpty(o.getOrganize_id())) {
			param.put("organize_id", "%"+o.getOrganize_id()+"%");
		}
		if(!StringUtil.isEmpty(o.getApply_user_id())) {
			param.put("apply_user_id", "%"+o.getApply_user_id()+"%");
		}
		param.put("time1", new Long(time1));
		param.put("time2", new Long(time2));
		getMapper();
		int count = mapper.getProductApplyListCount(param);
		close();
		return count;
	}
	/**
	 * 分页查询投保产品购买申请列表
	 * @param o 投保产品购买申请对象
	 * @param time1 开始时间
	 * @param time2 结束时间
	 * @param iDisplayStart 分页记录开始
	 * @param iDisplayLength 每页返回记录数
	 * @param orderName 排序字段
	 * @param orderAZ 排序方式
	 * @return
	 */
	public List<ProductApply> getProductApplyPageList(ProductApply o,long time1,long time2,int iDisplayStart,int iDisplayLength,String orderName,String orderAZ){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("o", o);
		if(!StringUtil.isEmpty(o.getApply_user_name())) {
			param.put("apply_user_name", "%"+o.getApply_user_name()+"%");
		}
		if(!StringUtil.isEmpty(o.getApply_phone())) {
			param.put("apply_phone", "%"+o.getApply_phone()+"%");
		}
		if(!StringUtil.isEmpty(o.getApply_id_card())) {
			param.put("apply_id_card", "%"+o.getApply_id_card()+"%");
		}
		if(!StringUtil.isEmpty(o.getCheck_user_code())) {
			param.put("check_user_code", "%"+o.getCheck_user_code()+"%");
		}
		if(!StringUtil.isEmpty(o.getProduct_insurance_area())) {
			param.put("product_insurance_area", "%"+o.getProduct_insurance_area()+"%");
		}
		if(!StringUtil.isEmpty(o.getStatus())) {
			param.put("status", "%"+o.getStatus()+"%");
		}
		if(!StringUtil.isEmpty(o.getOrganize_id())) {
			param.put("organize_id", "%"+o.getOrganize_id()+"%");
		}
		if(!StringUtil.isEmpty(o.getApply_user_id())) {
			param.put("apply_user_id", "%"+o.getApply_user_id()+"%");
		}
		param.put("time1", new Long(time1));
		param.put("time2", new Long(time2));
		param.put("pageStart", new Integer(iDisplayStart));
		param.put("pageSize", new Integer(iDisplayLength));
		param.put("pageEnd", new Integer(iDisplayLength+iDisplayStart));
		param.put("orderName", orderName);
		param.put("orderAZ", orderAZ);
		getMapper();
		List<ProductApply> list = mapper.getProductApplyPageList(param);
		close();
		return list;
	}
	/**
	 * 根据主键查询投保产品购买申请明细信息
	 * @param id
	 * @return
	 */
	public ProductApply getProductApplyById(long id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", new Long(id));
		getMapper();
		List<ProductApply> list=mapper.getProductApplyById(param);
		close();
		if ( list != null && list.size() > 0 ) return list.get(0);
		return null;
	}
	/**
	 * 创建投保产品购买申请
	 * @param info 投保产品购买申请对象
	 */
	public void insertProductApply(ProductApply info) {
		getMapper();
		mapper.insertProductApply(info);
		System.out.println("info信息"+info.toJson());
		close();
	}
	/**
	 * 更新投保产品购买申请
	 * @param info 投保产品购买申请对象
	 */
	public void updateProductApply(ProductApply info) {
		getMapper();
		mapper.updateProductApply(info);
		close();
	}
	/**
	 * 更新投保支付状态
	 */
	public void updateProductApplyPayStatus(ProductApply info) {
		getMapper();
		mapper.updateProductApplyPayStatus(info);
		close();
	}

	/**
	 * 删除投保产品购买申请
	 * @param id
	 */
	public void deleteProductApply(long id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", new Long(id));
		getMapper();
		mapper.deleteProductApply(param);
		close();
	}
	public void  getMapper() {
		sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		mapper=sqlSession.getMapper(ApplyMapper.class);
	}
	public void close() {
		sqlSession.commit();
		sqlSession.close();
	}
	
}
