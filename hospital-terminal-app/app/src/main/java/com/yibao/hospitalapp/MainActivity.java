package com.yibao.hospitalapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.livedetect.LiveDetectActivity;
import com.livedetect.utils.FileUtils;
import com.livedetect.utils.LogUtil;
import com.yibao.hospitalapp.activity.BaseBegPermissionActivity;
import com.yibao.hospitalapp.activity.BillActivity;
import com.yibao.hospitalapp.activity.CheckActivity;
import com.yibao.hospitalapp.activity.MsgActivity;
import com.yibao.hospitalapp.activity.OpenCvCameraActivity;
import com.yibao.hospitalapp.activity.PayActivity;
import com.yibao.hospitalapp.activity.RecordActivity;
import com.yibao.hospitalapp.adapter.SelectIdCardAdapter;
import com.yibao.hospitalapp.entity.UserInfoEntity;
import com.yibao.hospitalapp.okhttp.CommonHttp;
import com.yibao.hospitalapp.okhttp.CommonHttpCallback;
import com.yibao.hospitalapp.okhttp.HashMapParams;
import com.yibao.hospitalapp.okhttp.UrlObject;
import com.yibao.hospitalapp.util.CommonEntity;
import com.yibao.hospitalapp.util.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

public class MainActivity extends BaseBegPermissionActivity {

    private GridView gvList;
    private SelectIdCardAdapter adapterGvList;
    private EditText etIdNum;
    private ImageButton ibClear,ibSearch;
    private final int START_LIVEDETECT = 0;
   private Timer timer;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builderDialog;
    public  static final int BACK_AND_RETRY=2;
    private RadioGroup rgLanguage;
    public static Activity instance;
    private static final int contectServerTimes=3;
    public static Activity getInstance(){
        return instance;
    }
    @SuppressLint({"HardwareIds", "MissingPermission"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getScreenParam();
        CommonEntity.screenH=screenHeight;
        CommonEntity.screenW=screenWidth;
        instance=this;
        setBackDo(new TodoBackFromBeg() {
            @SuppressLint("MissingPermission")
            @Override
            public void backTodo(int requestCode) {
                if(requestCode== BEGRWCODE) {
                    TelephonyManager telephonyManager=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    HashMapParams.deviceId=telephonyManager.getDeviceId();
                    init(contectServerTimes);
                }

            }

            @Override
            public void cancelTodo(int requestCode) {
                    finish();
            }

            @Override
            public void settingBack(int requsetCode) {
              finish();
            }
        });
        rgLanguage=(RadioGroup) findViewById(R.id.rg_language);
        rgLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_english:
                        CommonEntity.language="en";
                        CommonUtils.shiftLanguage("en",getBaseContext());
                        refreshSelf();
                        break;
                    case R.id.rb_chinese:
                        CommonEntity.language="";
                        CommonUtils.shiftLanguage("",getBaseContext());
                        refreshSelf();
                        break;
                }
            }
        });
        gvList=(GridView)findViewById(R.id.gv_home_list);
        adapterGvList=new SelectIdCardAdapter(this);
        etIdNum=(EditText)findViewById(R.id.et_home_idnum);
        ibClear=(ImageButton)findViewById(R.id.ib_clear);
        ibClear.setVisibility(View.INVISIBLE);
        ibSearch=(ImageButton)findViewById(R.id.ib_search);
        ibSearch.setOnClickListener(this);
        ibClear.setOnClickListener(this);
        etIdNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (etIdNum.getText().toString().length() > 0) {
                    ibClear.setVisibility(View.VISIBLE);

                } else {
                    ibClear.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
               search();
            }
        });


        gvList.setAdapter(adapterGvList);
        if (checkPermissions(begRWPermissons, BEGRWCODE)) {
            TelephonyManager telephonyManager=(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            HashMapParams.deviceId=telephonyManager.getDeviceId();
            if(HashMapParams.deviceId==null||HashMapParams.deviceId.length()<=1){
                HashMapParams.deviceId="error";
            }
            init(contectServerTimes);

        }

    }
    //refresh self
    public void refreshSelf(){
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_item_agree:
                alertDialog.dismiss();
                tocheck();
                break;
            case R.id.tv_item_no_agree:
                alertDialog.dismiss();
                break;
            case R.id.ib_clear:
                etIdNum.setText("");
                break;
            case R.id.ib_search:
                searchById();
//                userInfo = adapterGvList.getData().get(2);
//                Intent intent = new Intent(MainActivity.this, OpenCvCameraActivity.class);
//                startActivity(intent);
                break;
        }
    }
    private void search(){

        String str=etIdNum.getText().toString();
        if(str!=null&&str.length()>0) {
            ArrayList<UserInfoEntity> subEntity=new ArrayList<UserInfoEntity>();
            if(entities==null)
                return;
            for (int i = 0; i < entities.size(); i++) {
                if (entities.get(i).getIdNumber().startsWith(str)) {
                    entities.get(i).setSelect(false);
                    subEntity.add(entities.get(i));
                }
            }
            adapterGvList.setSelectpos(-1);
            adapterGvList.setData(subEntity);

        }else{
            adapterGvList.setSelectpos(-1);
            adapterGvList.setData(entities);
        }
        adapterGvList.notifyDataSetChanged();
    }
    private void searchById(){
        String str=etIdNum.getText().toString();
        if(str!=null&&str.length()==18){

            showPDialog();
            HashMapParams params=new HashMapParams();
            params.put("idNumber",str);
            new CommonHttp(MainActivity.this,new CommonHttpCallback() {
                @Override
                public void requestSeccess(String json) {
                    dismissPDialog();
                    Log.e("tag",json);
//                    entities=new ArrayList<>();
//                    UserInfoEntity entity=(UserInfoEntity) JSON.parseObject(CommonHttp.getBodyObj(json),UserInfoEntity.class);
//                    entities.add(entity);
                    entities= (ArrayList<UserInfoEntity>) JSON.parseArray(CommonHttp.getBodyArr(json),UserInfoEntity.class);
                    search();
                    //                    try {
//                        CommonHttp.token=HashMapParams.deviceId+"-"+new JSONObject(CommonHttp.getBodyObj(json)).getString("token");
//                        Toast.makeText(MainActivity.this,CommonHttp.token,Toast.LENGTH_SHORT).show();
//                        Log.e("token",CommonHttp.token);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Log.e("err",e.getMessage());
//                    }
//
                }

                @Override
                public void requestFail(String msg) {
                    dismissPDialog();
                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void requestAbnormal(int code) {
                    Toast.makeText(MainActivity.this,getString(R.string.net_error),Toast.LENGTH_SHORT).show();
                    dismissPDialog();
                }

            }).doRequest(UrlObject.GETUSERINFODIRECTLY,params);

        }else{
            Toast.makeText(this,"请输入正确的身份信息", Toast.LENGTH_SHORT).show();
        }
    }
    private void init(final int times){
        if(times<1) {
            Toast.makeText(MainActivity.this,getString(R.string.link_system_error),Toast.LENGTH_SHORT).show();
            return;
        }
        showPDialog();
        HashMapParams params=new HashMapParams();
        new CommonHttp(MainActivity.this,new CommonHttpCallback() {
            @Override
            public void requestSeccess(String json) {
                dismissPDialog();

                try {
                    CommonHttp.token=HashMapParams.deviceId+"-"+new JSONObject(CommonHttp.getBodyObj(json)).getString("token");
//                    Toast.makeText(MainActivity.this,CommonHttp.token,Toast.LENGTH_SHORT).show();
                    Log.e("token",CommonHttp.token);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("err",e.getMessage());
                }
//                getdata();
                timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        // // TODO Auto-generated method stub
                        try {
                            testhandler.sendEmptyMessage(0);
                        }catch (Exception e){

                        }

                    }
                }, 0, 60000);
            }

            @Override
            public void requestFail(String msg) {
                dismissPDialog();
//                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                init(times-1);
            }

            @Override
            public void requestAbnormal(int code) {
                Toast.makeText(MainActivity.this,getString(R.string.net_error),Toast.LENGTH_SHORT).show();
                dismissPDialog();
//
//                    entities=new ArrayList<>();
//                    for(int i=0;i<20;i++){
//                        UserInfoEntity entity=new UserInfoEntity();
//                        entity.setName("测试"+i);
//                        entity.setIdNumber(getIdNum());
//                        if(i%2==0){
//                            entity.setGender("男");
//                        }else{
//                            entity.setGender("女");
//                        }
//                        entities.add(entity);
//                    }
//                search();


            }

        }).doRequest(UrlObject.INITIALIZATION,params);
    }
    ArrayList<UserInfoEntity> entities;
    private  synchronized  void getdata(){
            showPDialog();
            HashMapParams params = new HashMapParams();
            new CommonHttp(MainActivity.this, new CommonHttpCallback() {
                @Override
                public void requestSeccess(String json) {
                    dismissPDialog();
                    entities = (ArrayList<UserInfoEntity>) JSON.parseArray(CommonHttp.getBodyArr(json), UserInfoEntity.class);
                    search();
                }

                @Override
                public void requestFail(String msg) {
                    dismissPDialog();
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void requestAbnormal(int code) {
                    Toast.makeText(MainActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
                    entities = new ArrayList<>();
                    dismissPDialog();
                }

            }).doRequest(UrlObject.GETUSERINFO, params);

    }
   private  Handler testhandler=new Handler(new Handler.Callback() {
       @Override
       public boolean handleMessage(Message msg) {
//           Toast.makeText(MainActivity.this,"刷新",Toast.LENGTH_SHORT).show();
           try {
               getdata();
           }catch (Exception e){

           }
           return false;
       }
   });

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private String getIdNum(){
        String s="";
        for(int i=0;i<18;i++){
            Random random=new Random();
          int a=  random.nextInt(9);
            s+=a;
        }
        return s;
    }
    public void check(int position){
            userInfo = adapterGvList.getData().get(position);
            CommonEntity.userinfo = userInfo;
            builderDialog = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View view1 = inflater.inflate(R.layout.alert_msg, null);
            builderDialog.setView(view1);
            view1.findViewById(R.id.tv_item_agree).setOnClickListener(this);
            view1.findViewById(R.id.tv_item_no_agree).setOnClickListener(this);
            alertDialog = builderDialog.show();
            Window window = alertDialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);

    }
    private UserInfoEntity userInfo;
    private void tocheck(){

        Intent intent = new Intent(MainActivity.this,
                LiveDetectActivity.class);

        Bundle bundle = new Bundle();

        bundle.putBoolean("isRandomable", true);
        bundle.putString("actions", "12");
        bundle.putString("selectActionsNum", "1");
        bundle.putString("singleActionDectTime", "8");
        bundle.putBoolean("isWaterable", false);
        bundle.putBoolean("openSound", true);
        intent.putExtra("comprehensive_set",bundle);
//        bundle.putString("SerialNumber","2ab434160772a9bd60d9f057626f39491a88d87e2171d0676432c43ee4d63721247b5b54212a11297e80f6f3796e7321811749afbd683234878da0b841b3493c");//模拟器是3c
        bundle.putString("SerialNumber","5e7944e8878648621c24078ec95a3080ceece270421e66a67acc9a851fc7fdac41167230debb6a4507a1c4fd2c3f8748b275584d1f4726c7413eb2a023a5da17");//一体机是17

        startActivityForResult(intent,START_LIVEDETECT);
    }

    @Override
    protected void onDestroy() {
        if(timer!=null) {
            timer.cancel();
        }
        super.onDestroy();
    }
    public static Bitmap bitmap;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==BACK_AND_RETRY){
            tocheck();
        }
        if(requestCode==START_LIVEDETECT) {
            Intent mIntent = data;
            if (mIntent != null) {
                Bundle result = mIntent.getBundleExtra("result");
                if(result==null){
                    Intent intent = new Intent(MainActivity.this, MsgActivity.class);
                    intent.putExtra("flag", 1);
                    startActivity(intent);
                    return;
                }

                boolean check_pass = result.getBoolean("check_pass");

                if (check_pass) {
                    String str = result.getString("mRezion");
                    byte[] pic_result = result.getByteArray("pic_result");
                    if (pic_result != null) {
                        bitmap = FileUtils.getBitmapByBytesAndScale(pic_result, 1);
                        if (bitmap != null) {
                            Intent intent = new Intent(MainActivity.this, CheckActivity.class);
                            intent.putExtra("userinfo",userInfo);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(MainActivity.this, MsgActivity.class);
                            intent.putExtra("flag", 1);
                            intent.putExtra("msg",getResources().getString(R.string.face_check_fail));
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(MainActivity.this, MsgActivity.class);
                        intent.putExtra("flag", 1);
                        intent.putExtra("msg",getResources().getString(R.string.face_check_fail));
                        startActivity(intent);
                    }


                }else{
                    Intent intent = new Intent(MainActivity.this, MsgActivity.class);
                    intent.putExtra("flag", 1);
                    intent.putExtra("msg",getResources().getString(R.string.face_check_fail));
                    startActivityForResult(intent,BACK_AND_RETRY);
                }
            }else {
                Intent intent = new Intent(MainActivity.this, MsgActivity.class);
                intent.putExtra("flag", 1);
                intent.putExtra("msg",getResources().getString(R.string.face_check_fail));
                startActivity(intent);
            }
        }

    }
}
