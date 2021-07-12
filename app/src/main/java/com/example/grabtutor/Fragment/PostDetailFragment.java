package com.example.grabtutor.Fragment;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;

import com.example.grabtutor.Activity.PostFitnessActivity;
import com.example.grabtutor.Activity.ReviewActivity;
import com.example.grabtutor.Activity.ReviewOrderActivity;
import com.example.grabtutor.Adapter.PostAdapter;
import com.example.grabtutor.Adapter.ReviewAdapter;
import com.example.grabtutor.Model.Post;
import com.example.grabtutor.Model.Review;
import com.example.grabtutor.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PostDetailFragment extends Fragment {

    private String postId;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private Button buyButton;
    ImageButton reviewsBtn;
    private RatingBar ratingBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        postId = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("postid", "none");
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);
        buyButton = view.findViewById(R.id.buyButton);
        reviewsBtn = view.findViewById(R.id.rating);
        ratingBar = view.findViewById(R.id.ratingBar);
        
        loadReviews();

        FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                postList.add(dataSnapshot.getValue(Post.class));

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ReviewOrderActivity.class));

            }
        });

        ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                intent.putExtra("postId", postId);
                startActivity(intent);
            }
        });

        return view;
    }

    private float ratingSum = 0;
    private void loadReviews() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.child(postId).child("Ratings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ratingSum = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    float rating = Float.parseFloat("" + ds.child("ratings").getValue());
                    ratingSum = ratingSum + rating;
                }


                long numberOfReviews = snapshot.getChildrenCount();
                float avgRating = ratingSum/numberOfReviews;
                ratingBar.setRating(avgRating);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}