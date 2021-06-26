package com.example.grabtutor.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.grabtutor.Activity.MainActivity;
import com.example.grabtutor.Adapter.CategoryAdapter;
import com.example.grabtutor.R;


public class CategoryFragment extends Fragment {
    private GridView gridView;
    public static int categoryImages[] = {R.drawable.fitness, R.drawable.music, R.drawable.programming, R.drawable.writing,
    R.drawable.design, R.drawable.data};
    public static String categoryNames[] = {"Fitness", "Music", "Programming", "Writing", "Graphic Design", "Databases"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        gridView = view.findViewById(R.id.gridView);

        CategoryAdapter adapter = new CategoryAdapter(getActivity());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FitnessFragment()).commit();
                }
                else if (position == 1) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MusicFragment()).commit();
                }
                else if (position == 2) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProgrammingFragment()).commit();
                }
                else if (position == 3) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WritingFragment()).commit();
                }
                else if (position == 4) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DesignFragment()).commit();
                }
                else {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DataBaseFragment()).commit();
                }
                }
        });
        return view;
    }
}