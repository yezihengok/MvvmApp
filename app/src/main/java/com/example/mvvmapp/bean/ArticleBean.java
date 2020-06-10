package com.example.mvvmapp.bean;


import com.example.mvvmapp.db.connverter.MaterialReleaseInfoConverter;
import com.example.mvvmapp.db.connverter.ParagrapBeanConvent;
import com.example.mvvmapp.db.connverter.StringConverter;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * 文章
 */

// @SerializedName 注解与 greenDao无关，可以不需要.
@Entity
public class ArticleBean extends BaseBean implements Serializable {
    private static final long serialVersionUID = 8144016205336010992L;

    @Id(autoincrement = true)
    @SerializedName("_id")
    private Long _id;

    @SerializedName("title")
    private String title;
    /**
     * 文章等级
     */
    @SerializedName("level")
    private String level;
    /**
     * 文章id
     */
    @SerializedName("articleId")
    private String articleId;
    /**
     * 文章图片
     */
    @SerializedName("cover")
    private String cover;
    /**
     * 等级关键次
     */
    @Convert(converter = StringConverter.class , columnType = String.class)
    @SerializedName("keyWords")
    private List<String> keyWords;
    /**
     * 段落
     */
    @Convert(converter = ParagrapBeanConvent.class , columnType = String.class)
    @SerializedName("paragraphList")
    private List<ParagrapBean> paragraphList;
    /**
     * 文章显示类型
     */
    @SerializedName("style")
    private String style;
    /**
     * 是否收藏这篇文章
     */
    @SerializedName("collected")
    private boolean collected;
    /**
     * 文章类型
     */
    @SerializedName("contentType")
    private String contentType;

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    //文章类型
//    英文绘本以一级类：enView_category
//    二级类：
//    默认绘本 enView
//    好词好句 enViewSentence
//
//    中文绘本_一级分类 cnView_category
//    二级类：
//    普通绘本 cnView
//    小古文 cnViewProse
//    名言警句 cnViewAphorism
//    好句好段 cnViewSentence
//    章节书 cnViewChapter
//
//    中文韵文_一级分类 cnVerse_category
//    二级类：
//    普通韵文 cnVerse
//    古诗词 cnVerseProse

    /**
     * 总金币
     */
    @SerializedName("totalCredits")
    private int totalCredits;
    /**
     * 将要奖励的金币
     */
    @SerializedName("rewardCredits")
    private int rewardCredits;

    /**
     * 跟读间隔时间
     */
    @SerializedName("recordInterval")
    private double recordInterval;

    @Convert(converter = MaterialReleaseInfoConverter.class , columnType = String.class)
   // @Convert(converter = CommBeanConvent<>.class , columnType = String.class)
    @SerializedName("releaseInfo")
    private MaterialReleaseInfo releaseInfo;//小古文&名言警句 标题来源作者等信息
    /**
     * (目前接口无此字段自己手动设值区分)文章内容的具体细分如   小古文&名言警句cnViewAphorism_cnViewProse
     */
    @SerializedName("category")
    private String category;

    @SerializedName("subject")
    private String subject;//内容语言 CN EN （接口无返回手动设值）


    public MaterialReleaseInfo getReleaseInfo() {
        return releaseInfo;
    }

    public void setReleaseInfo(MaterialReleaseInfo releaseInfo) {
        this.releaseInfo = releaseInfo;
    }

    /**
     * 背景音乐
     */
    @SerializedName("musicOver")
    private String musicOver;

    @Generated(hash = 1048575158)
    public ArticleBean(Long _id, String title, String level, String articleId, String cover,
                       List<String> keyWords, List<ParagrapBean> paragraphList, String style,
                       boolean collected, String contentType, int totalCredits, int rewardCredits,
                       double recordInterval, MaterialReleaseInfo releaseInfo, String category,
                       String subject, String musicOver) {
        this._id = _id;
        this.title = title;
        this.level = level;
        this.articleId = articleId;
        this.cover = cover;
        this.keyWords = keyWords;
        this.paragraphList = paragraphList;
        this.style = style;
        this.collected = collected;
        this.contentType = contentType;
        this.totalCredits = totalCredits;
        this.rewardCredits = rewardCredits;
        this.recordInterval = recordInterval;
        this.releaseInfo = releaseInfo;
        this.category = category;
        this.subject = subject;
        this.musicOver = musicOver;
    }

    @Generated(hash = 392728754)
    public ArticleBean() {
    }


    public String getMusicOver() {
        return musicOver;
    }

    public void setMusicOver(String musicOver) {
        this.musicOver = musicOver;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getRecordInterval() {
        return recordInterval;
    }

    public void setRecordInterval(double recordInterval) {
        this.recordInterval = recordInterval;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    public List<ParagrapBean> getParagraphList() {
        return paragraphList;
    }

    public void setParagraphList(List<ParagrapBean> paragraphList) {
        this.paragraphList = paragraphList;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public int getRewardCredits() {
        return rewardCredits;
    }

    public void setRewardCredits(int rewardCredits) {
        this.rewardCredits = rewardCredits;
    }

    public boolean getCollected() {
        return this.collected;
    }


    public static class MaterialReleaseInfo implements Serializable {
        private static final long serialVersionUID = -7668916722391784402L;
        @SerializedName("author")
        private String author;//作者
        @SerializedName("authorVoice")
        private String authorVoice;
        @SerializedName("source")
        private String source;//来源
        @SerializedName("sourceVoice")
        private String sourceVoice;
        @SerializedName("dynasty")
        private String dynasty;//朝代
        @SerializedName("dynastyVoice")
        private String dynastyVoice;
        @SerializedName("contentLayout")
        private String contentLayout;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthorVoice() {
            return authorVoice;
        }

        public void setAuthorVoice(String authorVoice) {
            this.authorVoice = authorVoice;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getSourceVoice() {
            return sourceVoice;
        }

        public void setSourceVoice(String sourceVoice) {
            this.sourceVoice = sourceVoice;
        }

        public String getDynasty() {
            return dynasty;
        }

        public void setDynasty(String dynasty) {
            this.dynasty = dynasty;
        }

        public String getDynastyVoice() {
            return dynastyVoice;
        }

        public void setDynastyVoice(String dynastyVoice) {
            this.dynastyVoice = dynastyVoice;
        }

        public String getContentLayout() {
            return contentLayout;
        }

        public void setContentLayout(String contentLayout) {
            this.contentLayout = contentLayout;
        }
    }
}
