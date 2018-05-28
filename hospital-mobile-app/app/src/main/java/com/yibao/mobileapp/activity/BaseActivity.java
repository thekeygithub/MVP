package com.yibao.mobileapp.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.yibao.mobileapp.MainActivity;
import com.yibao.mobileapp.R;
import com.yibao.mobileapp.util.CommonEntity;
import com.yibao.mobileapp.util.CommonUtils;

/**
 * activity 的基类
 *
 * @author YuXiu 2016-7-14
 */
public abstract class BaseActivity extends AppCompatActivity implements OnClickListener {
    protected AlertDialog progressView;



    @Override
    protected void onResume() {
        super.onResume();
//        CommonUtils.shiftLanguage(CommonEntity.language,getBaseContext());
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
    protected void initTitle(String str) {
        ((TextView) findViewById(R.id.title_text)).setText(str);
        findViewById(R.id.title_back).setOnClickListener(this);
    }

//      @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent intent=new Intent(BaseActivity.this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }



}
