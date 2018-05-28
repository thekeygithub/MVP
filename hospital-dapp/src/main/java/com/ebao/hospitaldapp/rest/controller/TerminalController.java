package com.ebao.hospitaldapp.rest.controller;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.exceptions.ClientException;
import com.ebao.hospitaldapp.config.ApplicationConfig;
import com.ebao.hospitaldapp.ebao.GetUnpaidUserList;
import com.ebao.hospitaldapp.ebao.entity.PrescriptionEntity;
import com.alibaba.fastjson.JSONObject;
import com.ebao.hospitaldapp.ebao.GetPictureCheckResult;
import com.ebao.hospitaldapp.ebao.entity.UserInfoEntity;
import com.ebao.hospitaldapp.ipfs.IPFSoperations;
import com.ebao.hospitaldapp.ipfs.entity.DmiResultEntity;
import com.ebao.hospitaldapp.neo.NEOoperations;
import com.ebao.hospitaldapp.rest.base.annotations.AuthCheck;
import com.ebao.hospitaldapp.rest.base.enums.ChainDataType;
import com.ebao.hospitaldapp.rest.base.enums.Exceptions;
import com.ebao.hospitaldapp.rest.base.result.JsonRESTResult;
import com.ebao.hospitaldapp.rest.base.utils.*;
import com.ebao.hospitaldapp.rest.entity.BlockChainResultEntity;
import com.ebao.hospitaldapp.rest.entity.TokenEntity;
import com.ebao.hospitaldapp.rest.entity.mapper.TreatmentInfoMapper;
import com.ebao.hospitaldapp.rest.entity.mapper.TreatmentMapper;
import com.ebao.hospitaldapp.rest.entity.mapper.UserInfoMapper;
import com.ebao.hospitaldapp.rest.entity.request.*;
import com.ebao.hospitaldapp.rest.entity.response.*;
import com.ebao.hospitaldapp.rest.service.*;
import com.ebao.hospitaldapp.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/terminal/api")
public class TerminalController {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private RedisKeyService redisKeyService;
    @Autowired
    private UserInfoRedisService userInfoRedisService;
    @Autowired
    private PrescriptionsRedisService prescriptionsRedisService;
    @Autowired
    private IPFSoperations ipfSoperations;
    @Autowired
    private StringRedisTemplate redis;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private TreatmentMapper treatmentMapper;
    @Autowired
    private TreatmentInfoMapper treatmentInfoMapper;
    @Autowired
    private NEOoperations neooperations;
    @Autowired
    private UserAccountRedisService userAccountRedisService;
    @Autowired
    private HospitalAccountRedisService hospAccountRedisService;
    @Autowired
    private SerializedStringService serializedStringService;
    @Autowired
    private GetUnpaidUserList getUnpaidUserList;
    @Autowired
    private ApplicationConfig applicationConfig;
    @Autowired
    PaymentService paymentService;
    @Autowired
    HospitalRedisService hospitalRedisService;
    @Autowired
    SMSService smsService;

    final static Logger LOGGER = LoggerFactory.getLogger(TerminalController.class);

    @RequestMapping(value =  "/initialization", method = RequestMethod.POST)
    public String initializeTerminal(@Validated Terminal terminal) throws IOException {
        TokenEntity tokenEntity = tokenService.createToken(terminal.getDeviceId());
        if (tokenEntity == null) return new JsonRESTResult(Exceptions.GetTokenFailed).encode();
        return new JsonRESTResult(tokenEntity).encode();
    }

    @RequestMapping(value =  "/sendCode", method = RequestMethod.POST)
    public String sendSMS(@Validated SMS sms) throws IOException {
        if (!smsService.checkChineseMobile(sms.getMobile())){
            LOGGER.error("非法号码：{}", sms.getMobile());
            return new JsonRESTResult(Exceptions.InvalidNumber).encode();
        }

        try {
            smsService.sendLoginSms(sms.getMobile());
        } catch (ClientException e) {
            LOGGER.error("发送短信失败，号码信息={} 错误信息={}", sms, e.getMessage());
            return new JsonRESTResult(Exceptions.SMSSendError).encode();
        }

        return new JsonRESTResult().encode();
    }

    @RequestMapping(value =  "/login", method = RequestMethod.POST)
    public String mobileLogin(@Validated Login login) throws IOException {
        Map<String, String> superNumbers = applicationConfig.getSuperMobileNumbers();
        if (!CollectionUtils.isEmpty(superNumbers) && superNumbers.containsKey(login.getMobile())){
            TokenEntity tokenEntity = tokenService.createToken(login.getMobile());
            if (tokenEntity == null) return new JsonRESTResult(Exceptions.GetTokenFailed).encode();

            LOGGER.info("超级用户{}登录成功", login.getMobile());
            return new JsonRESTResult(tokenEntity).encode();
        }

        String vCode = smsService.getLoginSMS(login.getMobile());
        if (!StringUtils.hasText(vCode)) return new JsonRESTResult(Exceptions.SMSNotExist).encode();
        if (!vCode.equals(login.getCode())) return new JsonRESTResult(Exceptions.SMSNotMatch).encode();

        TokenEntity tokenEntity = tokenService.createToken(login.getMobile());
        if (tokenEntity == null) return new JsonRESTResult(Exceptions.GetTokenFailed).encode();

        LOGGER.info("用户{}登录成功", login.getMobile());
        return new JsonRESTResult(tokenEntity).encode();
    }

    @AuthCheck
    @RequestMapping(value =  "/getTreatmentInfo", method = RequestMethod.POST)
    public String getTreatmentInfo(String mobile) throws IOException {
        if (!StringUtils.hasText(mobile)){
            LOGGER.error("无效手机号码");
            return new JsonRESTResult(Exceptions.InvalidNumber).encode();
        }

        UserAccountEntity userAccountEntity;
        Map<String, String> superNumbers = applicationConfig.getSuperMobileNumbers();
        if (!CollectionUtils.isEmpty(superNumbers) && superNumbers.containsKey(mobile)){
            userAccountEntity = userAccountRedisService.getUserAccount(superNumbers.get(mobile));
        }else {
            userAccountEntity = userAccountRedisService.getUserAccountByMobile(mobile);
        }

        if (userAccountEntity == null || userAccountEntity.getID() == null) {
            LOGGER.info("用户{}未发现患者信息", mobile);
            return new JsonRESTResult(Exceptions.NoTreatmentInfo).encode();
        }

        List<UserInfoEntity> userInfoResultList = new ArrayList<>();
        List<String> hospitalIds = hospitalRedisService.getHospitalIds();
        if (!CollectionUtils.isEmpty(hospitalIds)){
            for (String hospId : hospitalIds){
                List<UserInfoEntity> userList = userInfoRedisService.getUserInfoEntityList(hospId);
                if (CollectionUtils.isEmpty(userList)) continue;
                for (UserInfoEntity userInfoEntity : userList){
                    if (userInfoEntity != null && userAccountEntity.getID().equals(userInfoEntity.getId_no())){
                        userInfoResultList.add(userInfoEntity);
                    }
                }
            }
        }

        if (CollectionUtils.isEmpty(userInfoResultList)){
            LOGGER.info("用户{}未发待缴费信息", mobile);
            return new JsonRESTResult(Exceptions.NoTreatmentInfo).encode();
        }

        List<TreatmentInfoModel> treatmentInfoModels = treatmentInfoMapper.map(userInfoResultList);
        return new JsonRESTResult(treatmentInfoModels).encode();
    }

    @AuthCheck
    @RequestMapping(value =  "/getUserInfoByMobile", method = RequestMethod.POST)
    public String getUserInfoByMobile(@Validated Terminal terminal) {
        List<UserInfoEntity> userList = userInfoRedisService.getUserInfoEntityList(terminal.getHospId());
        List<PatientModel> patientModelList = userInfoMapper.map(userList);
        return new JsonRESTResult(patientModelList).encode();
    }

    @AuthCheck
    @RequestMapping(value =  "/getUserInfo", method = RequestMethod.POST)
    public String getUserInfo(@Validated Terminal terminal) {
        List<UserInfoEntity> userList = userInfoRedisService.getUserInfoEntityList(terminal.getHospId());
        List<PatientModel> patientModelList = userInfoMapper.map(userList);
        return new JsonRESTResult(patientModelList).encode();
    }

    @AuthCheck
    @RequestMapping(value =  "/getUserInfoDirectly", method = RequestMethod.POST)
    public String getUserInfoDirectly(@Validated Terminal terminal) {
        int result = getUnpaidUserList.retrieveFromEbaoAPI(terminal.getHospId(), "2018-04-03");
        return result == 0 ? getUserInfo(terminal) : new JsonRESTResult(Exceptions.NotFound).encode();
    }

    @AuthCheck
    @RequestMapping(value =  "/verifyUser2", method = RequestMethod.POST)
    public String verifyUser2(@Validated VerifyInfo verifyInfo) throws IOException, InterruptedException {
        String prescriptionKey = redisKeyService.getPrescriptionKey(verifyInfo);
        PrescriptionEntity prescriptionEntity = prescriptionsRedisService.getPresEntity(prescriptionKey);

        String s = "{ \"resultInfo\": { \"validID\": \"aaaaaa\", \"pointsBills\": [ { \"presgroupid\":\"180403000012\", \"bigpay\": \"0\", \"cashpay\": \"102\", \"cfstand\": \"0\", \"gwybz\": \"0\", \"perpay\": \"0\", \"propaybig\": \"0\", \"propayplan\": \"0\", \"qybcpay\": \"0\", \"resultCode\": \"0\", \"resultDesc\": \"成功\", \"superbigpay\": \"0\", \"tcpay\": \"0\", \"ylsum\": \"102\", \"zfproject\": \"102\", \"zhpay\": \"0\" }, { \"presgroupid\":\"180403000013\", \"bigpay\": \"0\", \"cashpay\": \"128.8\", \"cfstand\": \"0\", \"gwybz\": \"0\", \"perpay\": \"0\", \"propaybig\": \"0\", \"propayplan\": \"0\", \"qybcpay\": \"0\", \"resultCode\": \"0\", \"resultDesc\": \"成功\", \"superbigpay\": \"0\", \"tcpay\": \"0\", \"ylsum\": \"128.8\", \"zfproject\": \"128.8\", \"zhpay\": \"0\" } ], \"interfaceHisList\": [ { \"exeTime\": \"2018-04-19 17:55:00\", \"hisId\": \"474e4f82-029c-45af-9473-2630db0417b7\", \"idNo\": \"410205197204080024\", \"interfaceName\": \"SBKJBXX\", \"interfaceRestlt\": \"{\\\"code\\\":\\\"10003\\\",\\\"message\\\":\\\"查询社保基本信息出错\\\"}\", \"type\": \"01\" } ], \"resTotal\": \"102333333333\" } } ";
        JSONObject jsonObject = JSON.parseObject(s);
        String content = jsonObject.getString("resultInfo");
        DmiResultEntity entity = JSON.parseObject(content, DmiResultEntity.class);

        Thread.sleep(20000);

        TreatmentModel treatmentModel = treatmentMapper.map(prescriptionEntity, entity);
        return new JsonRESTResult(treatmentModel).encode();
    }

    @AuthCheck
    @RequestMapping(value =  "/verifyUser", method = RequestMethod.POST)
    public String verifyUser(@Validated VerifyInfo verifyInfo) throws IOException, InterruptedException {
        String userKey = redisKeyService.getUserInfoKey(verifyInfo);
        String prescriptionKey = redisKeyService.getPrescriptionKey(verifyInfo);
        String dmiResultKey = redisKeyService.getDMIResultKey(verifyInfo);
        String picKey = redisKeyService.getUserImageKey(verifyInfo.getHospId(), verifyInfo.getIdNumber());
        HospitalAccountEntity hospitalAccountEntity = hospAccountRedisService.getHospitalAccount(verifyInfo.getHospId());
        String userInfoString = userInfoRedisService.getUserInfoString(userKey);
        PrescriptionEntity prescriptionEntity = prescriptionsRedisService.getPresEntity(prescriptionKey);

        if (hospitalAccountEntity == null || !StringUtils.hasText(userInfoString) || prescriptionEntity == null){
            LOGGER.error("医院或用户信息有误：hospital={}, user={}, prescription={}", hospitalAccountEntity, userInfoString, prescriptionEntity);
            return new JsonRESTResult(Exceptions.VerifyFail, "验证失败").encode();
        }

        //put userInfo and prescriptions into IPFS
        String hash = ipfSoperations.addValidationInfo(userInfoString, JSON.toJSONString(prescriptionEntity.mx), verifyInfo.getImage());
        if (!StringUtils.hasText(hash)){
            LOGGER.error("验证信息存入IPFS失败: userInfo=%s, prescription=%s", userInfoString, JSON.toJSONString(prescriptionEntity.mx));
            return new JsonRESTResult(Exceptions.VerifyFail, "验证失败").encode();
        }else {
            LOGGER.info("IPFS存储成功，返回的hash：{}", hash);
        }

        //put IPFS hash into NEO
        String encryptedHash = RSAUtils.encryption(applicationConfig.getEbaoPublicKey(), hash);
        BlockChainResultEntity blockChainResultEntity = neooperations.saveToBlockchain(encryptedHash, ChainDataType.VALIDINFO);
        String validTxTime = LocalDateTime.now().format(DateUtils.YEAR_MONTH_DAY_24H_M_S);
        if (blockChainResultEntity == null){
            LOGGER.error("调用区块链, 存储IPFS哈希失败：加密后的串{}", encryptedHash);
            return new JsonRESTResult(Exceptions.VerifyFail, "验证失败").encode();
        }else {
            LOGGER.info("调用区块链, 存储IPFS哈希成功，validKey：{}", blockChainResultEntity);
        }

        //医院向ebao付3tky作为验证费用
        String fee = applicationConfig.getVerificationFee();
        String transferSerial = neooperations.payToEbao(verifyInfo.getHospId(), fee);
        String transferTime = LocalDateTime.now().format(DateUtils.YEAR_MONTH_DAY_24H_M_S);
        if (!StringUtils.hasText(transferSerial)){
            LOGGER.error("钱包转账失败：hospId={}, fee={}", verifyInfo.getHospId(), fee);
            return new JsonRESTResult(Exceptions.VerifyFail, "验证失败").encode();
        }else {
            LOGGER.info("转账成功，hospId:{}, fee:{}, transferSerial：{}", verifyInfo.getHospId(), fee, transferSerial);
        }

        boolean isIPFSModel = applicationConfig.isIPFSMode();
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("ID", verifyInfo.getIdNumber());
        param.put("hospitalId", verifyInfo.getHospId());
        param.put("validKey", blockChainResultEntity.getValidKey());
        param.put("validTxId", blockChainResultEntity.getTxid());
        param.put("validTxTime", validTxTime);
        param.put("transferTime", transferTime);
        param.put("transferSerial", transferSerial);
        param.put("publicKey", hospitalAccountEntity.getPublicKey());
        param.put("senderName", hospitalAccountEntity.getHospName());
        param.put("senderAddr", hospitalAccountEntity.getWalletAddr());
        param.put("type", "01");
        param.put("fee", fee);
        param.put("jzlsno", verifyInfo.getTreatmentId());
        param.put("ipfsHash", isIPFSModel ? hash : "");

        LOGGER.info("通知医保，参数：{}", param);
        String url = isIPFSModel ? applicationConfig.getNotifyEbaoVerifyUrl2() : applicationConfig.getNotifyEbaoVerifyUrl();
        String notifyResult = HttpUtils.sendPOST(url, param);
        LOGGER.info("通知易保的结果是：" + notifyResult);

        //todo http超时处理

        int i = 0;
        String verifyResult = null;
        while (i < 1200){
            verifyResult = redis.opsForValue().get(dmiResultKey);
            if (StringUtils.hasText(verifyResult)) break;
            i++;
            Thread.sleep(1000);
        }

        if (verifyResult == null){
            LOGGER.error("等待易保回复超时：{}", verifyResult);
            LOGGER.info("redis清理缓存: {}", redis.delete(dmiResultKey));
            return new JsonRESTResult(Exceptions.VerifyFail, "验证失败").encode();
        }else if ("-1".equals(verifyResult)){
            LOGGER.error("易保回复异常：{}", verifyResult);
            LOGGER.info("redis清理缓存: {}", redis.delete(dmiResultKey));
            return new JsonRESTResult(Exceptions.VerifyFail.getValue(), "服务器繁忙").encode();
        }else {
            LOGGER.debug("验证结果： {}", verifyResult);
            LOGGER.info("redis清理缓存: {}", redis.delete(dmiResultKey));
        }

        //取出验证结果
        DmiResultEntity resultEntity = JsonUtils.fromJson(verifyResult,DmiResultEntity.class);
        if(resultEntity == null){
            LOGGER.error("从Redis中取验证结果异常：{}", verifyResult);
            return new JsonRESTResult(Exceptions.VerifyFail, "验证失败").encode();
        }
        //取出验证代码
        String resTotal = resultEntity.getResTotal();
        if (!serializedStringService.isSuccess(resTotal)){
            String result = serializedStringService.getSingleResult(resTotal);
            LOGGER.error("多维验证失败：{}， 验证返回代码:{}", result, resTotal);
            return new JsonRESTResult(Exceptions.VerifyFail.getValue(), result).encode();
        }else {
            LOGGER.info("多维验证成功，验证返回代码：{}", resTotal);
        }

        redis.boundValueOps(picKey).set(verifyInfo.getImage());
        TreatmentModel treatmentModel = treatmentMapper.map(prescriptionEntity, resultEntity);
        return new JsonRESTResult(treatmentModel).encode();
    }

    @AuthCheck
    @RequestMapping(value =  "/exchangeToTKY", method = RequestMethod.POST)
    public String exchangeToTKY(@Validated ExchangeInfo exchangeInfo) {
        TKYConvertModel tkyConvertModel = new TKYConvertModel();
        String rate = applicationConfig.getTkyExchangeRate();
        try {
            BigDecimal amount = new BigDecimal(exchangeInfo.getAmount());
            BigDecimal exchangeRate = new BigDecimal(rate);
            tkyConvertModel.setTKY(amount.multiply(exchangeRate));
        }catch (Exception e){
            LOGGER.error("转化TKY出错：rate={}, amount={}", rate, exchangeInfo.getAmount());
            return new JsonRESTResult(Exceptions.TKYCalculateError).encode();
        }
        return new JsonRESTResult(tkyConvertModel).encode();
    }

    @AuthCheck
    @RequestMapping(value =  "/payByPassword", method = RequestMethod.POST)
    public String payByPassword(@Validated PayByPasswordInfo payByPasswordInfo) {
        UserAccountEntity userInfo = userAccountRedisService.getUserAccount(payByPasswordInfo.getIdNumber());
        if (userInfo == null) {
            LOGGER.error("用户信息为空");
            return new JsonRESTResult(Exceptions.NotFound, "用户").encode();
        }

        if(!userInfo.getPayPwd().equals(payByPasswordInfo.getPassword())) {
            LOGGER.info("密码不正确：{}", userInfo);
            return new JsonRESTResult(Exceptions.PasswordError).encode();
        }else {
            LOGGER.info("密码校验成功");
        }

        JsonRESTResult result = paymentService.payToHospital(payByPasswordInfo);
        if (!result.isSuccess()) return result.encode();
        if (result.getReturnObj() == null) return new JsonRESTResult(Exceptions.ObjectNotFound).encode();

        MessageModel messageModel = new MessageModel();
        messageModel.setBalance(result.getReturnObj().toString());
        return new JsonRESTResult(messageModel).encode();
    }

    @AuthCheck
    @RequestMapping(value =  "/payByPicture", method = RequestMethod.POST)
    public String payByPicture(@Validated PayByImageInfo payByImageInfo) {
        String paymentImage = payByImageInfo.getImage();
        //从redis里找到存储的照片后与发送过来的照片进行对比，完成验证
        String picKey = redisKeyService.getUserImageKey(payByImageInfo.getHospId(), payByImageInfo.getIdNumber());
        String standardPhoto = redis.opsForValue().get(picKey);
        boolean isFaceMode = applicationConfig.isFaceMode();

        if (isFaceMode) {
            GetPictureCheckResult getPictureCheckResult = new GetPictureCheckResult();
            String picResponse = getPictureCheckResult.retrieveFromPicAPI(paymentImage, standardPhoto);// 调用人脸识别接口
            if (!StringUtils.hasText(picResponse)) {
                LOGGER.error("图片比对未通过, hospId:{}, treatmentId:{}, prescriptionList: {}", payByImageInfo.getHospId(), payByImageInfo.getTreatmentId(), payByImageInfo.getPrescriptionList());
                return new JsonRESTResult(Exceptions.VerifyFail, "验证失败").encode();
            } else {
                LOGGER.info("图片比对成功");
            }
        }else {
            LOGGER.info("关闭脸部识别功能");
        }

        JsonRESTResult result = paymentService.payToHospital(payByImageInfo);
        if (!result.isSuccess()) return result.encode();
        if (result.getReturnObj() == null) return new JsonRESTResult(Exceptions.ObjectNotFound).encode();

        MessageModel messageModel = new MessageModel();
        messageModel.setBalance(result.getReturnObj().toString());
        return new JsonRESTResult(messageModel).encode();
    }

    @AuthCheck
    @RequestMapping(value =  "/checkBalance", method = RequestMethod.POST)
    public String checkBalance(@Validated User user) {
        TKYConvertModel tkyConvertModel = new TKYConvertModel();
        //通过身份证信息调用钱包接口获得余额信息
        String balance = neooperations.getBalance(user.getIdNumber());
        try {
            tkyConvertModel.setTKY(new BigDecimal(balance));
        }catch (Exception e){
            LOGGER.error("转化TKY出错：ID={}, balance={}", user.getIdNumber(), balance);
            return new JsonRESTResult(Exceptions.TKYCalculateError).encode();
        }
        return new JsonRESTResult(tkyConvertModel).encode();
    }

    @RequestMapping(value =  "/test", method = RequestMethod.POST)
    public String test(String image) {
        LOGGER.info(image);
        CommonUtils.generateImage(image, "F:\\apache-tomcat-9.0.7\\logs\\hospital\\test.jpg");
        return new JsonRESTResult().encode();
    }

    public static void main(String[] args) {
        StringBuffer  a = new StringBuffer("a");
        StringBuffer b = a;
        a = a .append("1");
        System.out.println(a);
        System.out.println(b);
        System.out.println(a == b);;
    }
}
