package com.yibao.hospitalapp.applaction;

import android.app.Application;

import com.tencent.bugly.Bugly;

import java.io.File;

/**
 * Created by Administrator on 2017/7/17.
 */

public class MyApplication extends Application {
   @Override
   public void onCreate() {
      super.onCreate();
//      CrashReport.initCrashReport(this, "662587242e", false);
      Bugly.init(getApplicationContext(), "3705268e9c", false);
   }
}
