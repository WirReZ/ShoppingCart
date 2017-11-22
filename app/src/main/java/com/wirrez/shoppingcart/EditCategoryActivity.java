package com.wirrez.shoppingcart;

import android.content.Context;
import android.support.annotation.NonNull;


import com.afollestad.materialdialogs.MaterialDialog;


public class EditCategoryActivity extends MaterialDialog.Builder  {

        public EditCategoryActivity(@NonNull Context context, MaterialDialog.SingleButtonCallback posCallBack, MaterialDialog.SingleButtonCallback negCallBack) {
            super(context);

            this.title(R.string.drawer_edit_category)
                    .customView(R.layout.edit_category, true)
                    .positiveText(R.string.save)
                    .negativeText(R.string.cancel)
                    .onPositive(posCallBack).onNegative(negCallBack);


        }
}

