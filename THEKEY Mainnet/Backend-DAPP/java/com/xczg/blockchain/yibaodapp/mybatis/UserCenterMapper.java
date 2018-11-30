package com.xczg.blockchain.yibaodapp.mybatis;

import com.xczg.blockchain.yibaodapp.bean.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface UserCenterMapper {
	
	/*
	 * 对客户表的增删改查
	 */
	public List<CustInfo> getCustInfoAll();
	public List<CustInfo> getCustInfoPageList(PageList page);
	public void insertCustInfo(CustInfo CustInfo);
	public void updateCustInfo(CustInfo CustInfo);
	public int deleteCustInfo(String id);
	public int getCustInfoListCount();
	public CustInfo getCustInfoById(String id);
	public  List<CustInfo> getCustInfoByMobile(String mobile);
	public  List<CustInfo> getCustInfoByToken(Map<String,String> map);
	public int addVaildKey(String validKey);
	//更新余额
    int deductCost(Map<String,Object> map);
    public void updateCustInfoLoginToken(Map<String,String> map);
	/*
	 * 对企业表的增删改查
	 */
	public List<EnterpriseInfo> getEnterpriseInfoAll();
	public List<EnterpriseInfo> getEnterpriseInfoPageList(PageList page);
	public void insertEnterpriseInfo(EnterpriseInfo EnterpriseInfo);
	public void updateEnterpriseInfo(EnterpriseInfo EnterpriseInfo);
	public void deleteEnterpriseInfo(String id);
	//public Page getPageList();
	public int getEnterpriseInfoLsitCount();
	public EnterpriseInfo getEnterpriseInfofoById(String id);
	public List<EnterpriseInfo> getEnterpriseInfofoByLoginCodeAndType(Map<String, String> map);
	
	/*
	 * 对系统管理员表的操作
	 */
	public int insertTblSysUser(TblSysUser user);
	public int updateTblSysUser(TblSysUser user);
	public int deleteTblSysUser(String account);
	public int updatePassword(TblSysUser user);
	public List<TblSysUser> getTblSysUserPageList(PageList page);
	public int getTblSysUserCount();
	public int updateAvatarPath(TblSysUser user);
	public int checkPwd(LoginUser user);
	public int changePwd(Map<String, String> data);
	public List<TblSysUser> getTblSysUserByAccount(Map<String, String> map);
 
	public VerifyInfo select();

	/*
	 * 三表条件查询
	 */
	public List<TblSysUser> getTblSysUserPageListOfQuery(SysUserOfCodition Codition);
	public List<EnterpriseInfo> getEnterpriseInfoPageListOfQuery(EnterPriseOfQueryCodition Codition);
	public List<CustInfo> getCustInfoPageListOfQuery(CustOfQueryCodition Codition);
	public int getCustInfoPageListOfCount(CustOfQueryCodition Codition);
	public int getEnterpriseInfoPageListOfCount(EnterPriseOfQueryCodition Codition);
	public int getTblSysUserPageListOfCount(SysUserOfCodition Codition);
}