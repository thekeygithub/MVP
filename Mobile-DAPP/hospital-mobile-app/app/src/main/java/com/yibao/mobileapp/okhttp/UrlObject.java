package com.yibao.mobileapp.okhttp;

import com.yibao.mobileapp.activity.BaseActivity;

/**
 * Created by Administrator on 2017/7/12.
 */

public class UrlObject {
   public static final String BASEURL = "http://222.128.14.106:2989/hospital-dapp/terminal/api/";
   public static final String THEKBASEURL = "http://222.128.14.106:2989/HollyBlockChain/";
   public static final String INITIALIZATION=BASEURL+"initialization";
   public static final  String GETUSERINFO=BASEURL+"getUserInfo";
   public static final String  GETUSERINFOBYID=BASEURL+"getUserInfoById";
   public static final String GETUSERINFODIRECTLY=BASEURL+"getUserInfoDirectly";
   public static final  String VERIFYUSER2=BASEURL+"verifyUser";
   public static final String PAYBYPASSWORD=BASEURL+"payByPassword";
   public static final String EXCHANGETOTKY=BASEURL+"exchangeToTKY";
   public static final String CHECKBALANCE=BASEURL+"checkBalance";
   public static final String PAYBYPICTURE=BASEURL+"payByPicture";
   public static final String SENDCODE=BASEURL+"sendCode";
   public static final String LOGIN= THEKBASEURL+"InfoController/login";
 //  public static final String LOGIN= BASEURL+"login";
   public static final String GETTREATMENTINFO=BASEURL+"getTreatmentInfo";
}
