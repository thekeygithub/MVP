package com.ebao.hospitaldapp.rest.base.result;

import com.ebao.hospitaldapp.rest.base.enums.Exceptions;
import com.ebao.hospitaldapp.utils.JsonUtils;

import java.io.IOException;
import lombok.Getter;
import lombok.Setter;

public class JsonRESTResult{

    public JsonRESTResult(){}

    public JsonRESTResult(Object o){
        this.returnObj = o;
    }

    public JsonRESTResult(Exceptions exception, Object o) {
        this.message = exception.getLabel();
        this.statusCode = exception.getValue();
        this.returnObj = o;
    }

    public JsonRESTResult(Exceptions exception) {
        this.message = exception.getLabel();
        this.statusCode = exception.getValue();
    }

    public JsonRESTResult(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Getter @Setter private int statusCode = 200;
    @Getter @Setter private Object returnObj;
    @Getter @Setter private String message = "成功";

    private final int SUCCESS = 200;

    public String encode()
    {
        String json="";
        try {
            json = JsonUtils.toJson(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public boolean isSuccess(){
        return statusCode == SUCCESS ? true : false;
    }
}
