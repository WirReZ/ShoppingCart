package com.wirrez.shoppingcart;

import android.graphics.drawable.Drawable;

import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

/**
 * Created by walteda on 19.11.2017.
 */

public class CategoryItem {
    long _id;
    String _name;
    Drawable _icon;

    public CategoryItem(long id, String name, Drawable icon) {
        this._id = id;
        this._name = name;
        this._icon = icon;
    }

    public PrimaryDrawerItem getPrimaryDrawer() {
        return new PrimaryDrawerItem().withName(this._name).withIcon(this._icon).withIdentifier(this._id);
    }

}
