package com.wirrez.shoppingcart;


import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;

public class AddCategoryActivity extends MaterialDialog.Builder {
    public AddCategoryActivity(@NonNull Context context, MaterialDialog.SingleButtonCallback posCallBack, DialogInterface.OnCancelListener negCallBack) {
        super(context);
        this.title(R.string.drawer_add_category)
                .customView(R.layout.add_category, true)
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .onPositive(posCallBack).cancelListener(negCallBack);
    }
}

