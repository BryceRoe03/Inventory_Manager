package com.example.project3_broe2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class InvAdapter extends ArrayAdapter<Item> {
    private List<Item> inventory;
    private Context context;

    public InvAdapter(Context context, List<Item> inventory) {
        super(context, 0, inventory);
        this.context = context;
        this.inventory = inventory;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.datalist, parent, false);
        }

        Item currItem = inventory.get(position);
        TextView name = convertView.findViewById(R.id.name);
        TextView price = convertView.findViewById(R.id.price);
        TextView stock = convertView.findViewById(R.id.stock);

        name.setText(currItem.getItemName());
        price.setText(String.valueOf(currItem.getItemPrice()));
        stock.setText(String.valueOf(currItem.getItemStock()));

        return convertView;
    }
}
