package com.tokeninc.sardis.application_template.UI.Definitions;

import androidx.annotation.Nullable;

import com.token.components.ListMenuFragment.IListMenuItem;

import java.util.List;

public class MenuItem implements IListMenuItem {
    private String mTitle;
    private List<IListMenuItem> subMenuItemList;
    private int ID;

    public MenuItem(int ID, String title) {
        this(ID, title, null);
    }

    public MenuItem(int ID, String title, @Nullable List<IListMenuItem> subMenuItemList) {
        this.ID = ID;
        this.mTitle = title;
        this.subMenuItemList = subMenuItemList;
    }

    @Override
    public String getName() {
        return mTitle;
    }

    @Nullable
    @Override
    public List<IListMenuItem> getSubMenuItemList() {
        return subMenuItemList;
    }

    @Override
    public int getId() {
        return ID;
    }
}
