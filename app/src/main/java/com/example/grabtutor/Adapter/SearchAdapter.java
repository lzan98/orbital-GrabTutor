package com.example.grabtutor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grabtutor.Fragment.PostDetailFragment;
import com.example.grabtutor.Model.Post;
import com.example.grabtutor.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private ArrayList<Post> list = new ArrayList<>();
    private Context mContext;

    public SearchAdapter(Context mContext, ArrayList<Post> mPosts) {
        this.mContext = mContext;
        this.list = mPosts;
    }
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //ViewGroup is the parent class of all layout classes (ie RelativeLayout etc)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_post_item, parent, false);
        SearchViewHolder holder = new SearchViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        final Post post = list.get(position);
        String postId = post.getPostid();

        holder.title.setText(post.getTitle());
        Picasso.get().load(post.getImageurl()).into(holder.postImage);
        holder.price.setText("$ " + post.getPrice());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        ref.child(postId).child("Ratings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                float ratingSum = 0;
                long numberOfReviews = 0;
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        float rating = Float.parseFloat("" + ds.child("ratings").getValue());
                        ratingSum = ratingSum + rating;
                    }
                    numberOfReviews = snapshot.getChildrenCount();
                    ratingSum = ratingSum / numberOfReviews;
                }
                holder.rating.setText("" + ratingSum);
                holder.numOfRating.setText("(" + numberOfReviews + ")");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });


        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postId = list.get(position).getPostid();
                mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("postid", postId).apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new PostDetailFragment()).addToBackStack(null).commit();

            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<Post> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }


    public class SearchViewHolder extends RecyclerView.ViewHolder{

        private TextView title, price, rating, numOfRating;
        private ImageView postImage;
        private CardView parent;
        SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.post_image);
            title = itemView.findViewById(R.id.title); // need to call using itemView because we are not inside an Activity
            parent = itemView.findViewById(R.id.card);
            price = itemView.findViewById(R.id.price);
            numOfRating = itemView.findViewById(R.id.numOfRating);
            rating = itemView.findViewById(R.id.rating);



        }
    }
}
