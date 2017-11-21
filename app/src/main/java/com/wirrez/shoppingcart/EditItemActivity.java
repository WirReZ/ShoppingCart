package com.wirrez.shoppingcart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

public class EditItemActivity extends MaterialDialog.Builder  {

    public EditItemActivity(@NonNull Context context, MaterialDialog.SingleButtonCallback posCallBack, MaterialDialog.SingleButtonCallback negCallBack) {
        super(context);

        this.title(R.string.drawer_edit_item)
                .customView(R.layout.edit_item, true)
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .onPositive(posCallBack).onNegative(negCallBack);


    }
}
