package com.yibao.mobileapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yibao.mobileapp.MainActivity;
import com.yibao.mobileapp.R;
import com.yibao.mobileapp.RegisterActivity;

public class UserRegistered extends BaseActivity {
    private ProgressBar progressBar;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);

        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/html/register.html");//加载url   H5 页面本地化
//        webView.loadUrl("file:///android_asset/test.html");//加载asset文件夹下html

        // webView.addJavascriptInterface(this,"android");//添加js监听 这样html就能调用客户端
        // webView.setWebChromeClient(webChromeClient);
        //  webView.setWebViewClient(webViewClient);

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webView.addJavascriptInterface(new UserRegistered.AndroidAndJSInterface(), "android");
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
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    public class AndroidAndJSInterface {
        @JavascriptInterface
        public void registered(){
            Toast.makeText(UserRegistered.this, "注册成功", Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void goback(){
            finish();
        }
    }
}
