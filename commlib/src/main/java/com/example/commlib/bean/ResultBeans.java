package com.example.commlib.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 返回值 data节点为 list
 */
public class ResultBeans<T> implements Serializable {
    //    {
//        "code": 5,
//            "data": List<T>,
//            "msg": "未知司机"
//    }
    public ResultBeans(int code, String msg) {
        this.errorCode = code;
        this.errorMsg = msg;
    }
    private int errorCode;
    private List<T> data;


    private String errorMsg;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
