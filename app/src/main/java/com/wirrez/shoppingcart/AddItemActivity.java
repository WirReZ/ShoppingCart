package com.wirrez.shoppingcart;


import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;

public class AddItemActivity extends MaterialDialog.Builder {


    public AddItemActivity(@NonNull Context context, MaterialDialog.SingleButtonCallback posCallBack, MaterialDialog.SingleButtonCallback negCallBack) {
        super(context);
        this.title(R.string.drawer_add_item)
                .customView(R.layout.add_item, true)
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .onPositive(posCallBack).onNegative(negCallBack);
    }
}
