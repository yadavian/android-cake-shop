package com.vesit.cakeshopandroid.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vesit.cakeshopandroid.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public TextView category_name;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        category_name = (TextView) itemView.findViewById(R.id.tv_category_name);

    }
}
