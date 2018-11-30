package com.xczg.blockchain.yibaodapp.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

public class ChainTest {
	
	public static void getStorage() {
		Map<String, Object> inParam = new HashMap<String, Object>();
        inParam.put("jsonrpc", "2.0");
        inParam.put("method", "getstorage"); 
        inParam.put("id", "15");
        Object paramsArr[] = new Object[2];
        paramsArr[0] = "03febccf81ac85e3d795bc5cbd4e84e907812aa3"; // 合约脚本散列
        paramsArr[1] = "5065746572";  // 存储区的键。（需要转化为hex string）
        inParam.put("params",paramsArr);
        try {
            String inJson = JSONObject.toJSONString(inParam);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject("http://222.128.14.106:3226", inJson, String.class);
            JSONObject jsObj = JSONObject.parseObject(response);
            System.out.println("返回结果："+jsObj);
            // 返回结果：{"result":"4c696e","id":"15","jsonrpc":"2.0"}
        }catch(Exception e){
           System.out.println(e);
        }
	}
	
	public static void setStorage() {
		Map<String, Object> inParam = new HashMap<String, Object>();
        inParam.put("jsonrpc", "2.0");
        inParam.put("method", "getstorage");  // 智能合约脚本哈希 0x33ee36c712b37df8acfbda4a1beb165e100ed3e0
        inParam.put("id", "15");
        Object paramsArr[] = new Object[2];
        paramsArr[0] = "03febccf81ac85e3d795bc5cbd4e84e907812aa3"; // 合约脚本散列
        paramsArr[1] = "5065746572";  // 存储区的键。（需要转化为hex string）
        inParam.put("params",paramsArr);
        try {
            String inJson = JSONObject.toJSONString(inParam);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject("http://222.128.14.106:3226", inJson, String.class);
            JSONObject jsObj = JSONObject.parseObject(response);
            System.out.println("返回结果："+jsObj);
            // 返回结果：{"result":"4c696e","id":"15","jsonrpc":"2.0"}
        }catch(Exception e){
           System.out.println(e);
        }
	}
	
	public static void invokefunction() {
		Map<String, Object> inParam = new HashMap<String, Object>();
        inParam.put("jsonrpc", "2.0");
        inParam.put("method", "invokefunction"); 
        inParam.put("id", "3");
        Object paramsArr[] = new Object[3];
        paramsArr[0] = "0x33ee36c712b37df8acfbda4a1beb165e100ed3e0"; // 合约脚本散列
        paramsArr[1] = "saveResult"; 
        
        Map<String, Object> inParam2 = new HashMap<String, Object>();
        inParam2.put("type", "Hash160");
        inParam2.put("value", "0xa7274594ce215208c8e309e8f2fe05d4a9ae412b"); 
        Object paramsArr2[] = new Object[1];
        paramsArr2[0]=inParam2;
        paramsArr[2] = paramsArr2; 
        
        inParam.put("params",paramsArr);
        try {
            String inJson = JSONObject.toJSONString(inParam);
            
            System.out.println("请求数据："+inJson);
            
            
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject("http://222.128.14.106:3226", inJson, String.class);
            JSONObject jsObj = JSONObject.parseObject(response);
            System.out.println("返回结果："+jsObj);
            // 返回结果：{"result":"4c696e","id":"15","jsonrpc":"2.0"}
        }catch(Exception e){
           System.out.println(e);
        }
	}
	
	
	public static void invokefunctionSample() {
		Map<String, Object> inParam = new HashMap<String, Object>();
        inParam.put("jsonrpc", "2.0");
        inParam.put("method", "invokefunction"); 
        inParam.put("id", "3");
        Object paramsArr[] = new Object[3];
        paramsArr[0] = "0xecc6b20d3ccac1ee9ef109af5a7cdb85706b1df9"; // 合约脚本散列
        
        Map<String, Object> inParam2 = new HashMap<String, Object>();
        inParam2.put("type", "Hash160");
        inParam2.put("value", "0xa7274594ce215208c8e309e8f2fe05d4a9ae412b"); 
        
        Object paramsArr2[] = new Object[1];
        paramsArr2[0]=inParam2;
           
        paramsArr[1] = "balanceOf"; 
        
        paramsArr[2] = paramsArr2; 
        
        inParam.put("params",paramsArr);
        try {
            String inJson = JSONObject.toJSONString(inParam);
            
            System.out.println("请求数据："+inJson);
            
            
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject("http://222.128.14.106:3226", inJson, String.class);
            JSONObject jsObj = JSONObject.parseObject(response);
            System.out.println("返回结果："+jsObj);
            // 返回结果：{"result":"4c696e","id":"15","jsonrpc":"2.0"}
        }catch(Exception e){
           System.out.println(e);
        }
	}

	public static void main(String[] args) {
		//getStorage();
		invokefunction();
	}

}
