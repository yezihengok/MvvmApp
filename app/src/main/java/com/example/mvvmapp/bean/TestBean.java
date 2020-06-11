package com.example.mvvmapp.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yzh on 2020/6/9 16:54.
 */
@Entity
public class TestBean {
    @Id(autoincrement = true)
   // @SerializedName("_id")
    private Long _id;

 //   @SerializedName("title")
    private String title;
    /**
     * 文章等级
     */
  //  @SerializedName("level")
    private String level;

    private String name;


    public TestBean(String title, String level, String name) {
        this.title = title;
        this.level = level;
        this.name = name;
    }

    @Generated(hash = 1216338401)
    public TestBean(Long _id, String title, String level, String name) {
        this._id = _id;
        this.title = title;
        this.level = level;
        this.name = name;
    }

    @Generated(hash = 2087637710)
    public TestBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
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
}
