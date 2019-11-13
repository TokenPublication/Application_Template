package com.tokeninc.sardis.application_template.UI.Definitions;

import com.token.components.ListMenuFragment.IListMenuItem;

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
