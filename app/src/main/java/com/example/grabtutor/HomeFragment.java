package com.example.grabtutor;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView featuredRecycler;
    RecyclerView.Adapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home2, container, false);

        featuredRecycler = view.findViewById(R.id.featured_recycler);

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();
        featuredLocations.add(new FeaturedHelperClass(R.drawable.ic_add, "Test","abdedfgh"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.ic_add, "Test","abdedfgh"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.ic_add, "Test","abdedfgh"));

        adapter = new FeaturedAdapter(featuredLocations);

        featuredRecycler.setAdapter(adapter);

        return view;
    }
}