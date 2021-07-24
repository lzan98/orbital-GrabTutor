package com.example.grabtutor.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.grabtutor.R;


public class CategoryFragment extends Fragment implements View.OnClickListener {
    private ImageView searchBtn;
    private RelativeLayout programming, fitness, writing, music, design, dataAnalytics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        searchBtn = view.findViewById(R.id.action_search);
        programming = view.findViewById(R.id.programming);
        fitness = view.findViewById(R.id.fitness);
        writing = view.findViewById(R.id.writing);
        music = view.findViewById(R.id.music);
        design = view.findViewById(R.id.design);
        dataAnalytics = view.findViewById(R.id.dataAnalytics);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
            }
        });
        programming.setOnClickListener(this);
        fitness.setOnClickListener(this);
        writing.setOnClickListener(this);
        music.setOnClickListener(this);
        design.setOnClickListener(this);
        dataAnalytics.setOnClickListener(this);



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

            case R.id.writing:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WritingFragment()).commit();
                break;

            case R.id.music:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MusicFragment()).commit();
                break;

            case R.id.design:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DesignFragment()).commit();
                break;

            case R.id.dataAnalytics:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DataAnalyticsFragment()).commit();
                break;
        }
    }
}