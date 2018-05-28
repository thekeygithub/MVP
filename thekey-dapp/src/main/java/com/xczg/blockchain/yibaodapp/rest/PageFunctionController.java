package com.xczg.blockchain.yibaodapp.rest;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xczg.blockchain.common.dao.BaseDao;
import com.xczg.blockchain.common.model.PageResult;
import com.xczg.blockchain.yibaodapp.bean.TblAuditRecord;
import com.xczg.blockchain.yibaodapp.bean.TblTransactionInfo;
import com.xczg.blockchain.yibaodapp.service.PageFunctionService;

@Controller
@RequestMapping("pageFunction")
public class PageFunctionController {

	@Autowired
	private BaseDao baseDao;

	@Autowired
	private PageFunctionService pageFunctionService;

	@RequestMapping(value = "/getNewstInfo")
	@ResponseBody
	public Map<String, Object> getNewstInfo() {
		Map<String, Object> newstInfo = pageFunctionService.getNewstInfo();
		return newstInfo;
	}

	@RequestMapping("/serchFunction")
	public String serchFunction(String searchInfo) {
		// 判断查询内容为纯数字还是字符串，纯数字代表查询区块，字符串代表查询交易信息
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		boolean matches = pattern.matcher(searchInfo).matches();
		if (matches) {
			// 纯数字，查询区块
			return "blockInfo";
		} else {
			// 字符串，查询交易记录
			return "traInfo";
		}
	}

	@RequestMapping("/toTraInfoPage")
	public String toTraInfoPage(String searchInfo) {
		return "traInfo";
	}

	// 查询区块信息
	@RequestMapping("/searchBlockInfo")
	@ResponseBody
	public Map<String, Object> searchBlockInfo(String searchInfo) {
		int blockIndex = Integer.parseInt(searchInfo);
		Map<String, Object> blockInfo = pageFunctionService
				.searchBlockInfo(blockIndex);
		return blockInfo;
	}

	// 查询交易信息
	@RequestMapping("/searchtraInfo")
	@ResponseBody
	public Map<String, Object> searchtraInfo(String searchInfo) {
		Map<String, Object> blockInfo = pageFunctionService
				.searchtraInfo(searchInfo);
		return blockInfo;
	}

	// 查询交易数量,有分页功能
	@RequestMapping("/getNewestTraInfo")
	@ResponseBody
	public PageResult<TblTransactionInfo> getNewestTraInfo(int page, int rows) {

		PageResult<TblTransactionInfo> pageResult = new PageResult<TblTransactionInfo>();
		pageResult.setPageNo(page);
		pageResult.setPageSize(rows);
		pageResult = pageFunctionService.findPageBySql(pageResult);
		pageResult.getTotal();
		return pageResult;
	}

	/**
	 * 审计页面查询需求
	 */
	@RequestMapping(value = "/getAuditRecord", method = RequestMethod.POST)
	public @ResponseBody PageResult<TblAuditRecord> getAuditRecord(
			TblAuditRecord auditRecord, int page, int rows) {
		PageResult<TblAuditRecord> pageResult = new PageResult<TblAuditRecord>();
		pageResult.setPageNo(page);
		pageResult.setPageSize(rows);
		PageResult<TblAuditRecord> queryPageByEntity = pageFunctionService
				.queryPageByEntity(auditRecord, pageResult);

		return queryPageByEntity;
	}

	/**
	 * 根据交易id查询数据库
	 */

	@RequestMapping("/getTraInfoByID")
	@ResponseBody
	public List<TblTransactionInfo> getTraInfoByID(String txid) {
		txid = txid.substring(0, txid.length() - 1);
		List<TblTransactionInfo> traInfoByID = pageFunctionService
				.getTraInfoByID(txid);
		return traInfoByID;
	}

}
