package com.yibao.hospitalapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yibao.hospitalapp.MainActivity;
import com.yibao.hospitalapp.R;

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
                if(MainActivity.getInstance()!=null) {
                    MainActivity.getInstance().finish();
                }
                btnLeft.setText(getString(R.string.cancel));
                btnRight.setText(getString(R.string.seefee));
                ivMsg.setImageResource(R.mipmap.right);
                tvIss.setText(getString(R.string.success));
                tvIssMsg.setText(getString(R.string.sh_success_msg));
                break;
            case 1:
                btnLeft.setText(getString(R.string.retry));
                btnRight.setText(getString(R.string.rehome));

                ivMsg.setImageResource(R.mipmap.error);
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
                ivMsg.setImageResource(R.mipmap.right);
                tvIss.setText(getString(R.string.pay_success));
                tvIssMsg.setText(getString(R.string.pay_success_msg));
                break;
            case 3:
                btnLeft.setText(getString(R.string.exit));
                btnRight.setText(getString(R.string.repay));
                ivMsg.setImageResource(R.mipmap.error);
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
                        startActivity(intent);
                        finish();
                    case 1:
                        //重试,返回首页并且掉起人脸识别
                        setResult(MainActivity.BACK_AND_RETRY);
                        finish();
                        break;
                    case 2:
                    case 3:
                        //回首页
                        Intent intent2 = new Intent(this,
                                MainActivity.class);
                        startActivity(intent2);
                        PayActivity.getInstance().finish();
                        finish();
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
                        MainActivity.getInstance().finish();
                        finish();
                        break;
                    case 1:
                        //返回首页
//                Intent intent1 = new Intent(this,
//                        MainActivity.class);
//                startActivity(intent1);
                        finish();
                        break;
                    case 2:
                        //查看余额
                        Intent intent3 = new Intent(this,
                                MoneyActivity.class);
                        startActivity(intent3);
                        finish();
                        break;
                    case 3:
                        //再次支付
//                        Intent intent2 = new Intent(this,
//                                PayActivity.class);
//                        startActivity(intent2);
                        finish();
                        break;
                }
                break;
        }
    }
}
