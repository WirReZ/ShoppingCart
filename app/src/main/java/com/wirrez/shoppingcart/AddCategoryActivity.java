package com.wirrez.shoppingcart;


import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;

public class AddCategoryActivity extends MaterialDialog.Builder {
    public AddCategoryActivity(@NonNull Context context, MaterialDialog.SingleButtonCallback posCallBack, DialogInterface.OnCancelListener negCallBack) {
        super(context);
        this.title(R.string.drawer_add_category)
                .customView(R.layout.add_category,true)
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .onPositive(posCallBack).cancelListener(negCallBack);
    }
}


       /*this.title(R.string.drawer_add_category)
                .customView(R.layout.add_category,true)
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(getContext(),"test", Toast.LENGTH_SHORT).show();
                    }
                })
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                    }
                });

/*

        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .title(R.string.drawer_add_category)
                .customView(R.layout.add_category,true)
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(getContext(),"test", Toast.LENGTH_SHORT).show();
                    }
                })
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                    }
                })
                .build();*/



   /* public AddCategoryActivity(Context ctx)
    {
        mContext = ctx;
    }
    public void show() {
        MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                .title(R.string.drawer_add_category)
                .customView(R.layout.add_category,true)
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(mContext,"test",Toast.LENGTH_SHORT).show();
                    }
                })
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                    }
                })
                .build();
        dialog.show();
    }*/
