package com.yibao.mobileapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.livedetect.LiveDetectActivity;
import com.livedetect.utils.FileUtils;
import com.yibao.mobileapp.R;
import com.yibao.mobileapp.activity.BaseBegPermissionActivity;
import com.yibao.mobileapp.activity.CheckActivity;
import com.yibao.mobileapp.activity.MsgActivity;
import com.yibao.mobileapp.activity.OpenCvCameraActivity;
import com.yibao.mobileapp.adapter.ProfileListAdapter;
import com.yibao.mobileapp.adapter.SelectIdCardAdapter;
import com.yibao.mobileapp.entity.ProfileListEntity;
import com.yibao.mobileapp.entity.UserInfoEntity;
import com.yibao.mobileapp.okhttp.CommonHttp;
import com.yibao.mobileapp.okhttp.CommonHttpCallback;
import com.yibao.mobileapp.okhttp.HashMapParams;
import com.yibao.mobileapp.okhttp.UrlObject;
import com.yibao.mobileapp.util.CommonEntity;
import com.yibao.mobileapp.util.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseBegPermissionActivity {


    private final int START_LIVEDETECT = 0;

    private AlertDialog alertDialog;
    private AlertDialog.Builder builderDialog;
    public  static final int BACK_AND_RETRY=2;
    public static Activity instance;
    private static final int contectServerTimes=3;
    private TextView tvSwAccount,tvMobile,tvNoinfo;
    private ListView lvHospital;
    private ProfileListAdapter adapterHospital;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<ProfileListEntity> hospitalEntityList;
//    public static Activity getInstance(){
//        return instance;
//    }
    @SuppressLint({"HardwareIds", "MissingPermission"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getScreenParam();
        CommonEntity.screenH=screenHeight;
        CommonEntity.screenW=screenWidth;
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getdata();

            }
        });
        tvNoinfo=(TextView)findViewById(R.id.tv_noinfo);
        instance=this;

        setBackDo(new TodoBackFromBeg() {
            @Override
            public void backTodo(int requestCode) {

            }

            @Override
            public void cancelTodo(int requestCode) {

            }

            @Override
            public void settingBack(int requsetCode) {

            }
        });
        lvHospital=(ListView)findViewById(R.id.lv_hospital);
        tvMobile=(TextView)findViewById(R.id.tv_mobile);
        tvMobile.setText(CommonUtils.getUserShare(this).getString("mobile",""));
        tvSwAccount=(TextView)findViewById(R.id.tv_login_swacount);
        tvSwAccount.setOnClickListener(this);
        adapterHospital=new ProfileListAdapter(this);
        lvHospital.setAdapter(adapterHospital);
    }

    private  void getdata(){
        showPDialog();
        HashMapParams params = new HashMapParams();
        params.put("mobile",CommonUtils.getMobile(MainActivity.this));
        new CommonHttp(MainActivity.this, new CommonHttpCallback() {
            @Override
            public void requestSeccess(String json) {
                dismissPDialog();
                hospitalEntityList = (ArrayList<ProfileListEntity>) JSON.parseArray(CommonHttp.getBodyArr(json), ProfileListEntity.class);
                adapterHospital.setData(hospitalEntityList);
                adapterHospital.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                tvNoinfo.setVisibility(View.GONE);
            }

            @Override
            public void requestFail(String msg) {
                dismissPDialog();
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                adapterHospital.setData(null);
                adapterHospital.notifyDataSetChanged();
                tvNoinfo.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void requestAbnormal(int code) {
                Toast.makeText(MainActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
//                entities = new ArrayList<>();
                swipeRefreshLayout.setRefreshing(false);
                dismissPDialog();
            }

        }).doRequest(UrlObject.GETTREATMENTINFO, params);

    }
//    //refresh self
//    public void refreshSelf(){
//        Intent intent=new Intent(this,MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }

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
            case R.id.tv_login_swacount:
                CommonUtils.clearUserInfo(this);
                startActivity(new Intent(this,RegisterActivity.class));

                finish();
                overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getdata();
    }

    public void check(int pos){
        userInfo=hospitalEntityList.get(pos);
        if (checkPermissions(begRWPermissons, BEGRWCODE)) {
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

    }
    private ProfileListEntity userInfo;
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
        overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private static boolean isExit = false;
    /**
     * 点击返回按钮的消息处理器
     */
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;

        }
    };

    /**
     * 连续点击两次按钮退出程序
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

}
