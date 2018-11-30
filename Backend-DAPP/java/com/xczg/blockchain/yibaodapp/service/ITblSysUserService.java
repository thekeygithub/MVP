package com.xczg.blockchain.yibaodapp.service;

import java.util.List;
import java.util.Map;

import com.xczg.blockchain.yibaodapp.bean.LoginUser;
import com.xczg.blockchain.yibaodapp.bean.PageList;
import com.xczg.blockchain.yibaodapp.bean.SysUserOfCodition;
import com.xczg.blockchain.yibaodapp.bean.TblSysUser;

public interface ITblSysUserService {
	
	public int insertTblSysUser(TblSysUser user);
	public int updateTblSysUser(TblSysUser user);
	public int deleteTblSysUser(String account);
	public int updatePassword(TblSysUser user);
	public List<TblSysUser> getTblSysUserPageList(PageList page);
	public int getTblSysUserCount();
	public int updateAvatarPath(TblSysUser user);
	public int checkPwd(LoginUser user);
	public int changePwd(Map<String,String> data);
	public List<TblSysUser> getTblSysUserPageListOfQuery(SysUserOfCodition Codition);
	public int getTblSysUserPageListOfCount(SysUserOfCodition Codition);
	public TblSysUser getTblSysUserByAccount(String account);
}	
