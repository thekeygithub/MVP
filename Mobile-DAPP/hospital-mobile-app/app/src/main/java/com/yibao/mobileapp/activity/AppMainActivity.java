package com.yibao.mobileapp.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.livedetect.LiveDetectActivity;
import com.livedetect.utils.FileUtils;
import com.yibao.mobileapp.MainActivity;
import com.yibao.mobileapp.RegisterActivity;
import com.yibao.mobileapp.applaction.MyApplication;

import com.yibao.mobileapp.R;
import com.yibao.mobileapp.util.CommonUtils;

import java.util.Timer;


public class AppMainActivity extends BaseActivity {
    private final int START_LIVEDETECT = 0;
    public  static final int BACK_AND_RETRY=2;
    private ProgressBar progressBar;
    private WebView webView;
    private android.support.v7.app.AlertDialog alertDialog;
    private android.support.v7.app.AlertDialog.Builder builderDialog;
    protected String[] begRWPermissons = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_PHONE_STATE};
    protected int BEGRWCODE=1;
    public static Bitmap bitmap;
    //以下刘鹏加的
    private ProgressBar progerBar;
    private Timer timer;
    private int progress=0;
    private ImageView ivCheck;
    //private Bitmap bitmap;
    private TextView tvMsg;
    //以上刘鹏加的
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);

        webView = (WebView) findViewById(R.id.webview);

        String token=((MyApplication)this.getApplication()).getToken();
        String applyPhone=((MyApplication)this.getApplication()).getApplyPhone();
        webView.loadUrl("file:///android_asset/html/index.html?token="+token+"&phone="+applyPhone+"");//加载url   H5 页面本地化
        //        webView.loadUrl("file:///android_asset/test.html");//加载asset文件夹下html


       // webView.addJavascriptInterface(this,"android");//添加js监听 这样html就能调用客户端
       // webView.setWebChromeClient(webChromeClient);
       //  webView.setWebViewClient(webViewClient);

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webView.addJavascriptInterface(new AndroidAndJSInterface(this), "android");
        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        //设置客户端-不跳转到默认浏览器中
        webView.setWebViewClient(new WebViewClient());
        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.

        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        checkPermissions(begRWPermissons, BEGRWCODE);

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
        Log.i("test","onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i("test","onResume");
        super.onResume();
        jsAfterAndroidCheck();
    }

    private void tocheck(){

        Intent intent = new Intent(AppMainActivity.this, LiveDetectActivity.class);

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

    public void jsAfterAndroidCheck(){
        try {
            int checkStatus=((MyApplication)this.getApplication()).getFaceCheckStatus();
            if ( checkStatus == 0 ) return ;
            if( checkStatus!=2 ) checkStatus=3 ;
            webView.loadUrl("javascript:afterAndroidCheck("+checkStatus+")");
            this.setFaceCheckStatus(0);
        }catch(Exception e){
            Toast.makeText(AppMainActivity.this, "Android jsAfterAndroidCheck error" , Toast.LENGTH_SHORT).show();
        }
    }

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
                    Intent intent = new Intent(AppMainActivity.this, MsgActivity.class);
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
                            this.setFaceCheckStatus(2);// 设置全局变量，认识成功
                        } else {
                            this.setFaceCheckStatus(3);
                        }
                    } else {
                        this.setFaceCheckStatus(3);
                    }

                }else{
                    this.setFaceCheckStatus(3);
                }
            }else {
                this.setFaceCheckStatus(3);
            }
        }

    }

    public class AndroidAndJSInterface {
        public AndroidAndJSInterface(AppMainActivity activity) {
        }
        @JavascriptInterface
        public void payment(){
            //Toast.makeText(AppMainActivity.this,"toPayment",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AppMainActivity.this,MainActivity.class);
            startActivity(intent);
            //finish();
        }
        @JavascriptInterface
        public void goback(){
            Intent intent = new Intent(AppMainActivity.this,com.yibao.mobileapp.RegisterActivity.class);
            startActivity(intent);
        }
        @JavascriptInterface
        public void toCheck(){
            Log.i("test","toCheck");
            //Intent intent = new Intent(AppMainActivity.this,OpenCvCameraActivity.class);
            //startActivity(intent);Appprivate final int START_LIVEDETECT = 0;
            setFaceCheckStatus(1);
            check();
        }

        @JavascriptInterface
        public void toGACheck(){
            Log.i("test","toGAheck");
            //Intent intent = new Intent(AppMainActivity.this,OpenCvCameraActivity.class);
            //startActivity(intent);Appprivate final int START_LIVEDETECT = 0;
            //setFaceCheckStatus(1);
            toAppGACheck();
        }
    }

    private void toAppGACheck(){
        Intent intent = new Intent(AppMainActivity.this, CheckActivity.class);
        startActivityForResult(intent,START_LIVEDETECT);
        overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
    }

    public void check(){
        if (checkPermissions(begRWPermissons, BEGRWCODE)) {
            builderDialog = new android.support.v7.app.AlertDialog.Builder(AppMainActivity.this);
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

    protected boolean checkPermissions(String[] begPermissions,int requestCode) {
        boolean result = true;
        if (Build.VERSION.SDK_INT >= 23) {
            for (int i = 0; i < begPermissions.length; i++) {
                int checkedPermissionResult = checkSelfPermission(begPermissions[i]);
                if (checkedPermissionResult != PackageManager.PERMISSION_GRANTED) {
                    goBegPermission(begPermissions,requestCode);
                    result = false;
                    return result;
                }
            }

        }
        return result;
    }

    private void goBegPermission(String[] begPermissions,int requestCode) {

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(begPermissions, requestCode);
        }
    }

    private void setFaceCheckStatus(int v){
        ((MyApplication)this.getApplication()).setFaceCheckStatus(v);
    }


  }


