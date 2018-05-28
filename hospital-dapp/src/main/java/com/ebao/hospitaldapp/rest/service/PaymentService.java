package com.ebao.hospitaldapp.rest.service;

import com.ebao.hospitaldapp.config.ApplicationConfig;
import com.ebao.hospitaldapp.ipfs.IPFSoperations;
import com.ebao.hospitaldapp.neo.NEOoperations;
import com.ebao.hospitaldapp.rest.base.enums.Exceptions;
import com.ebao.hospitaldapp.rest.base.enums.ChainDataType;
import com.ebao.hospitaldapp.rest.base.result.JsonRESTResult;
import com.ebao.hospitaldapp.rest.base.utils.HospitalAccountEntity;
import com.ebao.hospitaldapp.rest.base.utils.HospitalAccountRedisService;
import com.ebao.hospitaldapp.rest.base.utils.UserAccountEntity;
import com.ebao.hospitaldapp.rest.base.utils.UserAccountRedisService;
import com.ebao.hospitaldapp.rest.entity.BlockChainResultEntity;
import com.ebao.hospitaldapp.rest.entity.request.PaymentBase;
import com.ebao.hospitaldapp.utils.DateUtils;
import com.ebao.hospitaldapp.utils.HttpUtils;
import com.ebao.hospitaldapp.utils.RSAUtils;
import com.ebao.hospitaldapp.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class PaymentService {

    final static Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private NEOoperations neooperations;
    @Autowired
    private ApplicationConfig applicationConfig;
    @Autowired
    private IPFSoperations ipfSoperations;
    @Autowired
    private RedisKeyService redisKeyService;
    @Autowired
    private StringRedisTemplate redis;
    @Autowired
    private UserAccountRedisService userAccountRedisService;
    @Autowired
    private HospitalAccountRedisService hospitalAccountRedisService;
    @Autowired
    private RestTemplate restTemplate;

    public JsonRESTResult payToHospital(PaymentBase paymentBase){
        String serialNoKey = redisKeyService.getSerialNoKey(paymentBase.getHospId(), paymentBase.getTreatmentId());
        String serialNo;
        if (!redis.hasKey(serialNoKey)){
            LOGGER.error("获取流水号失败 hospId={} jzlsno={}", paymentBase.getHospId(), paymentBase.getTreatmentId());
            return new JsonRESTResult(Exceptions.VerifyFail, "支付失败");
        }else {
            serialNo = redis.opsForValue().get(serialNoKey);
        }

        UserAccountEntity userEntity = userAccountRedisService.getUserAccount(paymentBase.getIdNumber());
        HospitalAccountEntity hospEntity = hospitalAccountRedisService.getHospitalAccount(paymentBase.getHospId());
        BlockChainResultEntity payEntity = neooperations.payToHospital(paymentBase.getIdNumber(), paymentBase.getHospId(), paymentBase.getTKY());
        String transferTime = LocalDateTime.now().format(DateUtils.YEAR_MONTH_DAY_24H_M_S);
        if (payEntity == null || !StringUtils.hasText(payEntity.getBalance())){
            LOGGER.error("支付失败，ID:{}, hospId:{}, TKY: {}", paymentBase.getIdNumber(), paymentBase.getHospId(), paymentBase.getTKY());
            return new JsonRESTResult(Exceptions.VerifyFail, "支付失败");
        }else{
            LOGGER.info("支付成功，余额：{}", payEntity.getBalance());
        }

        //通知易保dapp区块链交易信息：用户支付处方结算费用给医院
        String url = applicationConfig.getNotifyEbaoSaveTxUrl();
        String result;
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("txId", payEntity.getTxid());
        param.put("senderName", userEntity.getName());
        param.put("receiverName", hospEntity.getHospName());
        param.put("senderAddr", userEntity.getWalletAddr());
        param.put("receiverAddr", hospEntity.getWalletAddr());
        param.put("fee", paymentBase.getTKY());
        param.put("time", transferTime);
        param.put("validKey", payEntity.getValidKey());
        param.put("serialNo", serialNo);
        param.put("type", "处方结算");
        LOGGER.info("用户支付医院通知ebao参数：{}", param);
        result = HttpUtils.sendPOST(url, param);
        LOGGER.info("用户支付医院通知ebao结果：{}", result);


        //通知易保dapp区块链交易信息：医院dapp将用户支付详情存入智能合约
        String hash = ipfSoperations.addPayInfo(paymentBase.getHospId(), paymentBase.getTreatmentId(), paymentBase.getTransactionNO(), paymentBase.getPrescriptionList());
        if (!StringUtils.hasText(hash)){
            LOGGER.error("支付信息存入IPFS失败, hospId:{}, treatmentId:{}, prescriptionList: {}", paymentBase.getHospId(), paymentBase.getTreatmentId(), paymentBase.getPrescriptionList());
            return new JsonRESTResult(Exceptions.VerifyFail, "支付失败");
        }else{
            LOGGER.info("IPFS存储成功，返回的hash：{}", hash);
        }

        //put IPFS hash into NEO
        String encryptedHash = RSAUtils.encryption(applicationConfig.getEbaoPublicKey(), hash);
        BlockChainResultEntity blockChainResultEntity = neooperations.saveToBlockchain(encryptedHash, ChainDataType.PAYINFO);
        transferTime = LocalDateTime.now().format(DateUtils.YEAR_MONTH_DAY_24H_M_S);
        if (blockChainResultEntity == null){
            LOGGER.error("调用区块链, 存储IPFS哈希失败：加密后的串{}", encryptedHash);
            return new JsonRESTResult(Exceptions.VerifyFail, "支付失败");
        }else {
            LOGGER.info("调用区块链, 存储IPFS哈希成功，validKey：{}", blockChainResultEntity);
        }

        param.clear();
        param.put("txId", blockChainResultEntity.getTxid());
        param.put("senderName", hospEntity.getHospName());
        param.put("receiverName", "验证合约");
        param.put("senderAddr", hospEntity.getWalletAddr());
        param.put("receiverAddr", "");
        param.put("fee", "0");
        param.put("time", transferTime);
        param.put("validKey", blockChainResultEntity.getValidKey());
        param.put("serialNo", serialNo);
        param.put("type", "结算存证");
        LOGGER.info("结算上链通知ebao参数：{}", param);
        result = HttpUtils.sendPOST(url, param);
        LOGGER.info("结算上链通知ebao结果:{}", result);

        //医院dapp向易保dap通知支付详情的IPFS哈希
        boolean isIPFSModel = applicationConfig.isIPFSMode();
        param.clear();
        param.put("validKey", blockChainResultEntity.getValidKey());
        param.put("ipfsHash", isIPFSModel ? hash : "");

        LOGGER.info("通知医保，参数：{}", param);
        try {
            result = restTemplate.postForObject(applicationConfig.getNotifyEbaoPayUrl(), null, String.class, param);
        }catch (Exception e){
            LOGGER.error("通知医保超时：{}", e.getMessage());
        }
        LOGGER.info("通知易保的结果是：{}" + result);

        LOGGER.info("流水号：{} 清除流水号缓存：{}", serialNo, redis.delete(serialNoKey));
        return new JsonRESTResult(payEntity.getBalance());
    }
}
