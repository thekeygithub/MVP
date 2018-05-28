package com.yibao.mobileapp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yibao.mobileapp.activity.BaseActivity;
import com.yibao.mobileapp.okhttp.CommonHttp;
import com.yibao.mobileapp.okhttp.CommonHttpCallback;
import com.yibao.mobileapp.okhttp.UrlObject;
import com.yibao.mobileapp.util.CommonUtils;
import com.yibao.mobileapp.util.GetVCode;
import com.yibao.mobileapp.widget.TimeButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/24.
 */

public class RegisterActivity extends BaseActivity {
    private EditText etMobile;
    private EditText etVCode;
    private EditText etPwd;
    private TimeButton tbGetVCode;
    private int sex = -1;
    private String strMobile = null;
    private String strVcode;
    private TextView tvSubmit;
    private GetVCode getVCode;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_register_begvcode:
                strMobile = etMobile.getText().toString();
                if (strMobile != null && !strMobile.equals("") && CommonUtils.isMobileNO(strMobile)) {
                    tbGetVCode.onClick();
                    getVCode.getVCode(strMobile);
//                    if (strVcode != null)
//                        Toast.makeText(RegisterActivity.this, strVcode,
//                                Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "请输入正确手机号",
                            Toast.LENGTH_SHORT).show();
                    tbGetVCode.clearTimer();
                }
                break;
            case R.id.tv_register_submit:
                if (checked()) {
                    submit();
                }
//                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
//                finish();
                break;

            default:
                break;
        }
    }

    private void submit() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mobile", strMobile);
        params.put("code", strVcode);

        new CommonHttp(RegisterActivity.this, new CommonHttpCallback() {


            @Override
            public void requestSeccess(String json) {
                Toast.makeText(RegisterActivity.this, "正在登录", Toast.LENGTH_SHORT).show();
                String body = CommonHttp.getBodyObj(json);
                try {
                    JSONObject object = new JSONObject(body);
                    CommonUtils.saveUserInfo(RegisterActivity.this, strMobile, object.getString("token"));
                   Intent intent= new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void requestFail(String msg) {
                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestAbnormal(int code) {
                Toast.makeText(RegisterActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
            }
        }).doRequest(UrlObject.LOGIN, params);
    }


    private boolean checked() {
        strMobile = etMobile.getText().toString();
        if (strMobile == null || strMobile.equals("")) {
            Toast.makeText(this, getString(R.string.pls_input_mobile), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!CommonUtils.isMobileNO(strMobile)) {
            Toast.makeText(this, getString(R.string.pls_input_right_mobile), Toast.LENGTH_SHORT).show();
            return false;
        }
        strVcode = etVCode.getText().toString();
        if (strVcode == null || strVcode.equals("")) {
            Toast.makeText(this, getString(R.string.pls_input_vcode), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if(CommonUtils.getToken(RegisterActivity.this).length()>0){
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        }
        findView();
        createObject();
        setListener();
    }

    private void findView() {
        // TODO Auto-generated method stub
        etMobile = (EditText) findViewById(R.id.et_register_mobile);
        etVCode = (EditText) findViewById(R.id.et_register_vcode);
        etPwd = (EditText) findViewById(R.id.et_register_pwd);
        tbGetVCode = (TimeButton) findViewById(R.id.btn_register_begvcode);
        tvSubmit = (TextView) findViewById(R.id.tv_register_submit);
    }

    private void createObject() {
        // TODO Auto-generated method stub
        getVCode = new GetVCode(RegisterActivity.this);
    }

    private void setListener() {
        // TODO Auto-generated method stub
        tbGetVCode.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

}
