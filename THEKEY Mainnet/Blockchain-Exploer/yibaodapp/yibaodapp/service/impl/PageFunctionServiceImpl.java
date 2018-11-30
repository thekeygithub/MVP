package com.xczg.blockchain.yibaodapp.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import com.xczg.blockchain.common.dao.BaseDao;
import com.xczg.blockchain.common.model.PageResult;
import com.xczg.blockchain.yibaodapp.bean.TblAuditRecord;
import com.xczg.blockchain.yibaodapp.bean.TblTransactionInfo;
import com.xczg.blockchain.yibaodapp.service.PageFunctionService;
import com.xczg.blockchain.yibaodapp.util.ChainUtil;

@Service("PageFunctionServiceImpl")
public class PageFunctionServiceImpl implements PageFunctionService{

	private static Logger log = LoggerFactory.getLogger(ChainUtil.class);

	@Autowired
	private Environment env;
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbc;
	@Resource
	private BaseDao baseDao;
	
	/**
	 * 首页功能，获取开始时间(第一个区块创建时间)，运行时间，区块数量，交易数量，用户数量
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> getNewstInfo() {
		try {
			String url = env.getProperty("neo.url");
			ChainUtil chainUtil = new ChainUtil();
			//获取第一块区块
			String StrForFirstBlk="{\"jsonrpc\": \"2.0\",\"method\": \"getblock\",\"params\": [1,1],\"id\": 1}";
			Map<String, Object> MsgForFirstBlk = chainUtil.getBlockMessage(url,StrForFirstBlk);
			Map<String, Object> result = (Map<String, Object>) MsgForFirstBlk.get("result");
			String time =result.get("time").toString();//获取到第一个区块的时间，但是为unix时间戳
			//将unix时间戳转化为北京时间
			String formats ="yyyy-MM-dd HH:mm:ss";
			if (TextUtils.isEmpty(formats))
	            formats = "yyyy-MM-dd HH:mm:ss";
	        Long timestamp = Long.parseLong(time) * 1000;
	        String startTime = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
			//获取区块数量
			String StrForBlkCount="{\"jsonrpc\": \"2.0\",\"method\": \"getblockcount\",\"params\": [],\"id\": 1}";
			Map<String, Object> MsgForBlkCount = chainUtil.getBlockMessage(url,StrForBlkCount);
			String BlkCount = MsgForBlkCount.get("result").toString();
			
			String sql = " select count(txid) as traNum,TIMESTAMPDIFF(DAY,?,?)"
						+" as start_days,count(distinct sender)+count(distinct receiver) as user_num  from tbl_transaction_info";
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//得到当天日期
			String currentDay = sdf.format(new Date());
			Map<String, Object> NewestInfo = 
					jdbc.query(sql.toString(),new String[]{startTime.substring(0,10),currentDay},new ResultSetExtractor<Map<String, Object>>(){

				@Override
				public Map<String, Object> extractData(ResultSet rs)
						throws SQLException, DataAccessException {
					Map<String, Object> temp = null;
					while(rs.next()){
						temp = new HashMap<String, Object>();	
						temp.put("traNum", rs.getString("traNum"));//交易数量
						temp.put("start_days", rs.getString("start_days"));//启动时间
						temp.put("user_num", rs.getString("user_num"));//用户数量
					}
					return temp;
				}
				
			});	
				NewestInfo.put("startTime", startTime.substring(0,10));
				NewestInfo.put("BlkCount", BlkCount);
				return NewestInfo;
		} catch (Exception e) {
			log.error("获取区块链最新信息 失败 " + e.getMessage(), e.toString());
			return null;
		}
		
		}
	
	/**
	 * 根据区块索引查询区块信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> searchBlockInfo(int blockIndex) {
		try {		
			String url = env.getProperty("neo.url");
			ChainUtil chainUtil = new ChainUtil();
			String StrForFirstBlk="{\"jsonrpc\": \"2.0\",\"method\": \"getblock\",\"params\": ["+blockIndex+",1],\"id\": 1}";
			Map<String, Object> MsgForFirstBlk = chainUtil.getBlockMessage(url,StrForFirstBlk);
			Map<String, Object> blockInfo = (Map<String, Object>) MsgForFirstBlk.get("result");
			String formats ="yyyy-MM-dd HH:mm:ss";
			String time = blockInfo.get("time").toString();
			if (TextUtils.isEmpty(formats))
				formats = "yyyy-MM-dd | HH:mm:ss";
			Long timestamp = Long.parseLong(time) * 1000;
			String startTime = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
			blockInfo.put("createTime", startTime);
			return blockInfo;
		} catch (Exception e) {
			log.error("调用区块链,通过区块索引获取区块信息 失败 " + e.getMessage(), e.toString());
			return null;
		}
	}

	/**
	 * 根据交易标识查询
	 * 第一步,根据交易信息获取到区块hash
	 * 第二步,根据区块hash获取到对应区块信息
	 * 第三部,查询数据库获取该交易信息对应的区账户信息
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> searchtraInfo(String searchInfo) {
		try {
			String url = env.getProperty("neo.url");
			ChainUtil chainUtil = new ChainUtil();
			//根据交易标识获取区块hash
			String searchBlkHash="{\"jsonrpc\": \"2.0\",\"method\": \"getrawtransaction\",\"params\": [\""+searchInfo+"\",1],\"id\": 1}";
			Map<String, Object> traInfo = chainUtil.getBlockMessage(url,searchBlkHash);
			//判断是否获取到数据
				Map<String, Object> blockInfo = (Map<String, Object>) traInfo.get("result");
				String hash = blockInfo.get("blockhash").toString();
			//根据区块hash获取对应区块信息
			String block="{\"jsonrpc\": \"2.0\",\"method\": \"getblock\",\"params\": [\""+hash+"\",1],\"id\": 1}";
			Map<String, Object> resultInfo = chainUtil.getBlockMessage(url,block);
			Map<String, Object> map = (Map<String, Object>) resultInfo.get("result");
			//根据交易标识查询对应的账户信息
			String sql = "select sender_addr,receiver_addr from tbl_transaction_info where txid = ?";
			List<Map<String, Object>> addInfo = 
					jdbc.query(sql.toString(),new String[]{searchInfo},new ResultSetExtractor<List<Map<String, Object>>>(){
						
						@Override
						public List<Map<String, Object>> extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							List<Map<String, Object>> temp = new ArrayList<Map<String,Object>>();
							Map<String, Object> info = new HashMap<String, Object>(); 
							while(rs.next()){
								info.put("sender_addr", rs.getString("sender_addr"));//发送方钱包地址
								info.put("receiver_addr", rs.getString("receiver_addr"));//接收方钱包地址
								temp.add(info);
							}
							return temp;
						}
						
					});	
			map.put("addInfo", addInfo);
			return map;
		} catch (Exception e) {
			log.error("调用区块链,通过区块索引获取区块信息 失败 " + e, e.toString());
			return null;
		}
	}
	
	/**
	 * 获取最新的交易信息
	 */
	@Override
	public PageResult<TblTransactionInfo> findPageBySql(
			PageResult<TblTransactionInfo> pageResult) {
		TblTransactionInfo info = new TblTransactionInfo();
		String orderSql = "order by time desc";
		PageResult<TblTransactionInfo> queryPageByEntity = baseDao.queryPageByEntity(info,pageResult,orderSql);
		return queryPageByEntity;
	}

	@Override
	public PageResult<TblAuditRecord> queryPageByEntity(
			TblAuditRecord auditRecord, PageResult<TblAuditRecord> pageResult) {
		String ordersql = " order by TREATMENT_DATE desc "; 
		PageResult<TblAuditRecord> queryPageByEntity = baseDao.queryPageByEntity(auditRecord, pageResult, ordersql);
		return queryPageByEntity;
	}

	@Override
	public List<TblTransactionInfo> getTraInfoByID(String txid) {
		String sql = " and txid in ("+txid+") ";
		TblTransactionInfo info = new TblTransactionInfo();
		List<TblTransactionInfo> queryByEntity = baseDao.queryByEntity(info,sql);
		return queryByEntity;
	}
	
	
	
}
