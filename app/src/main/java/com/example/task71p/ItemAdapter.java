package com.example.task71p;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {
    public ItemAdapter(Context context, List<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_row, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.tvTitle))
                .setText(item.getPostType() + " " + item.getDescription());
        ((TextView) convertView.findViewById(R.id.tvDesc))
                .setText(item.getDate() + " at " + item.getLocation());
        return convertView;
    }
}

