package com.wirrez.shoppingcart;


/**
 * Created by walteda on 19.11.2017.
 */

public class Item {
    long _id;
    String _name;
    int _count;
    String _unit;
    String _icon;
    Boolean _cross;

    public Item(long id, String name, int count, String unit, String icon, Boolean cross) {
        this._id = id;
        this._name = name;
        this._count = count;
        this._unit = unit;
        this._icon = icon;
        this._cross = cross;
    }

}
