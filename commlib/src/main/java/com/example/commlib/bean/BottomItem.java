package com.example.commlib.bean;

import java.io.Serializable;

public class BottomItem implements Serializable {

    private String name;
    private String code;
    private boolean isChoose;
    public BottomItem() {

    }
    public BottomItem(String name, String code) {
        this.name = name;
        this.code = code;
    }
    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
