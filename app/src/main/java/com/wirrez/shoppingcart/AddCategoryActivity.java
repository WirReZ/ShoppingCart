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

public class AddCategoryActivity extends MaterialDialog.Builder {
    public String _icon;
    public AddCategoryActivity(@NonNull final Context context, MaterialDialog.SingleButtonCallback posCallBack, DialogInterface.OnCancelListener negCallBack) {
        super(context);

        final View contv = LayoutInflater.from(context).inflate(R.layout.add_category, null);
        Button btnImg = contv.findViewById(R.id.btnSelectIcon);
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final MaterialDialog.Builder dialog;
            final IconItemAdapter adapter =   new IconItemAdapter(context,GoogleMaterial.Icon.values());
                        adapter.setCallbacks(new IconItemAdapter.ItemCallback() {
                      @Override
                      public void onItemClicked(int itemIndex) {
                          TextView icon = contv.findViewById(R.id.StrIcon);
                          icon.setText(GoogleMaterial.Icon.values()[itemIndex].toString());
                      }
                  });
                dialog = new MaterialDialog.Builder(context).title(R.string.select_img).adapter(adapter,null);
                dialog.show();
            }
        });

      //  IconicsImageView imageView = contv.findViewById(R.id.icon);
     //   imageView.setIcon(new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_collection_item));
        //ImageDrawa
       // imageView.setImageDrawable( new ImageDrawable()  );
        this.title(R.string.drawer_add_category)
                .customView(contv, true)
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .onPositive(posCallBack).cancelListener(negCallBack);
    }
}

