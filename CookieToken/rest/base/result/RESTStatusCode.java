package com.ebao.hospitaldapp.rest.base.result;


public interface RESTStatusCode {
    // HTTP相关状态码
    int OK = 200;
    int BAD_REQUEST = 400;
    int NOT_FOUND = 404;
    int ERROR = 500;

    // 业务相关状态码
    int SUCCESS = 0;
    int FAIL = -1;

}
