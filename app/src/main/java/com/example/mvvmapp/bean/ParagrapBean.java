package com.example.mvvmapp.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 段落
 */
public class ParagrapBean extends BaseBean implements Serializable {
    private static final long serialVersionUID = -7031853677375013655L;
    @SerializedName("cover")
    private String cover;  //段落图片
    @SerializedName("sentenceList")
    private List<SentenceBean> sentenceList;//句子列表
    @SerializedName("axis")
    private List<String> axis;
    @SerializedName("width")
    private String width;
    @SerializedName("coverPosition")
    private String coverPosition;//  up  down  图片位置
    @SerializedName("readId")// modify morong
    private String readId;

    public void setReadId(String readId) {
        this.readId = readId;
    }

    public String getReadId() {
        return readId;
    }

    public String getCoverPosition() {
        return coverPosition;
    }

    public void setCoverPosition(String coverPosition) {
        this.coverPosition = coverPosition;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<SentenceBean> getSentenceList() {
        return sentenceList;
    }

    public void setSentenceList(List<SentenceBean> sentenceList) {
        this.sentenceList = sentenceList;
    }

    public List<String> getAxis() {
        return axis;
    }

    public void setAxis(List<String> axis) {
        this.axis = axis;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
