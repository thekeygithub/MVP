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
import com.xczg.blockchain.yibaodapp.bean.TblTranfer;
import com.xczg.blockchain.yibaodapp.service.ITranferService;

@Service("tranferService")
public class TranferServiceImpl implements ITranferService {

	@Resource
	private BaseDao baseDao;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbc;

	@Override
	public void save(TblTranfer entity) throws Exception {
		baseDao.save(entity);
	}

	public void update(TblTranfer entity) throws Exception {
		baseDao.update(entity);
	}

	@Override
	public List<TblTranfer> getUndo() {

		String sql = "select s.VALID_KEY , s.TYPE_COUNT , s.SERIAL_NO from TBL_TRANFER s where s.STATE='0' ";
		List<TblTranfer> list = jdbc.query(sql.toString(), new String[] {},
				new ResultSetExtractor<List<TblTranfer>>() {
					@Override
					public List<TblTranfer> extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						List<TblTranfer> list2 = new ArrayList<TblTranfer>();
						TblTranfer tblTranfer = null;
						while (rs.next()) {
							tblTranfer = new TblTranfer(rs
									.getString("VALID_KEY"), rs
									.getString("TYPE_COUNT"), "0", rs
									.getString("SERIAL_NO"));
							list2.add(tblTranfer);
						}
						return list2;
					}

				});
		return list;

	}

}
