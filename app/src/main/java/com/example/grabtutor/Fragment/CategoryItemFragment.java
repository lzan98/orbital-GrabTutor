package com.example.grabtutor.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.grabtutor.Adapter.CategoryAdapter;
import com.example.grabtutor.Adapter.CategoryItemAdapter;
import com.example.grabtutor.R;


public class CategoryItemFragment extends Fragment {
    private GridView gridView;
    public static int Images[] = {R.drawable.data};
    public static String names[] = {"test"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category_item, container, false);
        gridView = view.findViewById(R.id.gridView);
        CategoryItemAdapter adapter = new CategoryItemAdapter(getActivity());
        gridView.setAdapter(adapter);
        return view;
    }
}