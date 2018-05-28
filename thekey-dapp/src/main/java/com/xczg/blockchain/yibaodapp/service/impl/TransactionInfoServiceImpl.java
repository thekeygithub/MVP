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
import com.xczg.blockchain.yibaodapp.bean.TblTransactionInfo;
import com.xczg.blockchain.yibaodapp.service.ITransactionInfoService;
import com.xczg.blockchain.yibaodapp.util.DateUtil;

@Service("transactionInfoService")
public class TransactionInfoServiceImpl implements ITransactionInfoService {

	@Resource
	private BaseDao baseDao;

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbc;

	public List<Integer> saveList(List<TblTransactionInfo> list)
			throws Exception {
		return baseDao.save(list);
	}

	public void save(TblTransactionInfo entity) throws Exception {
		baseDao.save(entity);
	}

	@Override
	public String getSerialNo() throws Exception {

		String sql = "select s.SERIAL_NO from tbl_transaction_info s order by  s.TIME desc limit 1 ";
		String serialNo = jdbc.query(sql.toString(), new String[] {},
				new ResultSetExtractor<String>() {
					@Override
					public String extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						String serialNo = "";
						while (rs.next()) {
							serialNo = rs.getString("SERIAL_NO");
						}
						return serialNo;
					}

				});
		
		if(null !=serialNo){
			String[] day_index=serialNo.split("-");
			if( serialNo.startsWith(DateUtil.getDay2())){
				serialNo=day_index[0]+"-"+String.format("%08d",(Integer.valueOf(day_index[1])+1));
			}else{
				serialNo=DateUtil.getDay2()+"-"+String.format("%08d", 1);
			}
		}
		return serialNo;
	}

}
