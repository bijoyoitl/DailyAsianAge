package com.dailyasianage.android.item;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by optimal on 09-May-16.
 */
public class News {

    private String cat_id;
    private String id;
    private String heading;
    private String sub_heading;
    private String shoulder;
    private String publish_time;
    private String reporter;
    private String details;
    private String image;
    private int publish_serial;
    private int top_news;
    private int home_slider;
    private int inside_news;

    public News(){

    }

    public News(String cat_id,String id, String heading, String sub_heading, String shoulder, String publish_time, String reporter, String details, String image, int publish_serial,int top_news,int home_slider, int inside_news) {
       this.cat_id=cat_id;
        this.id = id;
        this.heading = heading;
        this.sub_heading = sub_heading;
        this.shoulder = shoulder;
        this.publish_time = publish_time;
        this.reporter = reporter;
        this.details = details;
        this.image = image;
        this.publish_serial = publish_serial;
        this.top_news = top_news;
        this.home_slider = home_slider;
        this.inside_news = inside_news;
    }


    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSub_heading() {
        return sub_heading;
    }

    public void setSub_heading(String sub_heading) {
        this.sub_heading = sub_heading;
    }

    public String getShoulder() {
        return shoulder;
    }

    public void setShoulder(String shoulder) {
        this.shoulder = shoulder;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
       // Log.e("News.java","details String : "+details);
        this.details = details;
      //  Log.e("News.java","details : "+this.details);

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public int getPublish_serial() {
        return publish_serial;
    }

    public int getTop_news() {
        return top_news;
    }

    public void setTop_news(int top_news) {
        this.top_news = top_news;
    }

    public int getHome_slider() {
        return home_slider;
    }

    public void setHome_slider(int home_slider) {
        this.home_slider = home_slider;
    }

    public int getInside_news() {
        return inside_news;
    }

    public void setInside_news(int inside_news) {
        this.inside_news = inside_news;
    }
}
