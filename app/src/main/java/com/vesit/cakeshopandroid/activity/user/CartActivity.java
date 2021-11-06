package com.vesit.cakeshopandroid.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vesit.cakeshopandroid.R;
import com.vesit.cakeshopandroid.adapter.CartAdapter;
import com.vesit.cakeshopandroid.adapter.ProductListAdapter;
import com.vesit.cakeshopandroid.model.ProductModel;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    TextView tv_total_prize;
    RecyclerView rv_cart_list;
    Button btn_checkout,btn_clear_cart;

    FirebaseFirestore db;
    CartAdapter cartAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rv_cart_list = (RecyclerView) findViewById(R.id.rv_cart_list);
        btn_checkout = (Button) findViewById(R.id.btn_checkout);
        btn_clear_cart = (Button) findViewById(R.id.btn_clear_cart);
        tv_total_prize = (TextView) findViewById(R.id.tv_total_prize);

        db = FirebaseFirestore.getInstance();
        CollectionReference orderRef = db.collection("order");

        //GETTING PRODUCTS FROM SHARED PREFERENCE
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String fetchedSpProductList = sharedPreferences.getString("spProductList", "");
        Log.d("CartScreen   list", ">>" + fetchedSpProductList);
//        Log.d("CartScreen   list", ">>" +fetchedSpProductList.getClass());

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ProductModel>>() {}.getType();
        ArrayList<ProductModel> list = gson.fromJson(fetchedSpProductList, type);
        Log.d("CartScreen   list", ">>" + list);
//        Log.d("CartScreen   list", ">>" + list.size());
//        Log.d("CartScreen   list", ">>" +list.getClass());

        // GETTING TOTAL PRIZE AND ADDING IN SHARED PREFERENCES
        Type typeHash = new TypeToken<ArrayList<HashMap>>() {}.getType();
        ArrayList<HashMap> listHash = gson.fromJson(fetchedSpProductList, typeHash);
        Log.d("CartScreen   listHash", ">>" + listHash);
        Log.d("CartScreen   listHash", ">>" + listHash.size());
        Log.d("CartScreen   listHash", ">>" +listHash.getClass());

        Integer totalPrize = 0;
        for (int i=0;i<listHash.size();i++)
        {
            HashMap<String, String> hashmap= listHash.get(i);
            String string= hashmap.get("product_price");
            Log.d("CartScreen   string", ">>" + string);
            totalPrize = totalPrize + Integer.valueOf(string);
        }
        Log.d("CartScreen   totalPrize", ">>" + totalPrize);

        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("totalPrize", String.valueOf(totalPrize));
        editor.commit();

        tv_total_prize.setText("TOTAL PRICE :  ₹ "+totalPrize);



        //SETTING UP PRODUCTS USING RECYCLER VIEW
        rv_cart_list.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(list);
        rv_cart_list.setAdapter(cartAdapter);

//        Date date = new Date();
//        Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
//        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
//        String todayDate = dt.format(newDate);
//
//        HashMap<String, Object> orderData = new HashMap<>();
//        orderData.put("crated_at", todayDate);
//        orderData.put("products", list);
//
//        Log.d("CartScreen   orderData", ">>" + orderData);
//        Log.d("CartScreen   orderData", ">>" + orderData.getClass());

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, AddressActivity.class));
//                db.collection("orders").document().set(orderData).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            String orderId = orderRef.document().getId();
//                            Toast.makeText(CartActivity.this, orderId, Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
            }
        });


        btn_clear_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().remove("spProductList").commit();
                sharedPreferences.edit().remove("totalPrize").commit();Toast.makeText(CartActivity.this, "Cart is Cleared !  ", Toast.LENGTH_LONG).show();
                startActivity(new Intent(CartActivity.this, HomeActivity.class));
            }
        });

    }
}