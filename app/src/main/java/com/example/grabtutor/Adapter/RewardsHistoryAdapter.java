package com.example.grabtutor.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grabtutor.Activity.LoginActivity;
import com.example.grabtutor.Activity.MainActivity;
import com.example.grabtutor.Activity.RedeemRewardsActivity;
import com.example.grabtutor.Model.CredentialsModel;
import com.example.grabtutor.Model.RewardsRedemptionHistory;
import com.example.grabtutor.Model.RewardsRedemptionModel;
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

import java.util.ArrayList;

public class RewardsHistoryAdapter extends RecyclerView.Adapter<RewardsHistoryAdapter.MyViewHolder> {

    Context context;

    ArrayList<RewardsRedemptionHistory> list;

    public RewardsHistoryAdapter(Context context, ArrayList<RewardsRedemptionHistory> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public RewardsHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rewards_history_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        final RewardsRedemptionHistory rewardsRedemptionHistory = list.get(position);
        holder.title.setText(rewardsRedemptionHistory.getTitle());
        holder.points.setText(rewardsRedemptionHistory.getPoints());
        holder.refNo.setText(rewardsRedemptionHistory.getRefNo());
        holder.date.setText(rewardsRedemptionHistory.getDate());
        holder.status.setText(rewardsRedemptionHistory.getStatus());

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView refNo, title, points, status, date;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            refNo = itemView.findViewById(R.id.rewards_history_item_ref);
            title = itemView.findViewById(R.id.reward_history_item_title);
            points = itemView.findViewById(R.id.reward_history_item_points);
            status = itemView.findViewById(R.id.reward_history_item_status);
            date = itemView.findViewById(R.id.reward_history_item_date);

        }
    }
}
