package com.vesit.cakeshopandroid.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vesit.cakeshopandroid.R;
import com.vesit.cakeshopandroid.activity.user.LoginActivity;
import com.vesit.cakeshopandroid.activity.user.ProductDetailActivity;
import com.vesit.cakeshopandroid.model.ProductModel;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {

    ProductModel productModel = new ProductModel();
    View view;
    ArrayList<ProductModel> productList;

    public ProductListAdapter(ArrayList<ProductModel> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_priduct_list, parent, false);
        return new ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        holder.product_name.setText(productList.get(position).getProduct_name());
        holder.product_price.setText("₹ "+productList.get(position).getProduct_price());
        Glide.with(holder.img_product.getContext()).load(productList.get(position).getProduct_image()).into(holder.img_product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }



    public class ProductListViewHolder extends RecyclerView.ViewHolder {

        public TextView product_name,product_price;
        public ImageView img_product;

        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.tv_product_name);
            product_price = itemView.findViewById(R.id.tv_product_price);
            img_product = itemView.findViewById(R.id.iv_product);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), ProductDetailActivity.class);
                    i.putExtra("intent_product_name", String.valueOf(productList.get(getAdapterPosition()).getProduct_name()));
                    i.putExtra("intent_product_price", String.valueOf(productList.get(getAdapterPosition()).getProduct_price()));
                    i.putExtra("intent_product_image", String.valueOf(productList.get(getAdapterPosition()).getProduct_image()));

                    v.getContext().startActivity(i);
                }
            });
        }
    }



}
