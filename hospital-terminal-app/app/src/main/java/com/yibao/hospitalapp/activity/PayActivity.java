package com.yibao.hospitalapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yibao.hospitalapp.MainActivity;
import com.yibao.hospitalapp.R;
import com.yibao.hospitalapp.entity.PrescriptionsEntity;
import com.yibao.hospitalapp.entity.UserInfoEntity;
import com.yibao.hospitalapp.okhttp.CommonHttp;
import com.yibao.hospitalapp.okhttp.CommonHttpCallback;
import com.yibao.hospitalapp.okhttp.HashMapParams;
import com.yibao.hospitalapp.okhttp.UrlObject;
import com.yibao.hospitalapp.util.CommonEntity;
import com.yibao.hospitalapp.util.CommonUtils;
import com.yibao.hospitalapp.widget.OnPasswordInputFinish;
import com.yibao.hospitalapp.widget.PasswordView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by root on 2018/4/20.
 */

public class PayActivity extends BaseActivity{
    private  PasswordView pwdView;
    public static Activity instance;
    public static Activity getInstance(){
        return instance;
    }
    private TextView tvTimeText,tvPayPwd,tvPayFace,tvTky,tvTranNo,tvPayPrice,tvReim,tvPrice;//
    private int MS = 60;
    private TimerTask timerTask; // 计时器任务
    private Timer timer;// 计时器
    private Button btnLeft,btnRight;
    private ImageView ivWindowCancel;
    private LinearLayout ll_window;
    private String transactionNO;
    private RadioGroup rgPayType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /************* 第一种用法————开始 ***************/
        setContentView(R.layout.activity_payfor);
        instance=this;
        btnLeft=(Button)findViewById(R.id.btn_left);
        btnRight=(Button)findViewById(R.id.btn_right);
        tvTky=(TextView)findViewById(R.id.tv_pay_tky);
        btnRight.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        pwdView= (PasswordView) findViewById(R.id.pwd_view);
        tvTimeText=(TextView)findViewById(R.id.tv_timer);
       tvPayFace=(TextView)findViewById(R.id.tv_facepay);
       tvPayPwd=(TextView)findViewById(R.id.tv_pay_by_pwd);
       tvPayFace.setOnClickListener(this);
       tvPayPwd.setOnClickListener(this);
       ll_window=(LinearLayout)findViewById(R.id.ll_window);
       ivWindowCancel=(ImageView)findViewById(R.id.iv_window_cancle);
       ivWindowCancel.setOnClickListener(this);
       tvPayPrice=(TextView)findViewById(R.id.tv_pay_payprice);
       tvReim=(TextView)findViewById(R.id.tv_pay_reim);
       tvPrice=(TextView)findViewById(R.id.tv_price);
       tvTranNo=(TextView)findViewById(R.id.tv_pay_transactionNO);
       rgPayType=(RadioGroup)findViewById(R.id.rg_pay_type);
       transactionNO=getTransactionNO();
       tvPayPrice.setText("￥"+getIntent().getStringExtra("payprice"));
        tvReim.setText("￥"+getIntent().getStringExtra("reim"));
        tvPrice.setText(getString(R.string.amount_rmb)+getIntent().getStringExtra("tprice"));
        tvTranNo.setText(transactionNO);
        findViewById(R.id.iv_ewm_back).setOnClickListener(this);
        //添加密码输入完成的响应
        pwdView.setOnFinishInput(new OnPasswordInputFinish() {
            @Override
            public void inputFinish() {
                //输入完成后我们简单显示一下输入的密码
                //也就是说——>实现你的交易逻辑什么的在这里写
//                Toast.makeText(PayActivity.this, pwdView.getStrPassword(), Toast.LENGTH_SHORT).show();
                payByPwd(pwdView.getStrPassword());
//                Intent intent=new Intent(PayActivity.this,MsgActivity.class);
//                intent.putExtra("flag",2);
//                startActivity(intent);
//                finish();
            }
        });

        /**
         *  可以用自定义控件中暴露出来的cancelImageView方法，重新提供相应
         *  如果写了，会覆盖我们在自定义控件中提供的响应
         *  可以看到这里toast显示 "Biu Biu Biu"而不是"Cancel"*/
        pwdView.getCancelImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(PayActivity.this, "Biu Biu Biu", Toast.LENGTH_SHORT).show();
                pwdView.setVisibility(View.GONE);
                pwdView.clear();
            }
        });
        /************ 第一种用法————结束 ******************/


        /************* 第二种用法————开始 *****************/
//        final PasswordView pwdView = new PasswordView(this);
//        setContentView(pwdView);
//        pwdView.setOnFinishInput(new OnPasswordInputFinish() {
//            @Override
//            public void inputFinish() {
//                Toast.makeText(MainActivity.this, pwdView.getStrPassword(), Toast.LENGTH_SHORT).show();
//            }
//        });
        /************** 第二种用法————结束 ****************/

        timerTask = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (MS > 0) {
                   handler.sendEmptyMessage(1);
                } else {

                  handler.sendEmptyMessage(0);
                }
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0,1000);
        changeToTKY();
        rgPayType.check(R.id.rb_paytype_tky);
    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {


                case 0:
                    timer.cancel();
                    Intent intent2 = new Intent(PayActivity.this,
                            MainActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
                case 1:
                    tvTimeText.setText(   MS-- + getString(R.string.cancel_pay));
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    private String strTky;
    private void changeToTKY(){
        showPDialog();
        HashMapParams params=new HashMapParams();
        params.put("amount",getIntent().getStringExtra("payprice"));
        new CommonHttp(PayActivity.this,new CommonHttpCallback() {
            @Override
            public void requestSeccess(String json) {

                dismissPDialog();
                try {
                    strTky= new JSONObject(CommonHttp.getBodyObj(json)).getString("tky");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tvTky.setText("TKY"+strTky);
            }

            @Override
            public void requestFail(String msg) {
                dismissPDialog();
                Toast.makeText(PayActivity.this,msg,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestAbnormal(int code) {
                Toast.makeText(PayActivity.this,getString(R.string.net_error),Toast.LENGTH_SHORT).show();
                dismissPDialog();
            }

        }).doRequest(UrlObject.EXCHANGETOTKY,params);
    }
    private void payByPwd(String pwd){
        showPDialog();

         HashMapParams params=new HashMapParams();
        params.put("TKY", strTky);
        params.put("idNumber",CommonEntity.idNumber);
        params.put("password",pwd);
        params.put("treatmentId",CommonEntity.treatmentId);
        params.put("prescriptionList",getIntent().getStringExtra("ids"));
        params.put("transactionNO",transactionNO);

//            params.put("prescriptionIds",userentity.get)
        new CommonHttp(PayActivity.this,new CommonHttpCallback() {
            @Override
            public void requestSeccess(String json) {
                dismissPDialog();
                try {
                    CommonEntity.balance=new JSONObject(CommonHttp.getBodyObj(json)).getString("balance");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(PayActivity.this,MsgActivity.class);
                intent.putExtra("flag",2);
                startActivity(intent);

                finish();
            }

            @Override
            public void requestFail(String msg) {
                dismissPDialog();
                Toast.makeText(PayActivity.this,msg,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestAbnormal(int code) {
                Toast.makeText(PayActivity.this,getString(R.string.net_error),Toast.LENGTH_SHORT).show();
                dismissPDialog();
            }

        }).doRequest(UrlObject.PAYBYPASSWORD,params);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_left:
                Intent intent2 = new Intent(PayActivity.this,
                        MainActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.btn_right:
                if(rgPayType.getCheckedRadioButtonId()==R.id.rb_paytype_tky) {
                    ll_window.setVisibility(View.VISIBLE);
                } else{
                    findViewById(R.id.ll_ewm).setVisibility(View.VISIBLE);
                    }
                break;
            case R.id.tv_pay_by_pwd:

                    pwdView.setVisibility(View.VISIBLE);
                    ll_window.setVisibility(View.GONE);

                break;
            case R.id.tv_facepay:
                    Intent intent = new Intent(PayActivity.this, OpenCvCameraActivity.class);
                    HashMapParams params = new HashMapParams();
                    params.put("TKY", strTky);
                    params.put("idNumber", CommonEntity.idNumber);
                    params.put("treatmentId", CommonEntity.treatmentId);
                    params.put("prescriptionList", getIntent().getStringExtra("ids"));
                    params.put("transactionNO", transactionNO);
                    intent.putExtra("paymap", params);
                    startActivity(intent);
                    timer.cancel();
                break;
            case R.id.iv_window_cancle:
                ll_window.setVisibility(View.GONE);
                break;
            case R.id.iv_ewm_back:
                findViewById(R.id.ll_ewm).setVisibility(View.GONE);
                break;

        }
    }
    private String getTransactionNO(){
        String date = new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis());
        String seconds = new SimpleDateFormat("HHmmss").format(System.currentTimeMillis());
        return "000014"+CommonEntity.treatmentId+date+seconds;
    }



    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
