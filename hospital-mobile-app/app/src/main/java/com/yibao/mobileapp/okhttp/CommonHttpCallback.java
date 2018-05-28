package com.yibao.mobileapp.okhttp;

import java.io.InputStream;

/**
 * Created by Administrator on 2017/7/13.
 */

public interface CommonHttpCallback {
    void requestSeccess(String json);

    void requestFail(String msg);
    void requestAbnormal(int code);
}
