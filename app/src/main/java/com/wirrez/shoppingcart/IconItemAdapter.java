package com.wirrez.shoppingcart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDAdapter;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;

/**
 * Created by walteda on 22.11.2017.
 */

class IconItemAdapter extends RecyclerView.Adapter<IconItemAdapter.ButtonVH>implements MDAdapter {

    private final GoogleMaterial.Icon[] items;
    private ItemCallback itemCallback;
    public Context mContext;
    private MaterialDialog dialog;


    public IconItemAdapter(Context ctx,GoogleMaterial.Icon[] items) {
        this.items = items;
        this.mContext = ctx;
    }

    void setCallbacks(ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
    }

    @Override
    public ButtonVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.dialog_customlistitem, parent, false);
        return new ButtonVH(view, this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ButtonVH holder, int position) {
        holder.icon.setIcon(new IconicsDrawable(mContext).icon(GoogleMaterial.Icon.valueOf(items[position].toString())));
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    @Override
    public void setDialog(MaterialDialog dialog) {
        this.dialog = dialog;
    }

    interface ItemCallback {
        void onItemClicked(MaterialDialog dialog,int itemIndex);
    }


    static class ButtonVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView title;
        final IconicsImageView icon;
        final IconItemAdapter adapter;

        ButtonVH(View itemView, IconItemAdapter adapter) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.md_title);
            icon = (IconicsImageView) itemView.findViewById(R.id.md_icon);
            this.adapter = adapter;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (adapter.itemCallback == null) {
                return;
            }
            adapter.itemCallback.onItemClicked(adapter.dialog,getAdapterPosition());

        }
    }
}