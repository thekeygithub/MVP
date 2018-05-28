package com.twimi.apiservice.Service;

import com.google.gson.*;
import com.twimi.apiservice.Model.*;
import com.twimi.apiservice.Util.EncryptionUtil;
import com.twimi.apiservice.Util.IPFSConnction;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class UserDataService {

    private String getFileContent(String hash)
    {
        try {
            Multihash userRootHash = Multihash.fromBase58(hash);
            //由文件hash读取文件，把结果返回给一个string 字符串 userRoot
            String userRoot = new String(IPFSConnction.getIpfs().cat(userRootHash));
            String decrpted = EncryptionUtil.decryptDES(userRoot,key);
            return decrpted;
        }catch (Exception e) {
            // rsCode 1  代表错误  rsCode 0 代表正确
            System.out.println(e.toString());
            return null;
        }
    }

    //获取用户的所有内容
    public ReturnData getAllDataByHash(String hash) {
//        Multihash userRootHash = Multihash.fromBase58(hash);
        ReturnData returnData = new ReturnData();
        Gson gson = new Gson();
        try {
            //由文件hash读取文件，把结果返回给一个string 字符串 userRoot
//            String userRoot = new String(IPFSConnction.getIpfs().cat(userRootHash));
           String userRoot = getFileContent(hash);

           //System.out.println("获取到上传信息：" + userRoot);
            //UploadData uploadData = gson.fromJson(userRoot, UploadData.class);
            //returnData.setData(uploadData);
            returnData.setRsJsonString(userRoot);
            returnData.setRsCode("0");
        } catch (Exception e) {
            // rsCode 1  代表错误  rsCode 0 代表正确
            System.out.println(e.toString());
            returnData.setRsCode("1");
        }
        return returnData;
    }


    /**
     * 根据hash获取所有验证相关信息
     * @param hash
     * @return ReturnData
     */
    public ReturnData getAllValidationData(String hash) {
//        Multihash userRootHash = Multihash.fromBase58(hash);
        ReturnData returnData = new ReturnData();
        Gson gson = new Gson();
        try {
            //由文件hash读取文件，把结果返回给一个string 字符串 userRoot
//            String userRoot = new String(IPFSConnction.getIpfs().cat(userRootHash));
            String userRoot = getFileContent(hash);

            JsonObject allObj = new JsonParser().parse(userRoot).getAsJsonObject();
            JsonElement validInfo = allObj.get("validInfo");
            JsonObject resultObj = new JsonObject();
            resultObj.add("validInfo",validInfo);
            String validInfoStr = resultObj.toString();
            returnData.setRsJsonString(validInfoStr);
            returnData.setRsCode("0");
        } catch (Exception e) {
            // rsCode 1  代表错误  rsCode 0 代表正确
            System.out.println(e.toString());
            returnData.setRsCode("1");
        }
        return returnData;
    }

    /**
     * 根据hash获取用户信息userinfo
     * @param hash
     * @return
     */
    public ReturnData getUserInfoByHash(String hash){
//        Multihash userRootHash = Multihash.fromBase58(hash);
        ReturnData returnData = new ReturnData();

        Gson gson = new Gson();
        try {
//            String userRoot = new String(IPFSConnction.getIpfs().cat(userRootHash));
            String userRoot = getFileContent(hash);

            JsonObject allObj = new JsonParser().parse(userRoot).getAsJsonObject();
            JsonObject validInfoObj = allObj.getAsJsonObject("validInfo");
//            JsonObject userInfoObj = validInfoObj.getAsJsonObject("userInfo");

            JsonElement userInfo = validInfoObj.get("userInfo");
            JsonObject resultObj = new JsonObject();
            resultObj.add("userInfo",userInfo);

            String userInfoStr = resultObj.toString();
            returnData.setRsJsonString(userInfoStr);

            if (userInfoStr == null ||userInfoStr.equals("")){
                returnData.setRsCode("1");
            }else {
                returnData.setRsCode("0");
            }
        }catch (Exception e) {
            // rsCode 1  代表错误  rsCode 0 代表正确
            returnData.setRsCode("1");
        }
        return returnData;
    }

    /**
     * 根据hash获取处方信息
     * @param hash
     * @return ReturnUserInfo
     */
    public ReturnData getPrescriptionByHash(String hash){
//        Multihash userRootHash = Multihash.fromBase58(hash);
        ReturnData returnData = new ReturnData();

        Gson gson = new Gson();
        try {
//            String userRoot = new String(IPFSConnction.getIpfs().cat(userRootHash));
            String userRoot = getFileContent(hash);

            JsonObject allObj = new JsonParser().parse(userRoot).getAsJsonObject();
            JsonObject validInfoObj = allObj.getAsJsonObject("validInfo");
            //JsonObject presObj = validInfoObj.getAsJsonObject("groupList");
            JsonArray groupList = validInfoObj.getAsJsonArray("groupList");

            JsonObject resultObj = new JsonObject();
            resultObj.add("groupList",groupList);
            String presStr = resultObj.toString();

            //String presStr = groupList.toString();


            returnData.setRsJsonString(presStr);

            if (presStr == null || presStr.equals("")){
                returnData.setRsCode("1");
            }else {
                returnData.setRsCode("0");
            }
        }catch (Exception e) {
            // rsCode 1  代表错误  rsCode 0 代表正确
            returnData.setRsCode("1");
        }

        return returnData;
    }

    /**
     * 根据hash获取图片信息
     * @param hash
     * @return ReturnUserInfo
     */
    public ReturnData getImageByHash(String hash){
//        Multihash userRootHash = Multihash.fromBase58(hash);
        ReturnData returnData = new ReturnData();

        Gson gson = new Gson();
        try {
//            String userRoot = new String(IPFSConnction.getIpfs().cat(userRootHash));
            String userRoot = getFileContent(hash);

            JsonObject allObj = new JsonParser().parse(userRoot).getAsJsonObject();
            JsonObject validInfoObj = allObj.getAsJsonObject("validInfo");
            JsonElement image = validInfoObj.get("image");
            JsonObject resultObj = new JsonObject();
            resultObj.add("image",image);
            String imageStr = resultObj.toString();
            returnData.setRsJsonString(imageStr);

            if (imageStr == null || imageStr.equals("") ){
                returnData.setRsCode("1");
            }else {
                returnData.setRsCode("0");
            }
        }catch (Exception e) {
            // rsCode 1  代表错误  rsCode 0 代表正确
            returnData.setRsCode("1");
        }

        return returnData;
    }

    /**
     * 根据Dmi hash获取uploadData
     * @param hash
     * @return
     */
    public ReturnData getResultInfomation(String hash){
//        Multihash userRootHash = Multihash.fromBase58(hash);
        ReturnData returnData = new ReturnData();

        Gson gson = new Gson();
        try {
            //由文件hash读取文件，把结果返回给一个string 字符串 userRoot
//            String userRoot = new String(IPFSConnction.getIpfs().cat(userRootHash));
            String userRoot = getFileContent(hash);

            JsonObject allObj = new JsonParser().parse(userRoot).getAsJsonObject();
            JsonObject validInfoObj = allObj.getAsJsonObject("resultInfo");

            String reStr = validInfoObj.toString();
            returnData.setRsJsonString(reStr);

            if (reStr == null || reStr.equals("")){
                returnData.setRsCode("1");
            }else {
                returnData.setRsCode("0");
            }
        } catch (Exception e) {
            returnData.setRsCode("1");
        }
        return returnData;
    }

    public ReturnData getPayInfomation(String hash){
//        Multihash userRootHash = Multihash.fromBase58(hash);
        ReturnData returnData = new ReturnData();

        Gson gson = new Gson();
        try {
            //由文件hash读取文件，把结果返回给一个string 字符串 userRoot
//            String userRoot = new String(IPFSConnction.getIpfs().cat(userRootHash));
            String userRoot = getFileContent(hash);

            JsonObject allObj = new JsonParser().parse(userRoot).getAsJsonObject();
           // JsonObject payInfoObj = allObj.getAsJsonObject("payInfo");

            JsonElement payInfo = allObj.get("payInfo");
            JsonObject resultObj = new JsonObject();
            resultObj.add("payInfo",payInfo);
            String payInfoStr = resultObj.toString();
            returnData.setRsJsonString(payInfoStr);

         //   String reStr = payInfoObj.toString();
          //  returnData.setRsJsonString(reStr);

            if (payInfoStr == null || payInfoStr.equals("")){
                returnData.setRsCode("1");
            }else {
                returnData.setRsCode("0");
            }
        } catch (Exception e) {
            returnData.setRsCode("1");
        }
        return returnData;
    }

    /**
     * 上传DMI验证结果，以及预结算分账单信息"resultInfo"
     * @param
     * @return
     */
    public ReturnAddResult addResultInformation(String reqData){

        ReturnAddResult re = new ReturnAddResult();
        Gson gson = new Gson();

        JsonObject infoObj = new JsonParser().parse(reqData).getAsJsonObject();
        String hash = infoObj.get("hashId").getAsString();

        JsonObject dmiObj = infoObj.getAsJsonObject("dmiResult");
        //Multihash.fromBase58(hash);
        try {
//            Multihash userRootHash =   Multihash.fromBase58(hash);
//            String userRoot = new String(IPFSConnction.getIpfs().cat(userRootHash));
            String userRoot = getFileContent(hash);
            JsonObject allObj = new JsonParser().parse(userRoot).getAsJsonObject();
            allObj.add("resultInfo",dmiObj.get("resultInfo"));

            String newDataStr = allObj.toString();
            re = uploadFileToIPFS(newDataStr);

        }  catch (Exception e) {
            re.setRsCode("1");
        }

        return re;
    }


    /**
     * 上传结算账单信息 payInfo
     * @param
     * @return
     */
    public ReturnAddResult addPayInformation(String reqJson){

        ReturnAddResult re = new ReturnAddResult();
        Gson gson = new Gson();
        JsonObject reqObj = new JsonParser().parse(reqJson).getAsJsonObject();
        String hash = reqObj.get("hashId").getAsString();
        JsonObject dmiObj = reqObj.getAsJsonObject("payInfo");

        try {
//            Multihash userRootHash = Multihash.fromBase58(hash);
//            String userRoot = new String(IPFSConnction.getIpfs().cat(userRootHash));
            String userRoot = getFileContent(hash);
            JsonObject allObj = new JsonParser().parse(userRoot).getAsJsonObject();
            allObj.add("payInfo",dmiObj.get("payInfo"));

            String newDataStr = allObj.toString();
            re = uploadFileToIPFS(newDataStr);

        }  catch (Exception e) {
            re.setRsCode("1");
        }

        return re;
    }


    /**
     * 为某用户新创建文件，不需要传入hashid，上传成功后返回文件哈希
     * 根据json上传用户数据到IPFS
     * @param json
     * @return
     */
    public ReturnAddResult addUploadDataByJson(String json){
        //Gson gson = new Gson();
        //UploadData uploadData = gson.fromJson(json, UploadData.class);
        //String uploadDataJson = new String(gson.toJson(uploadData));
        //return uploadFileToIPFS(uploadDataJson);
        return uploadFileToIPFS(json);
    }

    /**
     * 上传信息到IPFS
     * @param string
     * @return
     */
    public ReturnAddResult uploadFileToIPFS(String string){
        ReturnAddResult returnAddResult = new ReturnAddResult();
        try {
            String toBeUploaded = EncryptionUtil.encryptDES(string,key);
            //NamedStreamable.ByteArrayWrapper userRootFile = new NamedStreamable.ByteArrayWrapper(string.getBytes());
            NamedStreamable.ByteArrayWrapper userRootFile = new NamedStreamable.ByteArrayWrapper(toBeUploaded.getBytes());
            List<MerkleNode> results = IPFSConnction.getIpfs().add(userRootFile);
            //填充返回json
            returnAddResult.setNewHash( results.get(results.size() - 1).hash.toString());
            returnAddResult.setRsCode("0");
        }catch (IOException e){
            returnAddResult.setRsCode("1");
        }
        return returnAddResult;
    }

    private String key = "JZcLeYUxht8=";
}
