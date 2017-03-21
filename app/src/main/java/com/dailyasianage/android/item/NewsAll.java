package com.dailyasianage.android.item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ripon on 3/13/2017.
 */

public class NewsAll {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("shoulder")
    @Expose
    private String shoulder;
    @SerializedName("publish_time")
    @Expose
    private String publishTime;
    @SerializedName("publish_serial")
    @Expose
    private String publishSerial;
    @SerializedName("n_top_news")
    @Expose
    private String nTopNews;
    @SerializedName("n_home_slider")
    @Expose
    private String nHomeSlider;
    @SerializedName("n_inside_news")
    @Expose
    private String nInsideNews;
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("sub_heading")
    @Expose
    private String subHeading;
    @SerializedName("reporter")
    @Expose
    private String reporter;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("image")
    @Expose
    private String image;

    public NewsAll() {
    }

    public NewsAll(String id, String catId, String shoulder, String publishTime, String publishSerial, String nTopNews, String nHomeSlider, String nInsideNews, String heading, String subHeading, String reporter, String details, String image) {
        this.id = id;
        this.catId = catId;
        this.shoulder = shoulder;
        this.publishTime = publishTime;
        this.publishSerial = publishSerial;
        this.nTopNews = nTopNews;
        this.nHomeSlider = nHomeSlider;
        this.nInsideNews = nInsideNews;
        this.heading = heading;
        this.subHeading = subHeading;
        this.reporter = reporter;
        this.details = details;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getShoulder() {
        return shoulder;
    }

    public void setShoulder(String shoulder) {
        this.shoulder = shoulder;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublishSerial() {
        return publishSerial;
    }

    public void setPublishSerial(String publishSerial) {
        this.publishSerial = publishSerial;
    }

    public String getNTopNews() {
        return nTopNews;
    }

    public void setNTopNews(String nTopNews) {
        this.nTopNews = nTopNews;
    }

    public String getNHomeSlider() {
        return nHomeSlider;
    }

    public void setNHomeSlider(String nHomeSlider) {
        this.nHomeSlider = nHomeSlider;
    }

    public String getNInsideNews() {
        return nInsideNews;
    }

    public void setNInsideNews(String nInsideNews) {
        this.nInsideNews = nInsideNews;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSubHeading() {
        return subHeading;
    }

    public void setSubHeading(String subHeading) {
        this.subHeading = subHeading;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
