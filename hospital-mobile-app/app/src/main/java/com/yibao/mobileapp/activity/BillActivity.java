package com.yibao.mobileapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yibao.mobileapp.R;
import com.yibao.mobileapp.entity.PrescriptionsEntity;
import com.yibao.mobileapp.util.CommonEntity;

import java.util.ArrayList;

/**
 * Created by root on 2018/4/26.
 */

public class BillActivity extends BaseActivity {
    private LinearLayout llInfo;
    private LinearLayout llDetails;
    private TextView tvDetails;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        initTitle(getString(R.string.ac_bill_title));
        PrescriptionsEntity info= CommonEntity.prescriptionsListEntity.get(getIntent().getIntExtra("pos",0));
        llInfo=(LinearLayout)findViewById(R.id.ll_info);
        llDetails=(LinearLayout)findViewById(R.id.ll_details);
        tvDetails=(TextView)findViewById(R.id.tv_item_details);
        findViewById(R.id.btn_right).setOnClickListener(this);
        ArrayList<String> str=new ArrayList<>();
//        str.add(getString(R.string.name)+ CommonEntity.userinfo.getName());
//        if(CommonEntity.userinfo.getGender().equals("0")) {
//            str.add(getString(R.string.male) );
//        }else{
//            str.add(getString(R.string.female) );
//        }
//        str.add(getString(R.string.age)+CommonEntity.userinfo.getAge());
        str.add(getString(R.string.deptname)+info.getDeptName());
        str.add(getString(R.string.doctor)+info.getDoctorName());
        str.add(getString(R.string.diagnosis)+info.getDiagnosis());
        str.add(getString(R.string.treament_date)+info.getTreatmentDate());
        ArrayList<String> details=new ArrayList<>();


        for(int i=0;i<info.getPrescriptionDetails().size();i++){
            details.add(getString(R.string.drug_name)+info.getPrescriptionDetails().get(i).getItemName());
            details.add(getString(R.string.specification)+info.getPrescriptionDetails().get(i).getSpecification());
            details.add(getString(R.string.price_rmb)+info.getPrescriptionDetails().get(i).getPrice());
            details.add(getString(R.string.num)+info.getPrescriptionDetails().get(i).getNum()+info.getPrescriptionDetails().get(i).getSpecification());
            details.add(getString(R.string.usage)+info.getPrescriptionDetails().get(i).getUsage());
            details.add(" ");
        }
        for(int i=0;i<str.size();i++) {
            TextView textView = new TextView(this);
            textView.setTextSize(15);
            textView.setText(str.get(i));
            textView.setTextColor(Color.WHITE);
            llInfo.addView(textView);
        }
        for(int i=0;i<details.size();i++) {
            TextView textView = new TextView(this);
            textView.setTextSize(15);
            textView.setText(details.get(i));
            textView.setTextColor(Color.WHITE);
            llDetails.addView(textView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.btn_right:
                finish();
                break;
            case R.id.btn_register_begvcode:
                finish();
                break;
        }
    }
}
