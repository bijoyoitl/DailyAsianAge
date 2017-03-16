package com.dailyasianage.android.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ripon on 3/13/2017.
 */

public class NewsMain {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("news")
    @Expose
    private List<NewsAll> news = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NewsAll> getNews() {
        return news;
    }

    public void setNews(List<NewsAll> news) {
        this.news = news;
    }
}
