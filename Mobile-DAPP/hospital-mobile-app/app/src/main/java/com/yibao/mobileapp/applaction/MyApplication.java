package com.yibao.mobileapp.applaction;

import android.app.Application;

import com.tencent.bugly.Bugly;

import java.io.File;

/**
 * Created by Administrator on 2017/7/17.
 */

public class MyApplication extends Application {

   private int faceCheckStatus=0;
   private String token="";
   private String applyPhone="";

   @Override
   public void onCreate() {
      super.onCreate();
//      CrashReport.initCrashReport(this, "662587242e", false);
      Bugly.init(getApplicationContext(), "3705268e9c", false);
   }
   public String getApplyPhone(){
      return this.applyPhone;
   }

   public void setapplyPhone(String v){
      this.applyPhone=v;
   }
   public String getToken(){
      return this.token;
   }

   public void setToken(String v){
      this.token=v;
   }

   public int getFaceCheckStatus(){
      return this.faceCheckStatus;
   }

   public void setFaceCheckStatus(int v){
      this.faceCheckStatus=v;
   }
}
