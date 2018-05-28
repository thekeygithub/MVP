package com.yibao.hospitalapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;


import com.yibao.hospitalapp.util.CommonUtils;

import java.util.HashMap;

/**
 * Created by WY01 on 2017/4/17.
 */

public abstract class BaseBegPermissionActivity extends BaseActivity {
    private TodoBackFromBeg backDo;
    protected void setBackDo(TodoBackFromBeg backDo){
        this.backDo=backDo;
    }
    protected String[] begRWPermissons = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_PHONE_STATE};
    protected int BEGRWCODE=1;

    /**
     * 这个方法用于动态申请权限
     *
     * @param begPermissions 需要检测的一些权限
     */
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


    /**
     * 弹出请求权限对话框
     */
    private void goBegPermission(String[] begPermissions,int requestCode) {

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(begPermissions, requestCode);
        }
    }


    /**
     * 去设置
     */

    private void gotoSetting() {
        Intent intent = new Intent();
        Uri packageURI = Uri.parse("package:" + "com.yibao.hospitalapp");
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(packageURI);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {


            for (int i = 0; i < permissions.length; i++) {
                if (Build.VERSION.SDK_INT >= 23) {

                    int checkedPermissionResult = checkSelfPermission(permissions[i]);
                    if (checkedPermissionResult != PackageManager.PERMISSION_GRANTED) {

                        if (!shouldShowRequestPermissionRationale(permissions[i])) {
                            showGotoSetting(requestCode);
                            return;
                        }
                    }
                }
            }

            backDo.backTodo(requestCode);

    }

    /**
     * 弹出去设置界面对话框
     */
    private void showGotoSetting(int requestCode) {
        final int code=requestCode;
        HashMap<String, DialogInterface.OnClickListener> clickOptionMap = new HashMap<String, DialogInterface.OnClickListener>();
        clickOptionMap.put("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gotoSetting();
                backDo.settingBack(code);
            }
        });
        clickOptionMap.put("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                backDo.cancelTodo(code);
            }
        });
        CommonUtils.showButtonDialog(BaseBegPermissionActivity.this, "未开启用户权限，去设置", "开启用户权限",
                clickOptionMap);
    }

    public interface TodoBackFromBeg {


        void backTodo(int requestCode);
        void cancelTodo(int requestCode);
        void settingBack(int requsetCode);
    }
}
