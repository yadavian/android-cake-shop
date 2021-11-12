package com.vesit.cakeshopandroid.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vesit.cakeshopandroid.R;
import com.vesit.cakeshopandroid.adapter.CartAdapter;
import com.vesit.cakeshopandroid.model.ProductModel;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class OrderSummaryActivity extends AppCompatActivity {

    FirebaseFirestore db;

    TextView tv_address, tv_total_prize;
    RecyclerView rv_cart_list;
    Button btn_final_order;
    CartAdapter cartAdapter;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        db = FirebaseFirestore.getInstance();
        CollectionReference orderRef = db.collection("order");

        rv_cart_list = (RecyclerView) findViewById(R.id.rv_cart_list);
        btn_final_order = (Button) findViewById(R.id.btn_final_order);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_total_prize = (TextView) findViewById(R.id.tv_total_prize);

        rv_cart_list.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String fetchedSpProductList = sharedPreferences.getString("spProductList", "");
        String userId = sharedPreferences.getString("userId", "");
        String totalPrize = sharedPreferences.getString("totalPrize", "");
        tv_total_prize.setText("TOTAL PRICE :  ₹ " + totalPrize);

        Log.d("OrderSummaryScreen => list", ">>" + fetchedSpProductList);
        Log.d("OrderSummaryScreen => list", ">>" + fetchedSpProductList.getClass());


        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ProductModel>>() {
        }.getType();
        ArrayList<ProductModel> list = gson.fromJson(fetchedSpProductList, type);
        Log.d("OrderSummaryScreen => list", ">>" + list);
        Log.d("OrderSummaryScreen => list", ">>" + list.getClass());

        cartAdapter = new CartAdapter(list);
        rv_cart_list.setAdapter(cartAdapter);


        Type type2 = new TypeToken<ArrayList<HashMap>>() {
        }.getType();
        ArrayList<HashMap> list2 = gson.fromJson(fetchedSpProductList, type2);
        Log.d("OrderSummaryScreen => list", ">>" + list2);
        Log.d("OrderSummaryScreen => list", ">>" + list2.getClass());

        String fullAddress = getIntent().getStringExtra("FULL_ADDRESS");
        tv_address.setText(fullAddress);

        Date date = new Date();
        Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = dt.format(newDate);



        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("products", list2);
        dataMap.put("address", fullAddress);
        dataMap.put("orderDate", todayDate);
        dataMap.put("orderPrize", totalPrize);
        dataMap.put("userId", userId);

//        HashMap<String, HashMap> finalData = new HashMap<>();
//        finalData.put("order", dataMap);


        btn_final_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("orders").document().set(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            String orderId = orderRef.document().getId();
                            Toast.makeText(OrderSummaryActivity.this, "Your Order is Placed, will be delivered soon.", Toast.LENGTH_LONG).show();
                            sharedPreferences.edit().remove("spProductList").commit();
                            startActivity(new Intent(OrderSummaryActivity.this, HomeActivity.class));
                        }
                    }
                });
            }
        });


    }
}