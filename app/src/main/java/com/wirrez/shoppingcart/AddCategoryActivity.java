package com.wirrez.shoppingcart;


import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;

public class AddCategoryActivity extends MaterialDialog.Builder {
    public AddCategoryActivity(@NonNull final Context context, MaterialDialog.SingleButtonCallback posCallBack, DialogInterface.OnCancelListener negCallBack) {
        super(context);

        final View contv = LayoutInflater.from(context).inflate(R.layout.add_category, null);
        Button btnImg = contv.findViewById(R.id.btnSelectIcon);
        Button btnImgReset = contv.findViewById(R.id.btnDelIcon);
        btnImgReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView icon = contv.findViewById(R.id.StrIcon);
                IconicsImageView ico = contv.findViewById(R.id.md_icon);
                icon.setText("");
                ico.setIcon(null);
            }
        });
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final MaterialDialog.Builder dialog;
            final IconItemAdapter adapter =   new IconItemAdapter(context,GoogleMaterial.Icon.values());
                        adapter.setCallbacks(new IconItemAdapter.ItemCallback() {
                            @Override
                            public void onItemClicked(MaterialDialog dialog, int itemIndex) {
                                TextView icon = contv.findViewById(R.id.StrIcon);
                                IconicsImageView ico = contv.findViewById(R.id.md_icon);
                                ico.setIcon(new IconicsDrawable(context).icon(GoogleMaterial.Icon.valueOf(GoogleMaterial.Icon.values()[itemIndex].toString())));
                                icon.setText(GoogleMaterial.Icon.values()[itemIndex].toString());
                                dialog.dismiss();
                            }
                  });
                dialog = new MaterialDialog.Builder(context).title(R.string.select_img).adapter(adapter,null);
                dialog.show();

            }
        });

        this.title(R.string.drawer_add_category)
                .customView(contv, true)
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .onPositive(posCallBack).cancelListener(negCallBack);
    }
}

