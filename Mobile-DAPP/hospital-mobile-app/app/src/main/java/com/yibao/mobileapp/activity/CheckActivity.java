package com.yibao.mobileapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yibao.mobileapp.MainActivity;
import com.yibao.mobileapp.R;
import com.yibao.mobileapp.entity.PrescriptionsEntity;
import com.yibao.mobileapp.entity.ProfileListEntity;
import com.yibao.mobileapp.entity.UserInfoEntity;
import com.yibao.mobileapp.okhttp.CommonHttp;
import com.yibao.mobileapp.okhttp.CommonHttpCallback;
import com.yibao.mobileapp.okhttp.HashMapParams;
import com.yibao.mobileapp.okhttp.UrlObject;
import com.yibao.mobileapp.util.CommonEntity;
import com.yibao.mobileapp.util.CommonUtils;
import com.yibao.mobileapp.util.StorageUtils;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by root on 2018/4/23.
 */

public class CheckActivity extends BaseActivity {
    private ProgressBar progerBar;
    private Timer timer;
    private int progress=0;
    private ImageView ivCheck;
    private Bitmap bitmap;
    private TextView tvMsg;
//    public static Activity instance;
//    public static Activity getInstance(){
//        return instance;
//    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        initTitle(getString(R.string.ac_check_title));
//        instance=this;
        progerBar = (ProgressBar) findViewById(R.id.pb_pbar);
        progerBar.setMax(100);
        ivCheck=(ImageView)findViewById(R.id.iv_check);
        if(MainActivity.bitmap!=null) {
            bitmap=MainActivity.bitmap;
            MainActivity.bitmap=null;
            ivCheck.setImageBitmap(bitmap);
            saveImg(bitmap);
        }

//        bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.male);

        tvMsg=(TextView)findViewById(R.id.tv_msg);
        tvMsg.setText("正在验证社保卡基本信息");

//        ivCheck.setImageResource(R.mipmap.shenhe_success);
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // // TODO Auto-generated method stub
                testhandler.sendEmptyMessage(0);
            }
        }, 0, 500);
        if(getIntent().getSerializableExtra("userinfo")!=null)
        checkByServer();
    }
    private ProfileListEntity userentity;
    private void checkByServer(){

//            showPDialog();
            userentity=(ProfileListEntity) getIntent().getSerializableExtra("userinfo");
            final HashMapParams params=new HashMapParams();
            params.put("image", CommonUtils.bitmapToBase64(bitmap));
//            params.put("patientId",userentity.getPatientId());
            params.put("hospId", userentity.getHospId());
            CommonEntity.HospitalId=userentity.getHospId();
            CommonEntity.idNumber=userentity.getIdNumber();
            CommonEntity.treatmentId=userentity.getTreatmentId();
            params.put("idNumber",userentity.getIdNumber());
            params.put("treatmentId",userentity.getTreatmentId());

            // http://222.128.14.106:2989/hospital-dapp/terminal/api/verifyUser?hospId=111&idNumber=22222&treatmentId=111&image=

            params.put("deviceId",CommonUtils.getMobile(CheckActivity.this));
//            params.put("prescriptionIds",userentity.get)
            new CommonHttp(CheckActivity.this,new CommonHttpCallback() {
                @Override
                public void requestSeccess(String json) {
                    dismissPDialog();
                    Log.e("tag",json);
                    if(timer!=null)
                    timer.cancel();
                    progress=100;
                    progerBar.setProgress(progress);
                    try {
                        String str=new org.json.JSONObject(CommonHttp.getBodyObj(json)).getJSONArray("prescriptions").toString();
                        CommonEntity.prescriptionsListEntity=(ArrayList<PrescriptionsEntity>) JSON.parseArray(str,PrescriptionsEntity.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CommonEntity.idNumber=userentity.getIdNumber();
                    CommonEntity.treatmentId=userentity.getTreatmentId();
                    Intent intent=new Intent(CheckActivity.this, MsgActivity.class);
                    intent.putExtra("flag",0);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
                }

                @Override
                public void requestFail(String msg) {
                    dismissPDialog();
                    Toast.makeText(CheckActivity.this,msg,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(CheckActivity.this, MsgActivity.class);
                    intent.putExtra("flag",1);
                    intent.putExtra("msg",msg);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void requestAbnormal(int code) {
                    Toast.makeText(CheckActivity.this,getString(R.string.net_error),Toast.LENGTH_SHORT).show();
                    dismissPDialog();
                    finish();
                }

            }).doRequest(UrlObject.VERIFYUSER2,params);

    }
    private void saveImg(Bitmap bitmap){
        //先把mat转成bitmap

//        Bitmap mBitmap = null;

        File cacheDir = StorageUtils.getOwnCacheDirectory(
                getApplicationContext(), "yibao/cache");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(cacheDir.getAbsolutePath() + "checkfacewriteout.jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            testhandler.sendEmptyMessage(0);
//            Log.d(TAG, "图片已保存至本地");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_to_right);
                break;
        }

    }
    private Handler testhandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
//           Toast.makeText(MainActivity.this,"刷新",Toast.LENGTH_SHORT).show();
            progerBar.setProgress(progress++);
            if(progress==90){
                timer.cancel();
            }
            if(progress>5){
                tvMsg.setText(getString(R.string.check_item_1));
            }
            if(progress>10){
//                tvMsg.setText(getString(R.string.check_item_3));
            }
            if(progress>15){
                tvMsg.setText(getString(R.string.check_item_3));
            }
            if(progress>20){
                tvMsg.setText(getString(R.string.check_item_4));
            }
            if(progress>25){
                tvMsg.setText(getString(R.string.check_item_5));
            }
            if(progress>30){
                tvMsg.setText(getString(R.string.check_item_6));
            }
            if(progress>35){
                tvMsg.setText(getString(R.string.check_item_7));
            }
            if(progress>40){
                tvMsg.setText(getString(R.string.check_item_8));
            }
            if(progress>45){
                tvMsg.setText(getString(R.string.check_item_9));
            }
            if(progress>50){
                tvMsg.setText(getString(R.string.check_item_10));
            }
            if(progress>55){
                tvMsg.setText(getString(R.string.check_item_11));
            }
//            if(progress>60){
//                tvMsg.setText("正在验证");
//            }
//            if(progress>70){
//                tvMsg.setText("正在验证");
//            }
//            if(progress==100){

////                    Intent intent=new Intent(MainActivity.this, MsgActivity.class);
////                    intent.putExtra("flag",1);
////                    startActivity(intent);
//            }
            return false;
        }
    });
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
        }
    }
}
