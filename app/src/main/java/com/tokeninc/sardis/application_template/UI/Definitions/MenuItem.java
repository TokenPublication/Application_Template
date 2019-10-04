package com.tokeninc.sardis.application_template.UI.Definitions;

public class MenuItem implements IListMenuItem {
    private String mTitle;

    public MenuItem(String title) {
        this.mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getName() {
        return mTitle;
    }
}
