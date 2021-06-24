package com.example.grabtutor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.grabtutor.Fragment.CategoryFragment;
import com.example.grabtutor.Fragment.CategoryItemFragment;
import com.example.grabtutor.R;

public class CategoryItemAdapter extends BaseAdapter {

    private Context context;

    public CategoryItemAdapter(Context ctx) {
        context = ctx;
    }

    @Override
    public int getCount() {
        return CategoryItemFragment.Images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.single_category_item, null);
        ImageView imageView = convertView.findViewById(R.id.imageView);
        imageView.setImageResource(CategoryFragment.categoryImages[position]);
        TextView title = convertView.findViewById(R.id.title);
        title.setText(CategoryItemFragment.names[position]);

        Glide.with(context).load(CategoryItemFragment.Images[position]).into(imageView);

        return convertView;
    }
}
