package com.yibao.hospitalapp.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yibao.hospitalapp.MainActivity;
import com.yibao.hospitalapp.R;
import com.yibao.hospitalapp.entity.UserInfoEntity;

import java.util.ArrayList;

/**
 * @author YuXiu 2016-7-25
 */
public class SelectIdCardAdapter extends MyBaseAdapter<UserInfoEntity> implements View.OnClickListener {

    private int selectpos=-1;

    public int getSelectpos() {
        return selectpos;
    }

    public void setSelectpos(int selectpos) {
        this.selectpos = selectpos;
    }

    public SelectIdCardAdapter(Context context) {
        super(context);

    }

    private static class ViewHolder {
        ImageView ivSex;
        TextView tvName;
        TextView tvIdNum;
        TextView tvCheck;
        RelativeLayout rl;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_idcard, null);
            holder = new ViewHolder();
            holder.ivSex = (ImageView) convertView
                    .findViewById(R.id.iv_item_sex);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_item_name);
            holder.tvIdNum = (TextView) convertView
                    .findViewById(R.id.tv_item_idnum);
            holder.tvCheck= (TextView) convertView.findViewById(R.id.tv_item_check);
             holder.rl=(RelativeLayout)convertView.findViewById(R.id.rl);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        boolean select = data.get(position).isSelect();

        if (select){
            holder.tvCheck.setVisibility(View.VISIBLE);

        }
        else {
           holder.tvCheck.setVisibility(View.INVISIBLE);
        }
        holder.tvName.setText(data.get(position).getName());
        String s=data.get(position).getIdNumber();
        String buf="";
            buf=s.substring(0,3)+"***********"+s.substring(14);
        holder.tvIdNum.setText(buf);
        if(data.get(position).getGender().equals("0")){
                  holder.ivSex.setImageResource(R.mipmap.male);
        } else{
            holder.ivSex.setImageResource(R.mipmap.female);
        }
        holder.rl.setTag(R.id.rl, position);
        holder.rl.setOnClickListener(this);
        holder.tvCheck.setOnClickListener(this);
        holder.tvCheck.setTag(R.id.tv_item_check,position);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag(v.getId());
        switch (v.getId()) {
            case R.id.rl:
                if(position!=selectpos) {
                    data.get(position).setSelect(true);
                    if (selectpos != -1)
                        data.get(selectpos).setSelect(false);
                    selectpos = position;
                    notifyDataSetChanged();
                }else{
                }
                break;
            case R.id.tv_item_check:
                ((MainActivity)context).check(position);
//                Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
