package com.yibao.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yibao.mobileapp.MainActivity;
import com.yibao.mobileapp.R;
import com.yibao.mobileapp.entity.PrescriptionsEntity;
import com.yibao.mobileapp.util.CommonEntity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 2018/4/18.
 */

public class RecordActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private LinearLayout llList;
    private CheckBox cbSelectAll;
    private Button btnRight;
    private TextView tvPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initTitle(getString(R.string.ac_record_title));
        llList = (LinearLayout) findViewById(R.id.ll_record_list);
        cbSelectAll = ((CheckBox) findViewById(R.id.item_cb_allselect));
        cbSelectAll.setOnCheckedChangeListener(this);
        btnRight = (Button) findViewById(R.id.btn_right);
        btnRight.setOnClickListener(this);
        tvPrice=(TextView)findViewById(R.id.tv_record_total_price);
        test();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                Intent intent = new Intent(this,
                        MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_right:
                toPay();
                break;
            case R.id.ll_sub:
                int pos=Integer.parseInt(((String) v.getTag()).substring(2));
                Intent intent1=new Intent(this,BillActivity.class);
                intent1.putExtra("pos",pos);
                startActivity(intent1);
                break;

        }
    }

    ArrayList<PrescriptionsEntity> entities;

    private void test() {
//        entities = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 4; j++) {
//                PrescriptionsEntity entity = new PrescriptionsEntity();
//                entity.setPrescriptionId("测试id" + i + j);
//                entity.setTreatmentDate("测试日期" + i);
//                entities.add(entity);
//            }
//        }
        entities= CommonEntity.prescriptionsListEntity;
        if(entities!=null) {
            addCheckBox(CommonEntity.prescriptionsListEntity);
        }
        cbSelectAll.setChecked(true);
    }

    HashMap<String, ArrayList<Integer>> hashMapGroup = new HashMap<>();

    private void addCheckBox(ArrayList<PrescriptionsEntity> entities) {
        String date = "";
        for (int i = 0; i < entities.size(); i++) {

            if (!date.equals(entities.get(i).getTreatmentDate())) {
                LinearLayout ll = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_check_title, null);
                CheckBox cb = ll.findViewById(R.id.item_cb_title);
                cb.setTag(entities.get(i).getTreatmentDate());
                cb.setOnCheckedChangeListener(this);
                cb.setText(getString(R.string.user_info)+entities.get(i).getTreatmentDate());
                LinearLayout llsub = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_check_sub, null);
                CheckBox cbsub = llsub.findViewById(R.id.item_cb_sub);
                llsub.setTag("ll"+i);
                llsub.setOnClickListener(this);
                cbsub.setTag(i);
                cbsub.setText(entities.get(i).getPrescriptionName());
                cbsub.setOnCheckedChangeListener(this);
                TextView tvDoctorInfo=(TextView)llsub.findViewById(R.id.tv_item_doctorinfo);
                tvDoctorInfo.setText(entities.get(i).getDeptName()+" | "+entities.get(i).getDoctorName());
                TextView tvPrice=(TextView)llsub.findViewById(R.id.tv_item_price);
                double priceDouble=entities.get(i).getPayActully()+entities.get(i).getReimbursement();
                DecimalFormat df   = new DecimalFormat("######0.00");
                tvPrice.setText("￥"+df.format(priceDouble));
                llList.addView(ll);
                llList.addView(llsub);
                date = entities.get(i).getTreatmentDate();
                ArrayList<Integer> groupList = new ArrayList<>();
                groupList.add(i);
                hashMapGroup.put(date, groupList);

            } else {
                LinearLayout llsub = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_check_sub, null);
                CheckBox cbsub = llsub.findViewById(R.id.item_cb_sub);
                llsub.setTag("ll"+i);
                llsub.setOnClickListener(this);
                cbsub.setTag(i);
                cbsub.setText(entities.get(i).getPrescriptionName());
                cbsub.setOnCheckedChangeListener(this);
                TextView tvDoctorInfo=(TextView)llsub.findViewById(R.id.tv_item_doctorinfo);
                tvDoctorInfo.setText(entities.get(i).getDeptName()+" | "+entities.get(i).getDoctorName());
                TextView tvPrice=(TextView)llsub.findViewById(R.id.tv_item_price);
                double priceDouble=entities.get(i).getPayActully()+entities.get(i).getReimbursement();
                DecimalFormat df   = new DecimalFormat("######0.00");
                tvPrice.setText("￥"+df.format(priceDouble));
                llList.addView(llsub);
                ArrayList<Integer> groupList = hashMapGroup.get(date);
                groupList.add(i);
            }

        }
    }
    private void changeTotalPrice(){
        double pPrice=0;
        double reim=0;
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).isSelect()) {
                pPrice+=entities.get(i).getPayActully();
                reim+=entities.get(i).getReimbursement();
            }
        }
        DecimalFormat df   = new DecimalFormat("######0.00");
        tvPrice.setText("￥"+df.format(pPrice+reim));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.item_cb_title:
                ArrayList<Integer> groupList = hashMapGroup.get(buttonView.getTag());
                for (int i = 0; i < groupList.size(); i++) {
                    CheckBox cb = (CheckBox) llList.findViewWithTag(groupList.get(i));
                    cb.setChecked(isChecked);
                }
                changeTotalPrice();

                break;
            case R.id.item_cb_sub:
                int pos = (Integer) buttonView.getTag();
                entities.get(pos).setSelect(isChecked);
                changeTotalPrice();
                break;
            case R.id.item_cb_allselect:
                for (int i = 0; i < entities.size(); i++) {
                    CheckBox cb = (CheckBox) llList.findViewWithTag(i);
                    CheckBox cb2 = llList.findViewWithTag(entities.get(i).getTreatmentDate());
                    cb.setChecked(isChecked);
                    cb2.setChecked(isChecked);
                    entities.get(i).setSelect(isChecked);
                }
                changeTotalPrice();
                break;
        }
    }

    private void toPay () {
        String ids = "";
        double pPrice=0;
        double reim=0;
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).isSelect()) {
                ids += entities.get(i).getPrescriptionId() + ",";
                pPrice+=entities.get(i).getPayActully();
                reim+=entities.get(i).getReimbursement();
            }
        }
        if(pPrice<=0){
            Toast.makeText(RecordActivity.this,getString(R.string.pls_select_bill),Toast.LENGTH_SHORT).show();
            return;
        }
        DecimalFormat df   = new DecimalFormat("######0.00");
        Intent intent = new Intent(this,
                PayActivity.class);
        intent.putExtra("ids",ids.substring(0,ids.length()-1));
        intent.putExtra("payprice",df.format(pPrice));
        intent.putExtra("reim",df.format(reim));
        intent.putExtra("tprice",df.format(pPrice+reim));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
//        finish();
    }
}
