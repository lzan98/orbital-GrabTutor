package com.example.grabtutor.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.grabtutor.Activity.MainActivity;
import com.example.grabtutor.Adapter.CategoryAdapter;
import com.example.grabtutor.R;


public class CategoryFragment extends Fragment implements View.OnClickListener {
    private ImageView backBtn;
    private RelativeLayout programming;
    private RelativeLayout fitness;

    public static int categoryImages[] = {R.drawable.fitness, R.drawable.music, R.drawable.programming, R.drawable.writing,
    R.drawable.design, R.drawable.data};
    public static String categoryNames[] = {"Fitness", "Music", "Programming", "Writing", "Graphic Design", "Databases"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        backBtn = view.findViewById(R.id.backBtn);
        programming = view.findViewById(R.id.programming);
        fitness = view.findViewById(R.id.fitness);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            }
        });
        programming.setOnClickListener(this);
        fitness.setOnClickListener(this);



        CategoryAdapter adapter = new CategoryAdapter(getActivity());

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.programming:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProgrammingFragment()).commit();
                break;

            case R.id.fitness:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FitnessFragment()).commit();
                break;
        }
    }
}