package com.example.mvvmapp.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


/**
 * 句
 */
public class SentenceBean extends BaseBean implements Serializable {
    /**
     * 句子
     */
    @SerializedName("sentenceId")
    private String sentenceId;
    /**
     * 句的图片
     */
    @SerializedName("voice")
    private String voice;
    /**
     * 句子配音
     */
    @SerializedName("voiceOver")
    private String voiceOver;
    @SerializedName("voiceDesc")
    private String voiceDesc;
    /**
     * 句子
     */
    @SerializedName("content")
    private String content;
    @SerializedName("translatel")
    private String translatel;
    /**
     * 不读句的下标  [0-1,3-5,8-22]
     */
    @SerializedName("noReadIndexList")
    private List<String> noReadIndexList;

    public SentenceBean(String sentenceId, String voice, String content) {
        this.sentenceId = sentenceId;
        this.voice = voice;
        this.content = content;
    }

    public String getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(String sentenceId) {
        this.sentenceId = sentenceId;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getVoiceOver() {
        return voiceOver;
    }

    public void setVoiceOver(String voiceOver) {
        this.voiceOver = voiceOver;
    }

    public String getVoiceDesc() {
        return voiceDesc;
    }

    public void setVoiceDesc(String voiceDesc) {
        this.voiceDesc = voiceDesc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTranslatel() {
        return translatel;
    }

    public void setTranslatel(String translatel) {
        this.translatel = translatel;
    }

    public List<String> getNoReadIndexList() {
        return noReadIndexList;
    }

    public void setNoReadIndexList(List<String> noReadIndexList) {
        this.noReadIndexList = noReadIndexList;
    }
}
