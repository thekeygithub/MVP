package com.xczg.blockchain.yibaodapp.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xczg.blockchain.yibaodapp.bean.TblAuditRecord;
import com.xczg.blockchain.yibaodapp.service.IStatementService;
import com.xczg.blockchain.yibaodapp.service.ITblAuditRecordService;
import com.xczg.blockchain.yibaodapp.util.DateUtil;
import com.xczg.blockchain.yibaodapp.util.IpfsUtil;
import com.hollycrm.hollyuniproxy.test.HttpJsonBlockListClient;

@Component
public class CpaAfterAuditTask {

	private static Logger log = LoggerFactory
			.getLogger(CpaAfterAuditTask.class);

	@Autowired
	private IStatementService statementService;

	@Autowired
	private ITblAuditRecordService auditRecordService;

	@Autowired
	private Environment env;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Scheduled(cron = "0 0 1 * * ?")
	// 每天凌晨1点执行一次：0 0 1 * * ?
	// 每分钟执行一次0 0/1 * * * ?
	public void reportCurrentTimeCron() throws InterruptedException {
		System.out.println("CpaAfterAuditTask:" + DateUtil.getNow());
		// 获取前一天的所有hash
		List<String> ipfsHashs = statementService.getHashInTheDay();
		System.out.println("ipfsHashs:" + ipfsHashs);

		if (null != ipfsHashs && ipfsHashs.size() > 0) {
			String getUserInformationUrl = env
					.getProperty("ipfs.getUserInformation.url");

			Map respMap = new HashMap<String, Object>();
			List<Map<String, Object>> illegalList = null;
			TblAuditRecord tblAuditRecord = null;
			String medicalInstitution = "";// 医疗机构
			String treatType = "";// 就诊类型
			String sfzh = "";// 身份证号
			String prediagname = "";
			String treatmentDate = "";// 就诊日期
			String sex = "";// 性别
			String name = "";// 姓名
			String cpaaUrl = env.getProperty("CPAA.url");
			for (String ipfsHash : ipfsHashs) {
				// 通过Hash 获取结算单
				Map<String, Object> settlementOrder = IpfsUtil
						.getUserInformation(getUserInformationUrl, ipfsHash,
								"4");
				System.out.println(ipfsHash +"settlementOrder:"+ settlementOrder);
				
				Map<String, Object> payInfo = (Map<String, Object>) settlementOrder
						.get("payInfo");
				Map<String, Object> illegalMap = new HashMap<String, Object>();
				List<Map<String, Object>> e01_inp_list01 = (List<Map<String, Object>>) payInfo
						.get("E01_INP_LIST01");
				for (Map<String, Object> e : e01_inp_list01) {
					illegalMap.put(e.get("E01_INP_LIST01_NO01").toString(),
							e.get("itemname"));
				}
				List<Map<String, Object>> e01_inp_list02 = (List<Map<String, Object>>) payInfo
						.get("E01_INP_LIST02");
				for (Map<String, Object> e : e01_inp_list02) {
					illegalMap.put(e.get("E01_INP_LIST02_NO01").toString(),
							e.get("itemname"));
				}
				medicalInstitution = payInfo.get("E01_INP_NO07") == null ? ""
						: payInfo.get("E01_INP_NO07").toString();// 医疗机构
				treatType = payInfo.get("med_type") == null ? "" : payInfo.get(
						"med_type").toString();// 就诊类型
				sfzh = payInfo.get("E01_INP_NO04") == null ? "" : payInfo.get(
						"E01_INP_NO04").toString();// 身份证号
				prediagname = payInfo.get("prediagname") == null ? "" : payInfo
						.get("prediagname").toString();// 诊断/诊疗
				treatmentDate = payInfo.get("treatmentdate") == null ? ""
						: payInfo.get("treatmentdate").toString();// 就诊日期
				sex = payInfo.get("patientgend") == null ? "" : payInfo.get(
						"patientgend").toString();
				name = payInfo.get("E01_INP_NO03") == null ? "" : payInfo.get(
						"E01_INP_NO03").toString();

				System.out.println(ipfsHash +"reqJson:"+ JSON.toJSONString(payInfo));
				String resJson = HttpJsonBlockListClient.requestNeo(cpaaUrl,
						JSON.toJSONString(payInfo));
				System.out.println(ipfsHash +"resJson:"+ resJson);

				respMap = (Map) JSONObject.parseObject(resJson);
				illegalList = (List<Map<String, Object>>) respMap
						.get("E01_OUT_SUB01");
				String entryStr = "";
				if (null != illegalList && illegalList.size() > 0) {
					for (Map<String, Object> entry : illegalList) {
						entryStr = entry.toString();
						tblAuditRecord = new TblAuditRecord(ipfsHash,
								medicalInstitution, treatType, illegalMap.get(
										entryStr.substring(
												entryStr.indexOf("\"") + 1,
												entryStr.indexOf(":") - 1))
										.toString(), entryStr.substring(
										entryStr.indexOf(":") + 2,
										entryStr.lastIndexOf("\"")), sfzh,
								prediagname, treatmentDate, sex, name);
						try {
							auditRecordService.save(tblAuditRecord);
							System.out.println("tblAuditRecord:"
									+ tblAuditRecord);
						} catch (Exception e) {
							log.error("CPA事后审核统计任务 error " + e.getMessage(), e);
						}
					}
				}

			}

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {

		String aStr = readFileByLines("C:\\Users\\Administrator\\Desktop\\区块链\\IPFS接口模拟数据\\CPA事后-张颖.json");
		Map<String, Object> payInfo = (Map<String, Object>) ((Map<String, Object>) JSONObject
				.parseObject(aStr).get("rsJsonString")).get("payInfo");
		Map<String, Object> illegalMap = new HashMap<String, Object>();
		List<Map<String, Object>> e01_inp_list01 = (List<Map<String, Object>>) payInfo
				.get("E01_INP_LIST01");
		if (null != e01_inp_list01 && e01_inp_list01.size() > 0) {
			for (Map<String, Object> e : e01_inp_list01) {
				illegalMap.put(e.get("E01_INP_LIST01_NO01").toString(),
						e.get("itemname"));
			}
		}
		List<Map<String, Object>> e01_inp_list02 = (List<Map<String, Object>>) payInfo
				.get("E01_INP_LIST02");
		if (null != e01_inp_list02 && e01_inp_list02.size() > 0) {
			for (Map<String, Object> e : e01_inp_list02) {
				illegalMap.put(e.get("E01_INP_LIST02_NO01").toString(),
						e.get("itemname"));
			}
		}
		String medicalInstitution = payInfo.get("E01_INP_NO07") == null ? ""
				: payInfo.get("E01_INP_NO07").toString();// 医疗机构
		String treatType = payInfo.get("med_type") == null ? "" : payInfo.get(
				"med_type").toString();// 就诊类型
		String sfzh = payInfo.get("E01_INP_NO04") == null ? "" : payInfo.get(
				"E01_INP_NO04").toString();// 身份证号
		String prediagname = payInfo.get("prediagname") == null ? "" : payInfo
				.get("prediagname").toString();// 诊断/诊疗
		String treatmentDate = payInfo.get("treatmentdate") == null ? ""
				: payInfo.get("treatmentdate").toString();// 就诊日期

		String sex = payInfo.get("patientgend") == null ? "" : payInfo.get(
				"patientgend").toString();
		String name = payInfo.get("E01_INP_NO03") == null ? "" : payInfo.get(
				"E01_INP_NO03").toString();

		String resJson = HttpJsonBlockListClient.requestNeo(
				"http://59.110.174.56:9002/ebaoAPI/API.do",
				JSON.toJSONString(payInfo));

		Map respMap = (Map) JSONObject.parseObject(resJson);
		List<Map<String, Object>> illegalList = (List<Map<String, Object>>) respMap
				.get("E01_OUT_SUB01");
		String entryStr = "";
		if (null != illegalList && illegalList.size() > 0) {
			for (Map<String, Object> entry : illegalList) {
				entryStr = entry.toString();
				TblAuditRecord tblAuditRecord = new TblAuditRecord("",
						medicalInstitution, treatType, illegalMap.get(
								entryStr.substring(entryStr.indexOf("\"") + 1,
										entryStr.indexOf(":") - 1)).toString(),
						entryStr.substring(entryStr.indexOf(":") + 2,
								entryStr.lastIndexOf("\"")), sfzh, prediagname,
						treatmentDate, sex, name);
				System.out.println(tblAuditRecord);
			}
		}

	}

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	public static String readFileByLines(String fileName) {
		StringBuffer sb = new StringBuffer();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					file), "UTF-8");
			reader = new BufferedReader(isr);
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				sb.append(new String(tempString.getBytes(), "UTF-8"));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return sb.toString();
	}

}
