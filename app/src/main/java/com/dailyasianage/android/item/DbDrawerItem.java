package com.dailyasianage.android.item;

/**
 * Created by optimal on 25-Jun-16.
 */
public class DbDrawerItem {
    private String cat_id;
    private String cat_name;
    private int cat_image;
    private String cat_imag;
    private String level;
    private int order;

    public DbDrawerItem() {
    }


    public DbDrawerItem(String catId, String title, String s, String level, int order) {
        this.cat_id = catId;
        this.cat_name = title;
        this.cat_imag = s;
        this.level=level;
        this.order=order;
    }

    public DbDrawerItem(String cat_id) {
        this.cat_id = cat_id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCat_imag() {
        return cat_imag;
    }

    public void setCat_imag(String cat_imag) {
        this.cat_imag = cat_imag;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public int getCat_image() {
        return cat_image;
    }

    public void setCat_image(int cat_image) {
        this.cat_image = cat_image;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String Output(){
        String cat_id = " ";
        cat_id = this.cat_id;
        return cat_id;
    }
}
