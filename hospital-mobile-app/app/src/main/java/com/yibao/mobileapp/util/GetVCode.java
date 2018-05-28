package com.yibao.mobileapp.util;

import java.net.URL;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.yibao.mobileapp.R;
import com.yibao.mobileapp.okhttp.CommonHttp;
import com.yibao.mobileapp.okhttp.CommonHttpCallback;
import com.yibao.mobileapp.okhttp.UrlObject;


public class GetVCode {
	private String result = null;
	private Context mContext = null;

	public GetVCode(Context mContext) {
		this.mContext = mContext;
	}

	public String getResult() {
		return result;
	}

	public void getVCode(String mobile) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		params.put("type","1");
		new CommonHttp(mContext,new CommonHttpCallback() {

			@Override
			public void requestSeccess(String json) {
                Toast.makeText(mContext,mContext.getString(R.string.getvcode_success),Toast.LENGTH_SHORT).show();
			}

			@Override
			public void requestFail(String msg) {
                Toast.makeText(mContext,mContext.getString(R.string.getvcode_fail),Toast.LENGTH_SHORT).show();
			}

			@Override
			public void requestAbnormal(int code) {
                Toast.makeText(mContext,mContext.getString(R.string.net_error),Toast.LENGTH_SHORT).show();
			}
		}).doRequest(UrlObject.SENDCODE,params);
	}
}
