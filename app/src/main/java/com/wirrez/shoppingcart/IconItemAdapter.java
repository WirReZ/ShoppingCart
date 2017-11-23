package com.wirrez.shoppingcart;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.view.IconicsImageView;

/**
 * Created by walteda on 22.11.2017.
 */

class IconItemAdapter extends RecyclerView.Adapter<IconItemAdapter.ButtonVH> {

    private final GoogleMaterial.Icon[] items;
    private ItemCallback itemCallback;


    public IconItemAdapter(GoogleMaterial.Icon[] items) {
        this.items = items;
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
        holder.title.setText(items[position].toString());
        // holder.icon.setIcon( items[position] );
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    interface ItemCallback {
        void onItemClicked(int itemIndex);
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
            adapter.itemCallback.onItemClicked(getAdapterPosition());

        }
    }
}