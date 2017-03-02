package com.dailyasianage.android.item;

/**
 * Created by optimal on 24-Apr-16.
 */
public class DrawerListItem {
    private int iconImage;
    private String listDrawer;

    public DrawerListItem(int iconImage, String listDrawer) {
        this.iconImage = iconImage;
        this.listDrawer = listDrawer;

    }

    public int getIconImage() {
        return iconImage;
    }

    public void setIconImage(int iconImage) {
        this.iconImage = iconImage;
    }

    public String getListDrawer() {
        return listDrawer;
    }

    public void setListDrawer(String listDrawer) {
        this.listDrawer = listDrawer;
    }

}
