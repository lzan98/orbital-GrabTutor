package com.example.grabtutor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grabtutor.Model.PaymentHistory;
import com.example.grabtutor.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.MyViewHolder> {

    Context context;

    ArrayList<PaymentHistory> list;

    public PaymentHistoryAdapter(Context context, ArrayList<PaymentHistory> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public PaymentHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.payment_history_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PaymentHistoryAdapter.MyViewHolder holder, int position) {

        final PaymentHistory paymentHistory = list.get(position);
        holder.ref.setText(paymentHistory.getRefNumber());
        holder.amt.setText(paymentHistory.getAmount());
        holder.date.setText(paymentHistory.getDate());
        holder.status.setText(paymentHistory.getStatus());
        holder.type.setText(paymentHistory.getPaymentType());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ref, amt, status, type, date;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            ref = itemView.findViewById(R.id.payment_history_item_ref);
            amt = itemView.findViewById(R.id.payment_history_item_amt);
            status = itemView.findViewById(R.id.payment_history_item_status);
            type = itemView.findViewById(R.id.payment_history_item_paymentType);
            date = itemView.findViewById(R.id.payment_history_item_date);
        }
    }
}
