package com.yibao.mobileapp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;;

import com.yibao.mobileapp.MainActivity;
import com.yibao.mobileapp.R;
import com.yibao.mobileapp.RegisterActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Administrator on 2017/7/17.
 */

public class CommonUtils {
    /**
     * 重新设置图片大小
     *
     * @param bitmap
     * @param newHeight
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int newHeight) {
        if (bitmap == null)
            return null;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Log.e("Jarvis", w + "~" + h);

        float temp = ((float) w) / ((float) h);
        int newWidth = (int) (newHeight * temp);
        float scaleWidth = ((float) newWidth) / w;
        float scaleHeight = ((float) newHeight) / h;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix,
                true);
        return resizedBitmap;
    }
    public static void toLogin(Context context){
        clearUserInfo(context);
        Toast.makeText(context,context.getString(R.string.pls_relogin),Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(context,RegisterActivity.class);
        context.startActivity(intent);
    }
    public static void showMsgDialog(Context context,String title,String msg,String btntxt){
        AlertDialog.Builder builder  = new AlertDialog.Builder(context);
        builder.setTitle(title) ;
        builder.setMessage(msg ) ;
        builder.setPositiveButton(btntxt ,  null );
        builder.show();
    }

    public static AlertDialog getProgressDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AlertDialog);
        // LayoutInflater inflater = (LayoutInflater) context
        // .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // View view = inflater.inflate(R.layout.layout_progress_dialog, null);
        // builder.setView(view);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setContentView(R.layout.layout_progress_view);
        Window window=dialog.getWindow();
        WindowManager.LayoutParams params=window.getAttributes();
        params.width= WindowManager.LayoutParams.MATCH_PARENT;
        params.height= WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        return dialog;
    }

    public static int getStringID(String strName, Context context) {
        return context.getResources().getIdentifier(strName, "string",
                context.getPackageName());
    }
    /**
     * 获取用户数据
     *
     * @param context
     * @return
     */
    public static SharedPreferences getUserShare(Context context) {
        return context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
    }
    public static void saveUserInfo(Context context,String username,String token){
        SharedPreferences sharedPreferences=getUserShare(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("mobile",username);
        editor.putString("token",token);
        editor.commit();
    }
    public static String getMobile(Context context){
        SharedPreferences sharedPreferences=getUserShare(context);
        return sharedPreferences.getString("mobile","");
    }
    public static String getToken(Context context){
        SharedPreferences sharedPreferences=getUserShare(context);
        return sharedPreferences.getString("token","");
    }
    public static void clearUserInfo(Context context){
        SharedPreferences sharedPreferences=getUserShare(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.remove("mobile");
        editor.remove("token");
        editor.commit();
    }

    /**
     * 验证手机号格式
     */
    public static boolean isMobileNO(String mobileNums) {
        String telRegex = "[1]\\d{10}";
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }
    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }



    /**
     * dp转px方法
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp方法
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static void showButtonDialog(Context context, String message,
                                        String title, HashMap<String, DialogInterface.OnClickListener> map) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);
        boolean bool = true;
        boolean bool2=true;
        for (String key : map.keySet()){
            if (bool) {
                builder.setPositiveButton(key, map.get(key));
                bool = false;
            } else if(bool2){
                builder.setNegativeButton(key, map.get(key));
                bool2=false;
            }else{
                builder.setNeutralButton(key, map.get(key));
            }
        }
        builder.setCancelable(false);
        builder.create().show();
    }
    public static  boolean isGifUrl(String url){
        if(url.endsWith(".gif")||url.endsWith(".GIF")){
            return true;
        }
        return false;
    }

    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    public static void shiftLanguage(String sta,Context context){

        if(sta.equals("en")){
            Locale.setDefault(Locale.US);
            Configuration config = context.getApplicationContext().getResources().getConfiguration();
            config.setLocale(Locale.US);

//            config.locale = Locale.ENGLISH;
            context.getApplicationContext().getResources().updateConfiguration(config
                    , context.getResources().getDisplayMetrics());
        }else{
            Locale.setDefault(Locale.CHINESE);
            Configuration config = context.getApplicationContext().getResources().getConfiguration();
            config.setLocale(Locale.CHINESE);
//            config.locale = Locale.CHINESE;
            context.getApplicationContext().getResources().updateConfiguration(config
                    , context.getResources().getDisplayMetrics());
        }
    }
}
