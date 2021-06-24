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
import com.example.grabtutor.Fragment.HomeFragment;
import com.example.grabtutor.R;

public class CategoryAdapter extends BaseAdapter {

    private Context context;

    public CategoryAdapter(Context ctx) {
        context = ctx;
    }

    @Override
    public int getCount() {
        return CategoryFragment.categoryImages.length;
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
        title.setText(CategoryFragment.categoryNames[position]);

        Glide.with(context).load(CategoryFragment.categoryImages[position]).into(imageView);

        return convertView;
    }
}
