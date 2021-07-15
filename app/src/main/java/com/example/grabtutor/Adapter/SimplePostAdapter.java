package com.example.grabtutor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grabtutor.Activity.SignUpActivity;
import com.example.grabtutor.Fragment.PostDetailFragment;
import com.example.grabtutor.Fragment.ProfileFragment;
import com.example.grabtutor.Model.Post;
import com.example.grabtutor.Model.User;
import com.example.grabtutor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimplePostAdapter extends RecyclerView.Adapter<SimplePostAdapter.Viewholder> {
    private Context mContext;
    private List<Post> mPosts;
    private FirebaseUser firebaseUser;
    private String postId;

    public SimplePostAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    @NonNull
    @Override
    public SimplePostAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.simple_post_item, parent, false);
        return new SimplePostAdapter.Viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        final Post post = mPosts.get(position);
        postId = post.getPostid();
        Picasso.get().load(post.getImageurl()).into(holder.postImage);
        holder.title.setText(post.getTitle());
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
                } else {
                    ratingSum = 0;

                }
                holder.rating.setText("" + ratingSum);
                holder.numOfRating.setText("(" + numberOfReviews + ")");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });


        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                //Picasso.get().load(user.getProfile_picture()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);
                //holder.username.setText(user.getUsername());//user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        /*holder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                        .edit().putString("profileId", post.getPublisher()).apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment()).commit();
            }
        });*/

        /*holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                        .edit().putString("profileId", post.getPublisher()).apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment()).commit();
            }
        });*/


        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("postid", post.getPostid()).apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new PostDetailFragment()).addToBackStack(null).commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        public ImageView imageProfile, postImage;

        public TextView username, author;
        TextView title, description, rating, numOfRating, price;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            postImage = itemView.findViewById(R.id.post_image);
            //username = itemView.findViewById(R.id.username);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            rating = itemView.findViewById(R.id.rating);
            numOfRating = itemView.findViewById(R.id.numOfRating);
            //description = itemView.findViewById(R.id.description);

        }
    }
}
