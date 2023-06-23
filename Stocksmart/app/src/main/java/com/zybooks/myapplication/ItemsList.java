package com.zybooks.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemsList extends BaseAdapter {

    private final Activity context;
    // private PopupWindow popwindow;
    ArrayList<Item> items;
    InventoryDatabase db;
    private boolean isLoggedIn;

    public ItemsList(Activity context, ArrayList<Item> items, InventoryDatabase db, boolean isLoggedIn) {
        this.context = context;
        this.items = items;
        this.db = db;
        this.isLoggedIn = isLoggedIn;
    };

    public static class ViewHolder {
        TextView textViewItemId;
        TextView textViewItemDesc;
        TextView textViewItemQty;
        TextView textViewItemUnit;
        ImageButton editBtn;
        ImageButton deleteBtn;
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder vh;

        if (convertView == null) {
            vh = new ViewHolder();
            row = inflater.inflate(R.layout.item_row_template, null, true);

            vh.editBtn = row.findViewById(R.id.editButton);
            vh.textViewItemId = row.findViewById(R.id.textViewItemId);
            vh.textViewItemDesc = row.findViewById(R.id.textViewItemDesc);
            vh.textViewItemQty = row.findViewById(R.id.textViewItemQty);
            vh.textViewItemUnit = row.findViewById(R.id.textViewItemUnit);
            vh.deleteBtn = row.findViewById(R.id.deleteButton);

            row.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        };

        vh.textViewItemId.setText("" + items.get(position).getId());
        vh.textViewItemDesc.setText(items.get(position).getDesc());
        vh.textViewItemQty.setText(items.get(position).getQty());
        vh.textViewItemUnit.setText(items.get(position).getUnit());

        final int positionPopup = position;

        // vh.editBtn.setOnClickListener(view -> editPopup(positionPopup));

        vh.deleteBtn.setOnClickListener(view -> {

            // Only allow deletion of the item if a user is logged in
            if (isLoggedIn) {
                db.deleteItem(items.get(positionPopup));

                items = (ArrayList<Item>) db.getAllItems();
                notifyDataSetChanged();

                Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "You must be logged in to delete items.", Toast.LENGTH_SHORT).show();
            };
        });

        return row;
    };

    public Object getItem(int position) {
        return position;
    };

    public long getItemId(int position) {
        return position;
    };

    public int getCount() {
        return items.size();
    };

    public void setItems(ArrayList<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    };
};