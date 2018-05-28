package com.yibao.hospitalapp.okhttp;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/7/13.
 */

public class CommonHttp {
    private CommonHttpCallback callback;
    public static String token;
    private boolean isfile=false;
    private Context mContext;
    public CommonHttp(Context mContext,CommonHttpCallback callback){
        this.callback=callback;
        this.isfile=isfile;
        this.mContext=mContext;
    }
    private String TAG="OKHTTP";
    public void doRequest(Object... objects) {
        String url;
        HashMap<String, String> params;
        HashMap<String, File> files;
        Request request=null;
        Request.Builder requestBuiler = new Request.Builder();
//        requestBuiler.header("X-Requested-With","XMLHttpRequest");

//        if(MyApplication.cookie!=null){
//            requestBuiler.header("Cookie", MyApplication.cookie);
//        }
//        if(cookie!=null&&!cookie.equals("")){
//            requestBuiler.header("Cookie",cookie);
//        }
        if(token!=null&&token.length()>0){
            requestBuiler.header("TOKEN",token);
        }


        switch (objects.length) {
            case 1:
                url = (String) objects[0];
                        request=requestBuiler.url(url)
                        .build();
                break;
            case 2:
                url = (String) objects[0];
                FormBody.Builder builder = new FormBody.Builder();
                params = (HashMap<String, String>) objects[1];
                for (HashMap.Entry<String, String> entry :
                        params.entrySet()) {
                    builder.add(entry.getKey(), entry.getValue());
                }

                request = requestBuiler.url(url).post(builder.build()).build();
                break;
            case 3:
                url = (String) objects[0];
                MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                MediaType type = MediaType.parse("application/octet-stream");
                params = (HashMap<String, String>) objects[1];
                for (HashMap.Entry<String, String> entry : params.entrySet()
                        ) {
                    multipartBuilder.addFormDataPart(entry.getKey(), entry.getValue());
                }

                files = (HashMap<String, File>) objects[2];
                for (HashMap.Entry<String, File> entry :
                        files.entrySet()) {
                    RequestBody fileBody = RequestBody.create(type, entry.getValue());
                    multipartBuilder.addFormDataPart(entry.getKey(), entry.getValue().getName(), fileBody);
                }
                request = requestBuiler.url(url).post(multipartBuilder.build()).build();
                break;
        }
        final OkHttpClient httpClient = new OkHttpClient.Builder().readTimeout(300, TimeUnit.SECONDS).addInterceptor(new LogInterceptor()).build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("response.code","error error error ");
                mHandler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                cookie=response.header("Set-Cookie","").toString();
//                xresponsejson=response.header("X-Responded-JSON","").toString();
//                if(cookie!=null&&!cookie.equals(""))
//                MyApplication.cookie=cookie;
                Log.e("response.code",response.code()+","+response.body());

                Message msg=new Message();
                msg.obj=response;
                mHandler.sendMessage(msg);
            }
        });
    }
    Handler mHandler=new Handler(new Handler.Callback(){

        @Override
        public boolean handleMessage(Message message) {
            if(message.what==-1){
                callback.requestAbnormal(-1);
             return false;
            }
                Response response = (Response) message.obj;

                try {
                    if (response.code() == 200 || response.code() == 204) {

                        if (response.code() == 401) {
                            callback.requestAbnormal(response.code());
                        }
                       String body=response.body().string();

                        if( getCode(body)==200) {
                            callback.requestSeccess(body);

                        }else{
                            callback.requestFail(getMsg(body));
                        }
//                        }
                    } else if (response.code() == 500) {
//                        Toast.makeText(mContext, getMsg(response.body().string()), Toast.LENGTH_SHORT).show();
                        callback.requestFail(getMsg(response.body().string()));
                    } else {

                        callback.requestAbnormal(response.code());
                    }
                } catch (IOException e) {
                    callback.requestAbnormal(-2);
                }
            return false;
        }
    });
    private String getMsg(String json){
        String str=null;
        try {
            JSONObject object=new JSONObject(json);
            str=object.getString("message");
        } catch (JSONException e) {
            return json;
        }
        return str;
    }
    private int getCode(String json){
        int i=-1;
        if(json==null||json.equals("")){
            return -1;
        }
        try {
            JSONObject object=new JSONObject(json);
            i=object.getInt("statusCode");
        } catch (JSONException e) {
            return -1;
        }
        return i;
    }
//    private String getCode(String json){
//        String code=null;
//        if(json==null||json.equals("")){
//            return null;
//        }
//        try {
//            JSONObject object=new JSONObject(json);
//            code=object.getString("statusCode");
//        } catch (JSONException e) {
//            return json;
//        }
//        return code;
//    }
    public static String getBodyObj(String json){
        String body=null;
        if(json==null||json.equals("")){
            return null;
        }
        try {
            JSONObject object=new JSONObject(json);
            body=object.getJSONObject("returnObj").toString();
        } catch (JSONException e) {
            return json;
        }
        return body;
    }
    public static String getBodyArr(String json){
        String body=null;
        if(json==null||json.equals("")){
            return null;
        }
        try {
            JSONObject object=new JSONObject(json);
            body=object.getJSONArray("returnObj").toString();
        } catch (JSONException e) {
            return json;
        }
        return body;
    }
    private class LogInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Log.v(TAG, "request:" + request.toString());
            long t1 = System.nanoTime();
            Response response = chain.proceed(chain.request());
            long t2 = System.nanoTime();
            Log.v(TAG, String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            Log.i(TAG, "response body:" + content);
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    }

}
