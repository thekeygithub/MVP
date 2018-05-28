package com.xczg.blockchain.yibaodapp.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import com.xczg.blockchain.common.dao.BaseDao;
import com.xczg.blockchain.yibaodapp.bean.TblStatementKey;
import com.xczg.blockchain.yibaodapp.service.IStatementService;
import com.xczg.blockchain.yibaodapp.util.DateUtil;

@Service("statementService")
public class StatementServiceImpl implements IStatementService {

	@Resource
	private BaseDao baseDao;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbc;

	@Override
	public List<String> getHashInTheDay() {
		String startDate = DateUtil.getBeforeDay() + " 00:00:00";
		String endDate = DateUtil.getBeforeDay() + " 23:59:59";
		String sql = "select s.IPFS_HASH from TBL_STATEMENT_KEY s where s.CREATE_TIME >= ?  and  s.CREATE_TIME <= ?";
		List<String> ipfsHashs = jdbc.query(sql.toString(), new String[] {
				startDate, endDate }, new ResultSetExtractor<List<String>>() {
			@Override
			public List<String> extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				List<String> ipfsHashList = new ArrayList<String>();
				while (rs.next()) {
					ipfsHashList.add(rs.getString("IPFS_HASH"));
				}
				return ipfsHashList;
			}

		});
		return ipfsHashs;
	}

	public void save(TblStatementKey entity) throws Exception {
		baseDao.save(entity);
	}

}
