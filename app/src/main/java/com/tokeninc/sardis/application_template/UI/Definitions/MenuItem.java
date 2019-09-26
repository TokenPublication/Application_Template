package com.tokeninc.sardis.application_template.UI.Definitions;

public class MenuItem {
    private String mTitle;
    private int mIconId;
    public MenuItem() {
    }

    public MenuItem(String title, int iconSrc) {
        this.mTitle = title;
        this.mIconId = iconSrc;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public int getIconId() {
        return mIconId;
    }

    public void setIconId(int mIconId) {
        this.mIconId = mIconId;
    }
}
