package com.yibao.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.yibao.mobileapp.MainActivity;
import com.yibao.mobileapp.R;
import com.yibao.mobileapp.util.CommonEntity;

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
       initTitle(getString(R.string.ac_balance_title));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                Intent intent = new Intent(this,
                        MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_to_right);
                break;
        }
    }
}
