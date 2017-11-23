package com.wirrez.shoppingcart;

import android.graphics.drawable.Drawable;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

/**
 * Created by walteda on 19.11.2017.
 */

public class CategoryItem {
    long _id;
    String _name;
    String _count;
    GoogleMaterial.Icon _icon;

    public CategoryItem(long id, String name, GoogleMaterial.Icon icon,long count) {
        this._id = id;
        this._name = name;
        this._icon = icon;
        this._count = String.valueOf(count);
    }

    public PrimaryDrawerItem getPrimaryDrawer() {
        return new PrimaryDrawerItem().withName(this._name+" ("+_count+")").withIcon(this._icon).withIdentifier(this._id).withTag(this._name);
    }

}
