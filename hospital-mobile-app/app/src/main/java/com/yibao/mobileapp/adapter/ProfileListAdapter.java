package com.yibao.mobileapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yibao.mobileapp.MainActivity;
import com.yibao.mobileapp.R;
import com.yibao.mobileapp.entity.ProfileListEntity;

/**
 * 主页顶部标题栏菜单适配器
 * @author YuXiu 2016-7-14
 *
 */
public class ProfileListAdapter extends MyBaseAdapter<ProfileListEntity> implements View.OnClickListener{

	public ProfileListAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private static class ViewHolder {
		TextView tv;
		TextView tvToSee;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_profile_list, null);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.tv_item_name);
			holder.tvToSee=(TextView)convertView.findViewById(R.id.tv_item_tosee);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(data.get(position).getHospName());
		holder.tvToSee.setTag(R.id.tv_item_tosee,position);
		holder.tvToSee.setOnClickListener(this);
		return convertView;
	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag(v.getId());
		switch (v.getId()) {
			case R.id.tv_item_tosee:
				((MainActivity) context).check(position);
				break;

		}
	}
}
