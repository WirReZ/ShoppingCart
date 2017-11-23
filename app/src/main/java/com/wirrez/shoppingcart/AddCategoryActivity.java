package com.wirrez.shoppingcart;


import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

public class AddCategoryActivity extends MaterialDialog.Builder {
    public AddCategoryActivity(@NonNull final Context context, MaterialDialog.SingleButtonCallback posCallBack, DialogInterface.OnCancelListener negCallBack) {
        super(context);
        View contv = LayoutInflater.from(context).inflate(R.layout.add_category, null);

        this.title(R.string.drawer_add_category)
                .customView(contv, true)
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .onPositive(posCallBack).cancelListener(negCallBack);
    }
}

