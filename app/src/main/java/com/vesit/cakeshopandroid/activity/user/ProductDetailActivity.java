package com.vesit.cakeshopandroid.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vesit.cakeshopandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    FirebaseFirestore db;

    TextView text_product_name, text_product_price, text_product_quantity, tv_single_product_price;
    ImageView img_product;
    Button bn_add_to_cart, bn_counter_decrement, bn_counter_increment;
    int quantity = 1;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();

        bn_add_to_cart = findViewById(R.id.bn_add_to_cart);
        bn_counter_increment = findViewById(R.id.bn_counter_increment);
        bn_counter_decrement = findViewById(R.id.bn_counter_decrement);

        text_product_name = findViewById(R.id.tv_product_name);
        text_product_price = findViewById(R.id.tv_product_price);
        text_product_quantity = findViewById(R.id.tv_product_quantity);
        tv_single_product_price = findViewById(R.id.tv_total_single_product_price);

        img_product = findViewById(R.id.iv_product);

        text_product_name.setText(getIntent().getStringExtra("intent_product_name"));
        text_product_price.setText("₹ " + getIntent().getStringExtra("intent_product_price"));
        Glide.with(this).load(getIntent().getStringExtra("intent_product_image")).into(img_product);


        bn_counter_increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                text_product_quantity.setText(String.valueOf(quantity));
                tv_single_product_price.setText("TOTAL PRICE :  ₹ " + String.valueOf(quantity * Integer.valueOf(getIntent().getStringExtra("intent_product_price"))));
                Log.d("ProductDetailScreen quantity", ">>" + quantity);
            }
        });

        bn_counter_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity--;
                text_product_quantity.setText(String.valueOf(quantity));
            }
        });

        bn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity == 0) {
                    Toast.makeText(ProductDetailActivity.this, "Please enater quantity more than 0 ", Toast.LENGTH_LONG).show();
                } else {
                    AddToCart();
//                    Toast.makeText(ProductDetailActivity.this, "You've ordered  ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @SuppressLint("LongLogTag")
    private void AddToCart() {
//        Log.d("Cart", ">> Cart Clicked");
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

//        CollectionReference orderRef = db.collection("order");

        Date date = new Date();
        Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = dt.format(newDate);

        HashMap<String, Object> productData = new HashMap<>();
        productData.put("product_name", getIntent().getStringExtra("intent_product_name"));
        productData.put("product_image", getIntent().getStringExtra("intent_product_image"));
        productData.put("quantity", quantity);
        productData.put("product_price", String.valueOf(quantity * Integer.valueOf(getIntent().getStringExtra("intent_product_price"))));

        Log.d("ProductDetailScreen  productData", ">>" + productData);
        Log.d("ProductDetailScreen  productData", ">>" + productData.getClass());


        Gson gson = new Gson();

        if (sharedPreferences.contains("spProductList")) {
//            FETCHING SHARED PREFERENCE STRING TO LIST
            Log.d("ProductDetailScreen: docId", "Sp Exist: ");
            String fetchedSpProductList = sharedPreferences.getString("spProductList", "");

            Type type = new TypeToken<ArrayList<Object>>() {
            }.getType();
            List<Object> list = gson.fromJson(fetchedSpProductList, type);
            Log.d("ProductDetailScreen  list", ">>" + list);
            Log.d("ProductDetailScreen  list", ">>" + list.getClass());

            list.add(productData);

//            ADDING TO SHARED PREFERENCE LIST TO STRING
            String jsonString = gson.toJson(list);
            editor.putString("spProductList", jsonString);
            editor.commit();

            Log.d("ProductDetailScreen  spProductList", ">>" + sharedPreferences.getString("spProductList", ""));
            Log.d("ProductDetailScreen  spProductList", ">>" + sharedPreferences.getString("spProductList", "").getClass());

            Toast.makeText(ProductDetailActivity.this, "Added To Cart !  ", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ProductDetailActivity.this, HomeActivity.class));

        } else {
            Log.d("docId", "Sp Not Exist: ");

            ArrayList<HashMap<String, Object>> productArrayList = new ArrayList<>();
            productArrayList.add(productData);

            String jsonString = gson.toJson(productArrayList);
            editor.putString("spProductList", jsonString);
            editor.commit();

            Log.d("ProductDetailScreen  spProductList", ">>" + sharedPreferences.getString("spProductList", ""));
            Log.d("ProductDetailScreen  spProductList", ">>" + sharedPreferences.getString("spProductList", "").getClass());


            Toast.makeText(ProductDetailActivity.this, "Added To Cart !  ", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ProductDetailActivity.this, HomeActivity.class));

        }


//        HashMap<String, Object> productCart = new HashMap<>();

//        ArrayList<Object> productList = new ArrayList<Object>();
//
//        int length = productList.size();
//
//        productList.add(productData);

//        ADDING TO SHARED PREFERENCE
//
//        Gson gson = new Gson();
//        String convertedToStringJson = gson.toJson(productList);
//        editor.putString("spProductList", convertedToStringJson);
//        editor.commit();
//
////        Log.d("Cart   productList", ">>" + productList);
//        Log.d("Cart   spProductList", ">>" + sharedPreferences.getString("spProductList", ""));

//        Retriving and adding to shared Preference

//        String spProductList = sharedPreferences.getString("spProductList", null);
//
//        productList.add(productData);
//        productList.add(spProductList);
//
//        Log.d("Cart   productData", ">>" + productData);
//        Log.d("Cart   spProductList", ">>" + spProductList.getClass());
//
//        Map<String, Object> myMap = new HashMap<String, Object>();
//
//        String[] elements = spProductList.split(",");
//        for (String s1 : elements) {
//            String[] keyValue = s1.split(":");
//            myMap.put(keyValue[0], keyValue[1]);
//        }
//
//        Log.d("Cart   spProductList", ">>" + myMap.getClass());
//        Log.d("Cart   spProductList", ">>" + myMap);
//
//        productList.add(myMap);
//        Log.d("Cart   ProductList", ">>" + productList);


//        if (sharedPreferences.contains("spProductList")) {
//
////            sharedPreferences.edit().remove("spProductList").commit();
//            Log.d("Cart", ">>productList exists");
//
//            Gson gson = new Gson();
//            String json = sharedPreferences.getString("spProductList", "");
//
//            Map<String, Object> retMap = new Gson().fromJson(
//                    json, new TypeToken<HashMap<String, Object>>() {
//                    }.getType()
//            );
//
//
//            Log.d("Cart 2", "" + retMap.getClass());
//
//            Log.d("Cart   json", ">>" + json);
//
//            productList.add(productData);
//            productList.add(json);
//
//            //  ADDING TO SHARED PREFERENCE
//            String convertedToStringJson = new Gson().toJson(productList);
//            editor.putString("spProductList", convertedToStringJson);
//            editor.commit();
//
//            Log.d("Cart   spProductList", ">>" + sharedPreferences.getString("spProductList", ""));
//
//
//        } else {
//
////            sharedPreferences.edit().remove("spProductList").commit();
//
//            Log.d("Cart", ">>productList not exists");
//            productList.add(productData);
//
////            ADDING TO SHARED PREFERENCE
//            String convertedToStringJson = new Gson().toJson(productList);
//            editor.putString("spProductList", convertedToStringJson);
//            editor.commit();
//
////            Log.d("Cart", ">>" + productList);
//            Log.d("Cart   spProductList", ">>" + sharedPreferences.getString("spProductList", ""));
//        }


//        productCart.put("products", productData);


//        HashMap<String, Object> cartData = new HashMap<>();
//        cartData.put("products", "");
//        cartData.put("created_at", todayDate);
//        cartData.put("userId", sharedPreferences.getString("userId", ""));

//        if (!sharedPreferences.contains("cardData")) {
//            Log.d("Cart 1", "First Time + First Product and details");
//            //ADDING TO SHARED PREFERENCE
////            String convertedToStringJson = new Gson().toJson(cartData);
////            editor.putString("cardData", convertedToStringJson);
////            editor.commit();
//        } else {
//            Log.d("Cart 2", "SECOND TIME + ONLY Product will update + Total Price");
////          SHARED PREFERENCES DELETE PARTICULAR DATA
//          sharedPreferences.edit().remove("cardData").commit();
//
//            Gson gson = new Gson();
//            String json = sharedPreferences.getString("cardData", "");
//
//            Map<String, Object> retMap = new Gson().fromJson(
//                    json, new TypeToken<HashMap<String, Object>>() {
//                    }.getType()
//            );
//
//
//            Log.d("Cart 2", ""+retMap.get("products").getClass());
//
//        }


        //RETRIEVING FROM SHARED PREFERENCE IN HASHMAP
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("cardData", "");
//
//        Map<String, Object> retMap = new Gson().fromJson(
//                json, new TypeToken<HashMap<String, Object>>() {
//                }.getType()
//        );

//        Log.d("hashmap", "AddToCart: "+retMap.getClass());

//        USING FIREBASE AND ORDER CART COLLECTIONS

//        db.collection("order").document().set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    String orderId = orderRef.document().getId();
//                    Toast.makeText(ProductDetailActivity.this, orderId, Toast.LENGTH_LONG).show();
//                }
//            }
//        });


//        HashMap<String, Object> productData = new HashMap<>();
//        productData.put("product_name", "suraj");
//        productData.put("product_price", "602");
//        productData.put("product_image", "http");
//        productData.put("quantity", "5");

//        DocumentReference docRef = db.collection("cart").document("sseLUdkIJA4MENMo8tKl");
//        docRef.update("products", FieldValue.arrayUnion(productData)).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Toast.makeText(ProductDetailActivity.this, "Added To Cart !  ", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(ProductDetailActivity.this, "Something went wrong To Cart !  ", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

    }

}