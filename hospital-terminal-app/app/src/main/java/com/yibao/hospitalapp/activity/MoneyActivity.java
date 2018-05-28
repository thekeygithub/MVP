package com.yibao.hospitalapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.yibao.hospitalapp.MainActivity;
import com.yibao.hospitalapp.R;
import com.yibao.hospitalapp.util.CommonEntity;

/**
 * Created by root on 2018/4/20.
 */

public class MoneyActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        findViewById(R.id.btn_exit).setOnClickListener(this);
       TextView tv=(TextView) findViewById(R.id.tv_balance);
       tv.setText(getString(R.string.balance)+CommonEntity.balance+ "TKY");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit:
                Intent intent = new Intent(this,
                        MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
