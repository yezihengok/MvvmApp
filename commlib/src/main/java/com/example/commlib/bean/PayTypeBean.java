package com.example.commlib.bean;

/**
 * Created by yang on 2018/10/11.
 */

public class PayTypeBean extends BottomItem{

    /**
     * payLevel : 01
     * payWayCode : 20
     * payWayName : 微信
     * payWayType : prepay
     */

    private String payLevel;
    private String payWayCode;
    private String payWayName;

    public PayTypeBean( String payWayName,String payWayCode) {
        super(payWayName, payWayCode);
        this.payWayCode = payWayCode;
        this.payWayName = payWayName;
    }

    public String getPayLevel() {
        return payLevel;
    }

    public void setPayLevel(String payLevel) {
        this.payLevel = payLevel;
    }

    public String getPayWayCode() {
        return payWayCode;
    }

    public void setPayWayCode(String payWayCode) {
        this.payWayCode = payWayCode;
    }

    public String getPayWayName() {
        return payWayName;
    }

    public void setPayWayName(String payWayName) {
        this.payWayName = payWayName;
    }
}
