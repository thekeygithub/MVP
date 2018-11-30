package com.xczg.blockchain.yibaodapp.service;

import java.util.List;
import java.util.Map;

import com.xczg.blockchain.yibaodapp.bean.CustInfo;
import com.xczg.blockchain.yibaodapp.bean.CustOfQueryCodition;
import com.xczg.blockchain.yibaodapp.bean.EnterPriseOfQueryCodition;
import com.xczg.blockchain.yibaodapp.bean.EnterpriseInfo;
import com.xczg.blockchain.yibaodapp.bean.PageList;
import com.xczg.blockchain.yibaodapp.bean.SysUserOfCodition;
import com.xczg.blockchain.yibaodapp.bean.TblSysUser;

public interface IUserCenterService {
	
	/**
	 * 对客户表进行添加操作
	 * @param CustInfo
	 */
	public void insertCustInfo(CustInfo CustInfo);
   
	/**
	 * 查询个人客户表所有信息
	 * @return
	 */
	public CustInfo getCustInfoByMobile(String mobile);
	
	/**
	 * 根据Token查询个人客户
	 * @return
	 */
	public CustInfo getCustInfoByToken(String token);
	
	/**
	 * 查询个人客户表所有信息
	 * @return
	 */
	public List<CustInfo> getCustInfoAll();
	
	/**
	 * 对客户表进行删除操作
	 * @param id
	 */
    public void deleteCustInfo(String id);
    
    /**
     * 对客户表进行更新操作
     * @param CustInfo
     */
    public void updateCustInfo(CustInfo CustInfo);
    
    public void updateCustInfoLoginToken(String login_token,String id);
    
    /**
     * 查询客户表记录数
     * @return
     */
    public int getCustInfoListCount();
    
    /**
     * 查询企业表记录数
     * @return
     */
    public int getEnterpriseInfoListCount();
    
    /**
     * 对企业表进行添加操作
     * @param EnterpriseInfo
     */
    public void insertEnterpriseInfo(EnterpriseInfo EnterpriseInfo);
    
    /**
     * 对企业表进行删除操作
     * @param id
     */
    public void deleteEnterpriseInfo(String id);
    
    /**
     * 查询所有企业用户信息
     * @return
     */
    public List<EnterpriseInfo> getEnterpriseInfoAll();
    
    /**
     * 对企业表进行更新操作
     * @param EnterpriseInfo
     */
    public void updateEnterpriseInfo(EnterpriseInfo EnterpriseInfo);
    
    /**
     * 对客户信息进行分页查询操作
     * @param page
     * @return
     */
    public List<CustInfo> getCustInfoPageList(PageList page);
    
    /**
     * 通过id查询客户信息
     * @param id
     * @return
     */
    public CustInfo getCustInfoById(String id);
    
    /**
     * 对企业表进行分页查询操作
     * @param page
     * @return
     */
    public List<EnterpriseInfo> getEnterpriseInfoPageList(PageList page);
   
    /**
     * 通过id查询企业信息
     * @param id
     * @return
     */
    public EnterpriseInfo getEnterpriseInfofoById(String id);
    /**
     * 二表查询
     * @param map
     * @return
     */
	public List<EnterpriseInfo> getEnterpriseInfoPageListOfQuery(EnterPriseOfQueryCodition Codition);
	public List<CustInfo> getCustInfoPageListOfQuery(CustOfQueryCodition Codition);
	public int getEnterpriseInfoPageListOfCount(EnterPriseOfQueryCodition Codition);
	public int getCustInfoPageListOfCount(CustOfQueryCodition Codition);
	public EnterpriseInfo getEnterpriseInfofoByLoginCodeAndType(String logincode,String enterprisetype);
	
    //更新余额
    void deductCost(Map<String,Object> map);

}
