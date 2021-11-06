package com.vesit.cakeshopandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vesit.cakeshopandroid.R;
import com.vesit.cakeshopandroid.model.ProductModel;
import com.vesit.cakeshopandroid.model.YourOrderModel;

import java.util.ArrayList;

public class YourOrderAdapter extends RecyclerView.Adapter<YourOrderAdapter.YourOrderViewHolder> {

    YourOrderModel yourOrderModel;
    View view;
    ArrayList<YourOrderModel> data;

    public YourOrderAdapter(ArrayList<YourOrderModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public YourOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_your_order, parent, false);
        return new YourOrderAdapter.YourOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YourOrderViewHolder holder, int position) {
        holder.tv_order_date.setText(data.get(position).getOrderDate());
        holder.tv_total_prize.setText("₹ " + data.get(position).getOrderPrize());
        holder.tv_address.setText("₹ " + data.get(position).getAddress());
    }


        @Override
        public int getItemCount () {
            return data.size();
        }

        public class YourOrderViewHolder extends RecyclerView.ViewHolder {

            TextView tv_address, tv_order_date, tv_total_prize;

            public YourOrderViewHolder(@NonNull View itemView) {
                super(itemView);


                tv_address = itemView.findViewById(R.id.tv_address);
                tv_order_date = itemView.findViewById(R.id.tv_order_date);
                tv_total_prize = itemView.findViewById(R.id.tv_total_prize);

            }
        }

    }
