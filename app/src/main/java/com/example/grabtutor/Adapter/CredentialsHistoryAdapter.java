package com.example.grabtutor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grabtutor.Model.CredentialsModel;
import com.example.grabtutor.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CredentialsHistoryAdapter extends RecyclerView.Adapter<CredentialsHistoryAdapter.MyViewHolder> {

    Context context;

    ArrayList<CredentialsModel> list;

    public CredentialsHistoryAdapter(Context context, ArrayList<CredentialsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public CredentialsHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.credentals_history_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CredentialsHistoryAdapter.MyViewHolder holder, int position) {

        final CredentialsModel credentialsModel = list.get(position);
        holder.ref.setText(credentialsModel.getRefNo());
        holder.experience.setText(credentialsModel.getExperience());
        holder.date.setText(credentialsModel.getDate());
        holder.status.setText(credentialsModel.getStatus());
        holder.domain.setText(credentialsModel.getDomain());
        holder.description.setText(credentialsModel.getDescription());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ref, domain, status, description, date, experience;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            ref = itemView.findViewById(R.id.credentials_history_item_ref);
            domain = itemView.findViewById(R.id.credentials_history_item_domain);
            status = itemView.findViewById(R.id.credentials_history_item_status);
            description = itemView.findViewById(R.id.credentials_history_item_description);
            date = itemView.findViewById(R.id.credentials_history_item_date);
            experience = itemView.findViewById(R.id.credentials_history_item_experience);
        }
    }
}
