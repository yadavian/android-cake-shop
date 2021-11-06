package com.vesit.cakeshopandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vesit.cakeshopandroid.R;
import com.vesit.cakeshopandroid.model.CategoryModel;
import com.vesit.cakeshopandroid.viewholder.CategoryViewHolder;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    ArrayList<CategoryModel> data;

    public CategoryAdapter(ArrayList<CategoryModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_categories,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.category_name.setText(data.get(position).getCategory_name());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
