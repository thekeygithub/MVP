package com.yibao.hospitalapp.adapter;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * 适配器基类
 * 
 * @author YuXiu 2016-7-14
 * 
 * @param <T>
 *            数据类型
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

	protected Context context;
	protected LayoutInflater inflater;
	protected List<T> data;

	public MyBaseAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public List<T> getData() {
		return data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (data == null)
			return 0;
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		if (data == null)
			return null;
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		if (data == null)
			return 0;
		return position;
	}

}
