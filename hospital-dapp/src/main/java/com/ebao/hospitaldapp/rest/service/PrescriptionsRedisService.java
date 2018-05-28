package com.ebao.hospitaldapp.rest.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ebao.hospitaldapp.data.RedisClient;
import com.ebao.hospitaldapp.ebao.entity.PrescriptionDetail;
import com.ebao.hospitaldapp.ebao.entity.PrescriptionEntity;
import com.ebao.hospitaldapp.ebao.entity.PrescriptionGroup;
import com.ebao.hospitaldapp.utils.CommonUtils;
import com.ebao.hospitaldapp.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionsRedisService {

    final static Logger LOGGER = LoggerFactory.getLogger(PrescriptionsRedisService.class);
    @Autowired
    RedisKeyService redisKeyService;

    //以hospId+jzlsno为key，为每条用户数据添加k-v
    //定时任务每2分钟刷新一次UserInfor，同时更新这条list。
    public int putEntityListToRedis(JSONArray jsArray)
    {
        int result = 0;
        try {
            String jsString = jsArray.toJSONString();
            String hospId = "";
            String jzlsno = "";
            if(jsArray.size()>0)
            {
                JSONObject jsObj = jsArray.getJSONObject(0);
                hospId = jsObj.getString("hosp_id");
                jzlsno = jsObj.getString("jzlsno");
            }

            String listKey = CommonUtils.KeyGenerator("-", hospId,jzlsno, PrescriptionEntity.class.getSimpleName());
            RedisClient.set(listKey,jsString,"NX");
            //RedisClient.getJedis().expire(listKey,120); //TODO 最好设置个默认过期时间，比如一天
        }
        catch(Exception e)
        {
            LOGGER.error("PrescriptionsRedisUtils:PutEntityListToRedis，解析失败！",e);
            result = 1;
        }
        return result;
    }

    public PrescriptionEntity getPresEntity(String hospId,String jzlsno)
    {
        String key = redisKeyService.getPrescriptionKey(hospId, jzlsno);
        return getPresEntity(key);

    }

    public PrescriptionEntity getPresEntity(String key) {
        String jsString =  getPresString(key);
        if(jsString == null || "".equals(jsString))
        {
            LOGGER.error("PrescriptionsRedisUtils:GetPresEntity，未找到！");
            return null;
        }

        PrescriptionEntity presEntity = new PrescriptionEntity();
        try {
            JSONArray jsonArray = JSONObject.parseArray(jsString);
            if(jsonArray != null)
            {
                for(int i=0;i<jsonArray.size();i++)
                {
                    JSONObject jsObj = jsonArray.getJSONObject(i);
                    PrescriptionGroup presGroup = new PrescriptionGroup();

                    presGroup.setId(jsObj.getInteger("id"));
                    presGroup.setJzlsno(jsObj.getString("jzlsno"));
                    presGroup.setPresgroupid(jsObj.getString("presgroupid"));
                    presGroup.setHosp_id(jsObj.getString("hosp_id"));
                    presGroup.setTreatment_date(jsObj.getString("treatment_date"));
                    presGroup.setPatient_id(jsObj.getInteger("patient_id"));
                    presGroup.setCreate_at(jsObj.getString("create_at"));
                    presGroup.setMed_type(jsObj.getString("med_type"));
                    presGroup.setDis_code(jsObj.getString("dis_code"));
                    presGroup.setOp_type(jsObj.getString("op_type"));

                    JSONArray mxArray = jsObj.getJSONArray("mx");
                    if(mxArray != null)
                    {
                        for(int j=0;j<mxArray.size();j++)
                        {
                            JSONObject mxObj = mxArray.getJSONObject(j);
                            PrescriptionDetail presDetail = new PrescriptionDetail();
                            presDetail = JsonUtils.fromJson(mxObj.toJSONString(),PrescriptionDetail.class);
                            presGroup.AddPresDetail(presDetail); //Add detail into group
                        }
                    }
                    presEntity.AddPresGroup(presGroup); //Add group into entity
                }
            }
        }
        catch(Exception e)
        {
            LOGGER.error("PrescriptionEntity:GetPresEntity，解析失败！",e);
            return null;
        }
        return presEntity;
    }

    public String getPresString(String hospId,String jzlsno)
    {
        String key = redisKeyService.getPrescriptionKey(hospId, jzlsno);
        return getPresString(key);
    }

    public String getPresString(String key) {
        String jsString =  RedisClient.get(key);
        if(jsString == null || "".equals(jsString))
        {
            LOGGER.error("PrescriptionsRedisUtils:GetPresEntity，未找到！");
            return null;
        }
        return jsString;
    }

}
