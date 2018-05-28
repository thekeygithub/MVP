package com.ebao.hospitaldapp.rest.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ebao.hospitaldapp.data.RedisClient;
import com.ebao.hospitaldapp.ebao.entity.UserInfoEntity;
import com.ebao.hospitaldapp.ipfs.IPFSoperations;
import com.ebao.hospitaldapp.ipfs.entity.DmiResultEntity;
import com.ebao.hospitaldapp.neo.NEOoperations;
import com.ebao.hospitaldapp.rest.base.enums.Exceptions;
import com.ebao.hospitaldapp.rest.base.result.JsonRESTResult;
import com.ebao.hospitaldapp.rest.base.utils.HospitalAccountEntity;
import com.ebao.hospitaldapp.rest.base.utils.HospitalAccountRedisService;
import com.ebao.hospitaldapp.rest.controller.EbaoDmiResultController;
import com.ebao.hospitaldapp.rest.entity.EbaoDmiResultEntity;
import com.ebao.hospitaldapp.rest.service.RedisKeyService;
import com.ebao.hospitaldapp.utils.JsonUtils;
import com.ebao.hospitaldapp.utils.RSAUtils;
import com.ebao.hospitaldapp.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EbaoDmiResultListener {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPFSoperations ipfsOps;
    @Autowired
    private NEOoperations neOoperations;
    @Autowired
    private HospitalAccountRedisService hospitalAccountRedisService;
    @Autowired
    private RedisKeyService redisKeyService;
    @Autowired
    private ApplicationContext applicationContext;

    @Async
    @EventListener
    public void process(EbaoDmiResultEvent ebaoDmiResultEvent) throws Exception{
        if (ebaoDmiResultEvent == null){
            logger.error("医院验证事件异常：事件为空");
            return;
        }

        EbaoDmiResultEntity ebaoDmiResultEntity = ebaoDmiResultEvent.getEbaoDmiResultEntity();
        if (ebaoDmiResultEntity == null){
            logger.error("医院验证事件异常：{}", ebaoDmiResultEvent);
            return;
        }

        if (ebaoDmiResultEvent.isIPFSMode()) {
            handleEbaoDmiResult2(ebaoDmiResultEntity);
        } else {
            handleEbaoDmiResult(ebaoDmiResultEntity);
        }
    }

    public void handleEbaoDmiResult(EbaoDmiResultEntity ebaoDmiResultEntity){
        String blockKey = ebaoDmiResultEntity.getBlockKey();
        String hospId = ebaoDmiResultEntity.getHospitalId();
        String jzlsno = ebaoDmiResultEntity.getJzlsno();
        String serialNo = ebaoDmiResultEntity.getSerialNo();

        if (!StringUtils.hasText(hospId) || !StringUtils.hasText(jzlsno)){
            logger.error("易保dapp验证参数异常: {}", ebaoDmiResultEntity);
            return;
        }
        String ipfsKey = redisKeyService.getUserIPFSfileKey(hospId,jzlsno);
        String key = redisKeyService.getDMIResultKey(hospId,jzlsno);
        String serialNoKey = redisKeyService.getSerialNoKey(hospId, jzlsno);

        if(!StringUtils.hasText(blockKey) || !StringUtils.hasText(serialNo)){
            logger.error("易保dapp验证参数异常:{}", ebaoDmiResultEntity);
            RedisClient.set(key,"-1");
            RedisClient.set(ipfsKey,"-1");
            return;
        }

        try {
            String encryptedHash = neOoperations.getHashFromBlockChain(blockKey);
            int i = 1;
            while (i < 180){
                if (StringUtils.hasText(encryptedHash)){
                    logger.info("第{}秒成功取到链上信息！", i);
                    break;
                }else {
                    logger.info("第{}秒未取到链上信息", i);
                    i++;
                    Thread.sleep(1000);
                    encryptedHash = neOoperations.getHashFromBlockChain(blockKey);
                }
            }

            if(!StringUtils.hasText(encryptedHash)){
                logger.error("易保dapp验证结果通知--hash为空:{}", encryptedHash);
                RedisClient.set(key,"-1");
                RedisClient.set(ipfsKey,"-1");
                return;
            }else {
                logger.info("从区块链上获取IPFS哈希（加密）：{}", encryptedHash);
            }

            HospitalAccountEntity entity = hospitalAccountRedisService.getHospitalAccount(hospId);
            if (entity == null){
                logger.error("根据hospId获取到医院失败:{}", hospId);
                RedisClient.set(key,"-1");
                RedisClient.set(ipfsKey,"-1");
                return;
            }

            String privateKey = entity.getPrivateKey();
            String decryptedStr = RSAUtils.decryption(privateKey, encryptedHash);
            //{“newHash”：“”，“resTotal”：“”}
            JSONObject resObj = JSON.parseObject(decryptedStr);
            String newHash = resObj.getString("newHash");

            //去IPFS取文件
            if (!StringUtils.hasText(newHash)) {
                logger.error("易保dapp验证结果通知--IPFS哈希解密失败！{}", decryptedStr);
                RedisClient.set(key,"-1");
                RedisClient.set(ipfsKey,"-1");
                return;
            }else {
                logger.info("从区块链上获取IPFS哈希：{}", newHash);
            }

            DmiResultEntity resultEntity = ipfsOps.getDmiResultInfo(newHash);
            UserInfoEntity userInfoEntity = ipfsOps.getUserInfo(newHash);
            if(userInfoEntity == null || resultEntity == null){
                logger.error("易保dapp验证结果通知--获取的结果为空！newHash={}", newHash);
                RedisClient.set(key,"-1");
                RedisClient.set(ipfsKey,"-1");
                return;
            }else{
                logger.info("易保dapp验证结果存入Redis："+key);
                logger.info("用户IPFS文件哈希存入Redis："+ipfsKey);
            }

            //将DMI结果放入redis
            RedisClient.set(key, JsonUtils.toJson(resultEntity));
            //将IPFS文件哈希暂存在redis，支付完成后需要存入详情
            RedisClient.set(ipfsKey,newHash);
            //将流水号放到redis中，以便支付的时候使用
            RedisClient.set(serialNoKey, serialNo);
        }
        catch(Exception e)
        {
            logger.error("易保dapp验证结果通知--未知异常: {}",e);
            RedisClient.set(key,"-1");
            RedisClient.set(ipfsKey,"-1");
        }
    }

    public void handleEbaoDmiResult2(EbaoDmiResultEntity ebaoDmiResultEntity) throws IOException {
        String hospId = ebaoDmiResultEntity.getHospitalId();
        String jzlsno = ebaoDmiResultEntity.getJzlsno();
        String ipfsHash = ebaoDmiResultEntity.getNewHash();
        String serialNo = ebaoDmiResultEntity.getSerialNo();

        if (!StringUtils.hasText(hospId) || !StringUtils.hasText(jzlsno)){
            logger.error("易保dapp验证参数异常: {}", ebaoDmiResultEntity);
            return;
        }
        String ipfsKey = redisKeyService.getUserIPFSfileKey(hospId,jzlsno);
        String key = redisKeyService.getDMIResultKey(hospId,jzlsno);
        String serialNoKey = redisKeyService.getSerialNoKey(hospId, jzlsno);

        if(!StringUtils.hasText(serialNo) || !StringUtils.hasText(ipfsHash)){
            logger.error("易保dapp验证参数异常:{}", ebaoDmiResultEntity);
            RedisClient.set(key,"-1");
            RedisClient.set(ipfsKey,"-1");
            return;
        }

        DmiResultEntity resultEntity = ipfsOps.getDmiResultInfo(ipfsHash);
        if(resultEntity == null){
            logger.error("易保dapp验证结果通知--获取的结果为空！");
            RedisClient.set(key,"-1");
            RedisClient.set(ipfsKey,"-1");
            return;
        }else{
            logger.info("易保dapp验证结果存入Redis："+key);
            logger.info("用户IPFS文件哈希存入Redis："+ipfsKey);
        }

        //将DMI结果放入redis
        RedisClient.set(key,JsonUtils.toJson(resultEntity));
        //将IPFS文件哈希暂存在redis，支付完成后需要存入详情
        RedisClient.set(ipfsKey,ipfsHash);
        //将流水号放到redis中，以便支付的时候使用
        RedisClient.set(serialNoKey, serialNo);
    }
}
