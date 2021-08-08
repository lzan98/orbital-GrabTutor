package com.example.grabtutor.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grabtutor.Activity.EditPostActivity;
import com.example.grabtutor.Activity.EditReviewActivity;
import com.example.grabtutor.Activity.LeaveReviewActivity;
import com.example.grabtutor.Model.Review;
import com.example.grabtutor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.HolderReview> {

    private Context context;
    private ArrayList<Review> reviewArrayList;

    public ReviewAdapter(Context context, ArrayList<Review> reviewArrayList) {
        this.context = context;
        this.reviewArrayList = reviewArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public HolderReview onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_review, parent, false);
        return new HolderReview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HolderReview holder, int position) {
        Review review = reviewArrayList.get(position);
        String userId = review.getUserId();
        String ratings = review.getRatings();
        String timestamp = review.getTimestamp();
        String reviewText = review.getReview();
        String postId = review.getpostId();

        //load info of user who wrote the review
        loadUserDetail(review, holder);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String dateFormat = DateFormat.format("dd/MM/yyyy", calendar).toString();

        holder.ratingBar.setRating(Float.parseFloat(ratings));
        holder.reviewTv.setText(reviewText);
        holder.dateTv.setText(dateFormat);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Ratings").child(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                Intent intent = new Intent(context, EditReviewActivity.class);
                                intent.putExtra("rating", Float.parseFloat(ratings));
                                intent.putExtra("reviewText", review.getReview());
                                intent.putExtra("userId", review.getUserId());
                                context.startActivity(intent);
                                ((Activity)context).finish();

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
            }
        });

    }
    private void loadUserDetail(Review review, HolderReview holder) {
        String userId = review.getUserId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (!FirebaseAuth.getInstance().getUid().equals(review.getUserId())) {

                    holder.edit.setVisibility(View.INVISIBLE);
                }

                String username = "" + snapshot.child("username").getValue();
                String profileImage = "" + snapshot.child("profile_picture").getValue();

                holder.nameTv.setText(username);
                try{
                    Picasso.get().load(profileImage).placeholder(R.mipmap.ic_launcher).into(holder.profileIv);
                } catch (Exception e){
                    holder.profileIv.setImageResource(R.mipmap.ic_launcher);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    class HolderReview extends RecyclerView.ViewHolder {

        private ImageView profileIv, edit;
        private TextView nameTv, dateTv, reviewTv;
        private RatingBar ratingBar;

        public HolderReview(@NonNull @NotNull View itemView) {
            super(itemView);
            profileIv = itemView.findViewById(R.id.profileIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            reviewTv = itemView.findViewById(R.id.reviewTv);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            edit = itemView.findViewById(R.id.edit);

        }

    }
}
