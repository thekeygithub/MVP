package com.yibao.hospitalapp.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yibao.hospitalapp.util.CommonEntity;
import com.yibao.hospitalapp.util.CommonUtils;

import junit.runner.Version;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * activity 的基类
 *
 * @author YuXiu 2016-7-14
 */
public abstract class BaseActivity extends AppCompatActivity implements OnClickListener {
    protected AlertDialog progressView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.shiftLanguage(CommonEntity.language,getBaseContext());
    }

    protected void showPDialog(){
        if(progressView==null) {
            progressView = CommonUtils.getProgressDialog(BaseActivity.this);
        }
        else{
            progressView.show();
        }

    }
    protected void dismissPDialog(){
        if(progressView!=null) {
            progressView.dismiss();
        }
    }
    protected int screenHeight;// 屏幕高度
    protected int screenWidth;// 屏幕宽度
    /**
     * 获取屏幕宽度高度
     */
    protected void getScreenParam() {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        screenWidth = dm.widthPixels;

        screenHeight = dm.heightPixels;

    }


}
