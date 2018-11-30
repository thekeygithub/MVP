package com.xczg.blockchain.yibaodapp.service.impl;

import com.xczg.blockchain.yibaodapp.bean.*;
import com.xczg.blockchain.yibaodapp.mybatis.UserCenterMapper;
import com.xczg.blockchain.yibaodapp.service.IUserCenterService;
import com.xczg.blockchain.yibaodapp.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userCenterServiceImpl")
public class UserCenterServiceImpl implements IUserCenterService{
	

	/**
	 * 对客户表进行添加操作
	 */
	public void insertCustInfo(CustInfo CustInfo) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		mapper.insertCustInfo(CustInfo);
		sqlSession.commit();
		sqlSession.close();
	}
	
	/**
	 * 对客户表进行删除操作
	 */
	public void deleteCustInfo(String id) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		int i=mapper.deleteCustInfo(id);
		sqlSession.commit();
		sqlSession.close();
	}
	
	/**
	 * 对客户表进行更新操作
	 */
	public void updateCustInfo(CustInfo CustInfo) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		mapper.updateCustInfo(CustInfo);
		sqlSession.commit();
		sqlSession.close();
	}
	
	public void updateCustInfoLoginToken(String login_token,String id) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		Map<String, String> param = new HashMap<String, String>();
		param.put("login_token", login_token);
		param.put("id", id);
		mapper.updateCustInfoLoginToken(param);
		sqlSession.commit();
		sqlSession.close();
	}
	
	/**
	 * 查询客户表记录数
	 */
	public int getCustInfoListCount() {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		return mapper.getCustInfoListCount();
	}

	/**
	 * 查询企业表记录数
	 */
	public int getEnterpriseInfoListCount() {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		return mapper.getEnterpriseInfoLsitCount();
	}
	
	/**
	 * 对企业表进行添加操作
	 */
	public void insertEnterpriseInfo(EnterpriseInfo EnterpriseInfo) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		mapper.insertEnterpriseInfo(EnterpriseInfo);
		sqlSession.commit();
		sqlSession.close();
	}
	
	/**
	 * 对企业表进行删除操作
	 */
	public void deleteEnterpriseInfo(String id) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		mapper.deleteEnterpriseInfo(id);
		sqlSession.commit();
		sqlSession.close();
	}
	
	/**
	 * 对企业表进行更新操作
	 */
	public void updateEnterpriseInfo(EnterpriseInfo EnterpriseInfo){
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		mapper.updateEnterpriseInfo(EnterpriseInfo);
		sqlSession.commit();
		sqlSession.close();
	}
	
	 /**
     * 对客户信息进行分页查询操作
     */
	@Override
	public  List<CustInfo> getCustInfoPageList(PageList page) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		List<CustInfo> date=mapper.getCustInfoPageList(page);
		return date;
	}
	
	 /**
     * 通过id查询客户信息
     */
	@Override
	public CustInfo getCustInfoById(String id) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		return mapper.getCustInfoById(id);
	}
	
	public CustInfo getCustInfoByToken(String token) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		Map<String, String> param = new HashMap<String, String>();
		param.put("login_token", token);
		List<CustInfo> list = mapper.getCustInfoByToken(param);
		if ( list != null && list.size() > 0 ) return list.get(0);
		return null;
	}
	
	 /**
     * 通过mobile查询客户信息
     */
	@Override
	public CustInfo getCustInfoByMobile(String mobile) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		List<CustInfo> list = mapper.getCustInfoByMobile(mobile);
		if ( list == null ) return null;
		if ( list.size() > 0 ) return list.get(0);
		return null;
	}
	
	 /**
     * 对企业信息进行分页查询操作
     */
	@Override
	public List<EnterpriseInfo> getEnterpriseInfoPageList(PageList page) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		return mapper.getEnterpriseInfoPageList(page);
	}
	
	/**
	 * 通过id查询企业信息
	 */
	@Override
	public EnterpriseInfo getEnterpriseInfofoById(String id) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		return mapper.getEnterpriseInfofoById(id);
	}
	
	public EnterpriseInfo getEnterpriseInfofoByLoginCodeAndType(String logincode,String enterprisetype){
		Map<String, String> param = new HashMap<String, String>();
		param.put("logincode", logincode);
		param.put("enterprisetype", enterprisetype);
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		List<EnterpriseInfo> list=mapper.getEnterpriseInfofoByLoginCodeAndType(param);
		if ( list != null && list.size() > 0 ) return list.get(0);
		return null;
	}
	
    /**
     *查询所有个人客户信息 
     */
	@Override
	public List<CustInfo> getCustInfoAll() {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		return mapper.getCustInfoAll();
	}
	
    /**
     * 查询所有企业用户信息
     */
	@Override
	public List<EnterpriseInfo> getEnterpriseInfoAll() {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		return mapper.getEnterpriseInfoAll();
	}
    /**
     * 二表查询及查询总数查询
     */


	@Override
	public List<EnterpriseInfo> getEnterpriseInfoPageListOfQuery(EnterPriseOfQueryCodition Codition) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		return mapper.getEnterpriseInfoPageListOfQuery(Codition);
	}

	@Override
	public List<CustInfo> getCustInfoPageListOfQuery(CustOfQueryCodition Codition) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		return mapper.getCustInfoPageListOfQuery(Codition);
	}

	@Override
	public int getCustInfoPageListOfCount(CustOfQueryCodition Codition) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		return mapper.getCustInfoPageListOfCount(Codition);
	}

	@Override
	public void deductCost(Map<String, Object> map) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		mapper.deductCost(map);
		sqlSession.commit();
		sqlSession.close();
	}

	@Override
	public int getEnterpriseInfoPageListOfCount(EnterPriseOfQueryCodition Codition) {
		SqlSession sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		UserCenterMapper mapper=sqlSession.getMapper(UserCenterMapper.class);
		return mapper.getEnterpriseInfoPageListOfCount(Codition);
	}



}
