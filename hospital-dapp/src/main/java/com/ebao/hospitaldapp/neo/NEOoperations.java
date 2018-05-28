package com.ebao.hospitaldapp.neo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ebao.hospitaldapp.config.ApplicationConfig;
import com.ebao.hospitaldapp.rest.base.enums.ChainDataType;
import com.ebao.hospitaldapp.rest.base.utils.HospitalAccountEntity;
import com.ebao.hospitaldapp.rest.base.utils.HospitalAccountRedisService;
import com.ebao.hospitaldapp.rest.base.utils.UserAccountEntity;
import com.ebao.hospitaldapp.rest.base.utils.UserAccountRedisService;

import com.ebao.hospitaldapp.rest.entity.BlockChainResultEntity;
import com.ebao.hospitaldapp.utils.JsonUtils;
import com.ebao.hospitaldapp.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

@Component
public class NEOoperations {

    final Logger LOGGER = LoggerFactory.getLogger(NEOoperations.class);
    @Autowired
    private UserAccountRedisService userAccountRedisService;
    @Autowired
    private HospitalAccountRedisService hospitalAccountRedisService;
    @Autowired
    private ApplicationConfig applicationConfig;

    // 将加密的IPFS文件hashkey存入区款连
    // 返回validKey，发给医院dapp
    public BlockChainResultEntity saveToBlockchain(String hashKey, ChainDataType type)
    {
        try {
            if(!type.isValid()){
                LOGGER.error("调用区块链, 存储IPFS哈希失败, 数据类型错误: {}", type.getValue());
                return null;
            }
            String methodStr = "";
            if(type.equals(ChainDataType.VALIDINFO))
                methodStr = "saveValidateInfo";
            if(type.equals(ChainDataType.PAYINFO))
                methodStr = "savePayInfo";

            //String jsonStr = "method:save,value:" + hashKey;
            String jsonStr = "method:"+methodStr+",value:" + hashKey;
            LOGGER.info("调用区块链, 存储IPFS哈希, 调用参数:{}", jsonStr);
            //System.out.println("调用接口发送数据：" + jsonStr);
            //String url = "http://192.168.99.54:20334";
            String url = applicationConfig.getSaveToContractUrl();
            RestTemplate restTemplate = new RestTemplate();
            String respMsg = restTemplate.postForObject(url, jsonStr, String.class);
            //System.out.println("接口返回数据：" + respMsg); //	sendrawtransactionResult:true,chainKey:5fd9eb4c26164d6bb9352c9cd5856f5b
            String[] msgs = respMsg.split(",");
            if (msgs.length < 3){
                LOGGER.error("调用区块链, 存储IPFS哈希失败: {}", respMsg);
                return null;
            }

            String isSuccess = resolveBlockchainresult(msgs[0]);
            if (!Boolean.valueOf(isSuccess)){
                LOGGER.error("调用区块链, 存储IPFS哈希失败: {}", respMsg);
                return null;
            }

            String validKey = resolveBlockchainresult(msgs[1]);
            if (!StringUtils.hasText(validKey)){
                LOGGER.error("调用区块链, 存储IPFS哈希失败: {}", respMsg);
                return null;
            }

            String txid = resolveBlockchainresult(msgs[2]);
            if (!StringUtils.hasText(txid)){
                LOGGER.error("调用区块链, 存储IPFS哈希失败: {}", respMsg);
                return null;
            }

            BlockChainResultEntity resultEntity = new BlockChainResultEntity();
            resultEntity.setValidKey(validKey);
            resultEntity.setTxid(txid);

            LOGGER.debug("调用区块链, 存储IPFS哈希成功，validKey={}, hash={}",validKey, jsonStr );
            return resultEntity;
        }catch(Exception e)
        {
            LOGGER.error("调用区块链, 存储IPFS哈希失败:{} ", e.getMessage());
            return null;
        }
    }

    public String resolveBlockchainresult(String s){
        if (!StringUtils.hasText(s)) return null;
        int idx = s.indexOf(':');
        if (idx == -1) return null;
        return s.substring(idx+1);
    }


    //用validkey到链上取加密的IPFS哈希
    public String getHashFromBlockChain(String validKey)
    {
        try {
            Map<String, Object> inParam = new HashMap<String, Object>();
            inParam.put("jsonrpc", "2.0");
            inParam.put("method", "invokefunction");
            inParam.put("id", "3");
            Object paramsArr[] = new Object[3];
            paramsArr[0] = applicationConfig.getVerifyScriptHash();// verifyScriptHash;//区块hash
            paramsArr[1] = "get";
            JSONObject obj = new JSONObject();
            obj.put("type","String");
            obj.put("value",validKey);
            Object subParamsArr[] = new Object[1];
            subParamsArr[0] = obj;
            paramsArr[2] = subParamsArr;
            inParam.put("params", paramsArr);
            String inJson = JSONObject.toJSONString(inParam);
            LOGGER.debug("下链时传递的参数：{}", inJson);
            LOGGER.debug("下链的URL：{}", applicationConfig.getNeoCliUrl());
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(applicationConfig.getNeoCliUrl(), inJson, String.class);

            JSONObject jsObj = JSONObject.parseObject(response);
            if(jsObj != null && jsObj.containsKey("result"))
            {
                JSONObject resObj = jsObj.getJSONObject("result");
                if(resObj!=null && resObj.containsKey("stack")) {
                    JSONArray stackArr = resObj.getJSONArray("stack");
                    if(stackArr != null && stackArr.size()>0) {
                        JSONObject stackObj = stackArr.getJSONObject(0);
                        if(stackObj!=null && stackObj.containsKey("value")) {
                            String value = stackObj.getString("value");
                            String resultStr = StringUtils.hexStringToString(value);
                            LOGGER.debug("从区块链上获取IPFS哈希：" + resultStr);
                            return resultStr;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("调用区块链,通过validKey获取ipfsHash 失败 " + e.getMessage(), e);
        }
        return "";
    }


    //在医院dapp中，支付只发生在用户和医院之间
    //医院钱包地址hardcode
    //传入身份证号，医院Id，转账数量,
    //返回余额
    public BlockChainResultEntity payToHospital(String ID, String hospId, String amount)
    {
        //用身份证号找到用户的账户信息，取出钱包和密码的base64
        UserAccountEntity userEntity = userAccountRedisService.getUserAccount(ID);
        String wallet = userEntity.getWallet();
        String password = userEntity.getWalletPwd();
        //openwallet
        if(openWallet(wallet,password).equals("1"))
        {
            LOGGER.error("payToHospital:打开用户钱包失败！");
            return null;
        }
        HospitalAccountEntity hospEntity = hospitalAccountRedisService.getHospitalAccount(hospId);
        String hospWalletAddr = hospEntity.getWalletAddr();
        //sendtoaddress
        //TODO 测试失败情况，补充error信息
        String txid = transfer(hospWalletAddr,amount);
        if("1".equals(txid))
        {
            LOGGER.error("payToHospital:付款失败！");
            return null;
        }
        //查余额
        String balance = getBalance();
        //closewallet
        closeWallet(wallet,password);

        BlockChainResultEntity blockChainResultEntity = new BlockChainResultEntity();
        blockChainResultEntity.setTxid(txid);
        blockChainResultEntity.setBalance(balance);

        return blockChainResultEntity;
    }

    //向验证合约里转3tky，作为接下来接口调用的费用
    public String payToEbao(String hospId,String amount)
    {
        //用hospId找到医院的钱包
        HospitalAccountEntity hospEntity = hospitalAccountRedisService.getHospitalAccount(hospId);
        String hospWallet = hospEntity.getWallet();
        String password = hospEntity.getWalletPwd();
        //openwallet
        if(openWallet(hospWallet,password).equals("1"))
        {
            LOGGER.error("payToEbao:打开用户钱包失败！");
            return null;
        }
        //sendtoaddress
        //TODO 测试失败情况，补充error信息
        String txid = transfer(applicationConfig.getTransferContractAddr(),amount);
        if(!StringUtils.hasText(txid))
        {
            LOGGER.error("payToEba:付款失败！");
            return null;
        }
        //closewallet
        closeWallet(hospWallet,password);
        return txid;
    }


    //传入身份证号
    public String getBalance(String ID)
    {
        //用身份证号找到用户的账户信息，取出钱包和密码的base64
        UserAccountEntity userEntity = userAccountRedisService.getUserAccount(ID);
        String wallet = userEntity.getWallet();
        String password = userEntity.getWalletPwd();
        //openwallet
        if(openWallet(wallet,password).equals("1"))
        {
            LOGGER.error("getBalance:打开用户钱包失败！");
            return null;
        }
        String balance = getBalance();
        //closewallet
        closeWallet(wallet,password);
        return balance;
    }


    //传入钱包json和密码的base64
    private String openWallet(String wallet, String pwd)
    {
        Map<String, Object> inParam = new HashMap<String, Object>();
        inParam.put("jsonrpc", "2.0");
        inParam.put("method", "openwallet");
        inParam.put("id", "1");
        Object paramsArr[] = new Object[2];
        paramsArr[0] = wallet;
        paramsArr[1] = pwd;
        inParam.put("params",paramsArr);
        try {
            String inJson = JSONObject.toJSONString(inParam);
            //String response = HttpUtils.sendPOST(applicationConfig.getNeoCliUrl(), inJson);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(applicationConfig.getNeoCliUrl(), inJson, String.class);
            Map respMap = JsonUtils.parseMap(response);
            String res = respMap.get("result").toString();
            if(res.equals("0"))
            {
                LOGGER.debug("openWallet:打开钱包成功！");
                return "0";   //打开成功
            }
        }catch(Exception e){
            LOGGER.error("openWallet:消息请求失败！",e);
        }
        return "1";
    }

    //关闭钱包
    public String closeWallet(String wallet, String pwd)
    {
        Map<String, Object> inParam = new HashMap<String, Object>();
        inParam.put("jsonrpc", "2.0");
        inParam.put("method", "closewallet");
        inParam.put("id", "1");
        Object paramsArr[] = new Object[2];
        paramsArr[0] = wallet;
        paramsArr[1] = pwd;
        inParam.put("params",paramsArr);
        try {
            String inJson = JSONObject.toJSONString(inParam);
            //String response = HttpUtils.sendPOST(applicationConfig.getNeoCliUrl(), inJson);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(applicationConfig.getNeoCliUrl(), inJson, String.class);
            Map respMap = JsonUtils.parseMap(response);
            String res = respMap.get("result").toString();
            if(res.equals("0"))
            {
                LOGGER.debug("closeWallet:关闭钱包成功！");
                return "0";   //打开成功
            }
        }catch(Exception e){
            LOGGER.error("closeWallet:消息请求失败！",e);
        }

        return "1";
    }

    //调用neo-cli的RPC服务进行转账操作
    private String transfer(String toAddr, String amount)
    {
        Map<String, Object> inParam = new HashMap<String, Object>();
        inParam.put("jsonrpc", "2.0");
        inParam.put("method", "sendtoaddress");
        inParam.put("id", "1");
        Object paramsArr[] = new Object[3];
        //paramsArr[0] = applicationConfig.getTkyAssetId();
        paramsArr[0] = applicationConfig.getNep5AssetId();
        paramsArr[1] = toAddr;
        paramsArr[2] = amount;
        inParam.put("params",paramsArr);
        try {
            String inJson = JSONObject.toJSONString(inParam);
            //String response = HttpUtils.sendPOST(applicationConfig.getNeoCliUrl(), inJson);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(applicationConfig.getNeoCliUrl(), inJson, String.class);
            JSONObject jsObj = JSONObject.parseObject(response);
            LOGGER.info("转账NEO返回：{}", response);
            if(jsObj != null && jsObj.containsKey("result")) {
                JSONObject resObj = jsObj.getJSONObject("result");
                if(resObj.containsKey("txid") && !resObj.getString("txid").equals("")) {
                    LOGGER.info("成功付款到"+toAddr+",金额："+amount);
                    return resObj.get("txid").toString();
                }
                else {
                    //余额不足的时候会有exception,需测试结果json的内容
                    return null;
                }
            }
        }catch(Exception e){
            LOGGER.error("transfer:消息请求失败！",e);
        }
        return null;
    }

    //获取钱包余额
    private String getBalance()
    {
        Map<String, Object> inParam = new HashMap<String, Object>();
        inParam.put("jsonrpc", "2.0");
        inParam.put("method", "getbalance");
        inParam.put("id", "1");
        Object paramsArr[] = new Object[1];
        //paramsArr[0] = applicationConfig.getTkyAssetId();
        paramsArr[0] = applicationConfig.getNep5AssetId();
        inParam.put("params",paramsArr);

        try {
            String inJson = JSONObject.toJSONString(inParam);
            //String response = HttpUtils.sendPOST(applicationConfig.getNeoCliUrl(), inJson);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(applicationConfig.getNeoCliUrl(), inJson, String.class);
            Map respMap = JsonUtils.parseMap(response);
            JSONObject jsObj = JSONObject.parseObject(response);
            if(jsObj != null && jsObj.containsKey("result")) {
                JSONObject resObj = jsObj.getJSONObject("result");
                if (resObj.containsKey("balance")) {
                    String balance = resObj.getString("balance");
                    LOGGER.info("账户余额:"+ balance);
                    return balance;
                }
            }
        }catch(Exception e){
            LOGGER.error("getBalance:消息请求失败！",e);
        }
        return "";
    }

}
