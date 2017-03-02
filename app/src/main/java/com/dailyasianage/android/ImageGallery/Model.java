package com.dailyasianage.android.ImageGallery;

/**
 * Created by bijoy on 9/4/16.
 */
public class Model {
    private String title;
    private String caption;
    private String image;

    public Model() {
    }

    public Model(String title, String caption, String image) {
        this.title = title;
        this.caption = caption;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
