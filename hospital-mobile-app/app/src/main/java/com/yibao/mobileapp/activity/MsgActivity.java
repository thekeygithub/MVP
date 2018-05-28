package com.yibao.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yibao.mobileapp.MainActivity;
import com.yibao.mobileapp.R;

/**
 * Created by root on 2018/4/20.
 */

public class MsgActivity extends BaseActivity {
    private int flag;
    private Button btnLeft, btnRight;
    private TextView tvMsg,tvIss,tvIssMsg;
    private ImageView ivMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_msg);
        flag = getIntent().getIntExtra("flag", 0);
        btnLeft = (Button) findViewById(R.id.btn_left);
        btnRight = (Button) findViewById(R.id.btn_right);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        tvMsg = (TextView) findViewById(R.id.tv_error_msg);
        ivMsg = (ImageView) findViewById(R.id.iv_msg);
        tvIss=(TextView)findViewById(R.id.tv_iss);
        tvIssMsg=(TextView)findViewById(R.id.tv_iss_msg);
        tvMsg.setVisibility(View.GONE);
        switch (flag) {
            case 0:
                btnLeft.setText(getString(R.string.cancel));
                btnRight.setText(getString(R.string.seefee));
                ivMsg.setImageResource(R.mipmap.msg_success);
                tvIss.setText(getString(R.string.success));
                tvIssMsg.setText(getString(R.string.sh_success_msg));
                break;
            case 1:
                btnLeft.setText(getString(R.string.retry));
                btnRight.setText(getString(R.string.rehome));

                ivMsg.setImageResource(R.mipmap.msg_fail);
                tvIss.setText(getString(R.string.fail));
                tvIssMsg.setText(getString(R.string.sh_fail_msg));

                tvMsg.setVisibility(View.VISIBLE);
                tvMsg.setText(getIntent().getStringExtra("msg"));
                break;
            case 2:
                if(PayActivity.getInstance()!=null){
                    PayActivity.getInstance().finish();
                }
                btnLeft.setText(getString(R.string.exit));
                btnRight.setText(getString(R.string.seemoney));
                ivMsg.setImageResource(R.mipmap.msg_success);
                tvIss.setText(getString(R.string.pay_success));
                tvIssMsg.setText(getString(R.string.pay_success_msg));
                break;
            case 3:
                btnLeft.setText(getString(R.string.exit));
                btnRight.setText(getString(R.string.repay));
                ivMsg.setImageResource(R.mipmap.msg_fail);
                tvIss.setText(getString(R.string.pay_fail));
                tvIssMsg.setText(getString(R.string.pay_fail_mag));
                break;
        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_left:
                //left
                switch (flag) {
                    case 0:
                        Intent intent = new Intent(this,
                                MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_to_right);
                    case 1:
                        //重试,返回首页并且掉起人脸识别
                        setResult(MainActivity.BACK_AND_RETRY);
                        finish();
                        overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_to_right);
                        break;
                    case 2:
                    case 3:
                        //回首页
                        Intent intent2 = new Intent(this,
                                MainActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        PayActivity.getInstance().finish();
                        finish();
                        overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_to_right);
                        break;
                }
                break;
            case R.id.btn_right:

                //right
                switch (flag) {
                    case 0:
                        //查看账单
                        Intent intent0 = new Intent(this,
                                RecordActivity.class);
                        startActivity(intent0);
//                        MainActivity.getInstance().finish();
                        finish();
                        overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
                        break;
                    case 1:
                        //返回首页
//                Intent intent1 = new Intent(this,
//                        MainActivity.class);
//                startActivity(intent1);
                        overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_to_right);
                        finish();
                        break;
                    case 2:
                        //查看余额
                        Intent intent3 = new Intent(this,
                                MoneyActivity.class);
                        startActivity(intent3);
                        finish();
                        overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
                        break;
                    case 3:
                        //再次支付
//                        Intent intent2 = new Intent(this,
//                                PayActivity.class);
//                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_to_right);
                        finish();
                        break;
                }
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent=new Intent(MsgActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
