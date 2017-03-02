package com.dailyasianage.android.item;



/**
 * Created by optimal on 09-May-16.
 */
public class CatItem {

    private String cat_id;
    private String cat_news_id;

    public CatItem(String cat_id, String cat_news_id) {
        this.cat_id = cat_id;
        this.cat_news_id = cat_news_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_news_id() {
        return cat_news_id;
    }

    public void setCat_news_id(String cat_news_id) {
        this.cat_news_id = cat_news_id;
    }
}
