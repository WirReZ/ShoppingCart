package com.wirrez.shoppingcart;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by walteda on 19.11.2017.
 */

public class CustomItemAdapter extends RecyclerView.Adapter<CustomItemAdapter.ViewHolder> {
    private ArrayList<Item> values;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtName;
        public TextView txtCount;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtName = (TextView) v.findViewById(R.id.name);
            txtCount = (TextView) v.findViewById(R.id.count);
        }
    }

    public CustomItemAdapter(ArrayList<Item> list) {
        this.values = list;
    }

    @Override
    public CustomItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CustomItemAdapter.ViewHolder holder, int position) {
        final Item itm = values.get(position);
        holder.txtName.setText(itm._name);
        holder.txtCount.setText(String.valueOf(itm._count) + " "+String.valueOf(itm._unit));
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
