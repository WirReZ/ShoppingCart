package com.wirrez.shoppingcart;


/**
 * Created by walteda on 19.11.2017.
 */

public class Item {
    int _id;
    String _name;
    int _count;
    String _icon;
    Boolean _cross;

    public Item(int id,String name,int count,String icon,Boolean cross)
    {
        this._id = id;
        this._name = name;
        this._count = count;
        this._icon = icon;
        this._cross = cross;
    }

}
