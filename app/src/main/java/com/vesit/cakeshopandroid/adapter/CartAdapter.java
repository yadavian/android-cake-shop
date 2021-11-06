package com.vesit.cakeshopandroid.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vesit.cakeshopandroid.R;
import com.vesit.cakeshopandroid.model.ProductModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    View view;
    ArrayList<ProductModel> productList;

    public CartAdapter(ArrayList<ProductModel> productList) {
        this.productList = productList;
        Log.d("CartPage => list", ">>" + productList);
        Log.d("CartPage => list", ">>" +productList.getClass());
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.product_name.setText(productList.get(position).getProduct_name());
        holder.product_price.setText("₹ "+productList.get(position).getProduct_price());
        Glide.with(holder.img_product.getContext()).load(productList.get(position).getProduct_image()).into(holder.img_product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder
    {
        public TextView product_name,product_price;
        public ImageView img_product;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.tv_product_name);
            product_price = itemView.findViewById(R.id.tv_product_price);
            img_product = itemView.findViewById(R.id.iv_product);

        }
    }


}
