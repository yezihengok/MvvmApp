package com.example.commlib.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 返回值 返回值 data节点为 object
 */
public class ResultBean<T> implements Serializable {
    //    {
//        "code": 5,
//            "data": object,
//            "msg": "未知司机"
//    }
    public ResultBean(int code, String msg) {
        this.errorCode = code;
        this.errorMsg = msg;
    }
    private int errorCode;
    private T data;


    private String errorMsg;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
