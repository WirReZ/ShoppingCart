package com.wirrez.shoppingcart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


import com.afollestad.materialdialogs.MaterialDialog;

public class EditItemActivity extends MaterialDialog.Builder  {

    public EditItemActivity(@NonNull Context context, MaterialDialog.SingleButtonCallback posCallBack, MaterialDialog.SingleButtonCallback negCallBack) {
        super(context);

        View contv = LayoutInflater.from(context).inflate(R.layout.edit_item, null);
        AutoCompleteTextView name = (AutoCompleteTextView) contv.findViewById(R.id.name);
        AutoCompleteTextView units = (AutoCompleteTextView) contv.findViewById(R.id.unit);
        Database db = new Database(context);
        ArrayAdapter<String> adapterUsedItems = new ArrayAdapter<String>(context,android.R.layout.select_dialog_item,db.getUsedItems());
        ArrayAdapter<String> adapterUsedUnits = new ArrayAdapter<String>(context,android.R.layout.select_dialog_item,db.getUsedUnits());
        name.setThreshold(1);
        units.setThreshold(1);
        name.setAdapter(adapterUsedItems);
        units.setAdapter(adapterUsedUnits);

        this.title(R.string.drawer_edit_item)
                .customView(contv, true)
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .onPositive(posCallBack).onNegative(negCallBack);


    }
}
