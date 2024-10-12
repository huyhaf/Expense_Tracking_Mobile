package com.map.hanhathuy.mobileapp.demo2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.map.hanhathuy.mobileapp.demo2.R;

public class IconSpinnerAdapter extends ArrayAdapter<Integer> {

    private final Context context;
    private final Integer[] iconIds;

    public IconSpinnerAdapter(Context context, Integer[] iconIds) {
        super(context, R.layout.spinner_item_icon, iconIds);
        this.context = context;
        this.iconIds = iconIds;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item_icon, parent, false);
        }

        ImageView iconView = convertView.findViewById(R.id.icon);
        iconView.setImageResource(iconIds[position]);

        return convertView;
    }
}