package com.xczg.blockchain.yibaodapp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import com.xczg.blockchain.yibaodapp.bean.LoginUser;
import com.xczg.blockchain.yibaodapp.bean.PageList;
import com.xczg.blockchain.yibaodapp.bean.SysUserOfCodition;
import com.xczg.blockchain.yibaodapp.bean.TblSysUser;
import com.xczg.blockchain.yibaodapp.mybatis.UserCenterMapper;
import com.xczg.blockchain.yibaodapp.service.ITblSysUserService;
import com.xczg.blockchain.yibaodapp.util.SqlSessionFactoryUtil;
@Service("sysUserService")
public class SysUserServiceImpl implements ITblSysUserService {

	private UserCenterMapper mapper;
	private SqlSession sqlSession; 
	
	
	@Override
	public int insertTblSysUser(TblSysUser user) {
		mapper=getMapper();
		int i=mapper.insertTblSysUser(user);
		close();
		return i;
	}

	@Override
	public int updateTblSysUser(TblSysUser user) {
		mapper=getMapper();
		int i=mapper.updateTblSysUser(user);
		close();
		return i;
	}

	@Override
	public int deleteTblSysUser(String account) {
		mapper=getMapper();
		int i=mapper.deleteTblSysUser(account);
		close();
		return i;
	}

	@Override
	public int updatePassword(TblSysUser user) {
		mapper=getMapper();
		int i=mapper.updatePassword(user);
		close();
		return i;
	}

	@Override
	public List<TblSysUser> getTblSysUserPageList(PageList page) {
		mapper=getMapper();
		List<TblSysUser>list=new ArrayList<>();
		list=mapper.getTblSysUserPageList(page);
		close();
		return list;
	}

	@Override
	public int getTblSysUserCount() {
		mapper=getMapper();
		int i=mapper.getTblSysUserCount();
	
		return i;
	}

	@Override
	public int updateAvatarPath(TblSysUser user) {
		mapper=getMapper();
		int i=mapper.updateAvatarPath(user);
		close();
		return i;
	}
	public UserCenterMapper getMapper(){
		 sqlSession = new SqlSessionFactoryUtil().getSqlSessionFactory().openSession();
		 mapper=sqlSession.getMapper(UserCenterMapper.class);
		return mapper;
	}
	public void close() {
		sqlSession.commit();
		sqlSession.close();
	}

	@Override
	public int checkPwd(LoginUser user) {
		 mapper=getMapper();
		 int result = mapper.checkPwd(user);
		 close();
		 return result;	
	}

	@Override
	public int changePwd(Map<String, String> data) {
		mapper=getMapper();
		int result = mapper.changePwd(data);
		close();
		return result;
	}

	@Override
	public List<TblSysUser> getTblSysUserPageListOfQuery(SysUserOfCodition Codition) {
		mapper=getMapper();
		List<TblSysUser>list=new ArrayList<TblSysUser>();
		list=mapper.getTblSysUserPageListOfQuery(Codition);
		close();
		return list;	
	}
	
	@Override
	public TblSysUser getTblSysUserByAccount(String account) {
		mapper=getMapper();
		Map<String, String> map=new HashMap<String,String>();
		map.put("account", account);
		List<TblSysUser>list=mapper.getTblSysUserByAccount(map);
		if ( list !=null && list.size() > 0 ) return list.get(0);
		close();
		return null;	
	}

	@Override
	public int getTblSysUserPageListOfCount(SysUserOfCodition Codition) {
		mapper=getMapper();
		 int result = mapper.getTblSysUserPageListOfCount(Codition);
		 close();
		 return result;	
	}

	
	
}
