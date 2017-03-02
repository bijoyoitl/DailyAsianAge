package com.dailyasianage.android.item;

/**
 * Created by optimal on 15-Jun-16.
 */
public class HSItem {
    String name;
    String catId;
    int image;

    public HSItem(String name,String catId, int image) {
        this.name = name;
        this.catId = catId;
        this.image = image;
    }


    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
