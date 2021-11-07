package com.vesit.cakeshopandroid.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vesit.cakeshopandroid.R;
import com.vesit.cakeshopandroid.adapter.CategoryAdapter;
import com.vesit.cakeshopandroid.adapter.ProductListAdapter;
import com.vesit.cakeshopandroid.model.CategoryModel;
import com.vesit.cakeshopandroid.model.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView rv_product_list, rv_categories;
    //    ImageView iv_cart;
    ArrayList<ProductModel> product_list;
    ArrayList<CategoryModel> categories;

    FirebaseFirestore db;
    private FirebaseUser user;
    ProductListAdapter product_list_adapter;
    CategoryAdapter categoryAdapter;


    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigation_view;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = FirebaseAuth.getInstance().getCurrentUser();

//        iv_cart = (ImageView) findViewById(R.id.iv_cart);
        rv_product_list = (RecyclerView) findViewById(R.id.rv_product_list);

        //DRAWER
        navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.string_nav_open, R.string.string_nav_close);
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //GETTING CART ADDED PRODUCTS
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String fetchedSpProductList = sharedPreferences.getString("spProductList", "");
        Log.d("HomeActivity => fetchedSpProductList", ">>" + fetchedSpProductList);
        Log.d("HomeActivity => fetchedSpProductList", ">>" + fetchedSpProductList.getClass());

        //RECYCLER VIEW - ADAPTER
        //rv_product_list.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rv_product_list.setLayoutManager(gridLayoutManager);

        product_list = new ArrayList<>();
        product_list_adapter = new ProductListAdapter(product_list);
        rv_product_list.setAdapter(product_list_adapter);

        if (sharedPreferences.contains("userId")) {
            Log.d("docId", "onCreate: " + sharedPreferences.getString("userId", ""));
        } else {
            Log.d("docId", "Not found");
        }

        db = FirebaseFirestore.getInstance();

        db.collection("products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list) {
                    ProductModel pmObject = d.toObject(ProductModel.class);
                    product_list.add(pmObject);

                }
                product_list_adapter.notifyDataSetChanged();

                Log.d("HomePage", ">> product_list >>" + product_list);
            }
        });


        rv_categories = (RecyclerView) findViewById(R.id.rv_categories);
        rv_categories.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        categories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categories);
        rv_categories.setAdapter(categoryAdapter);

        db.collection("categories").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list) {
                    CategoryModel cmObject = d.toObject(CategoryModel.class);
                    categories.add(cmObject);
                }
                categoryAdapter.notifyDataSetChanged();
            }
        });

//        iv_cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HomeActivity.this, CartActivity.class));
//            }
//        });

        FloatingActionButton fab = findViewById(R.id.iv_cart);


        if (sharedPreferences.contains("spProductList")) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.INVISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });

        navigation_view.bringToFront();
        navigation_view.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();

                        if (id == R.id.nav_order) {
                            Log.d("HomeScreen :", ">>" + "onNavigationItemSelected Clicked");
                            startActivity(new Intent(HomeActivity.this, YourOrderActivity.class));

                        } else if (id == R.id.nav_logout) {
                            FirebaseAuth.getInstance().signOut();
                            sharedPreferences.edit().remove("spProductList").commit();
                            sharedPreferences.edit().remove("totalPrize").commit();
                            sharedPreferences.edit().remove("userId").commit();
                            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                            Toast.makeText(HomeActivity.this, "You have loggged Out !!", Toast.LENGTH_LONG).show();
                        }

                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                }
        );

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("HomeScreen :", ">>" + "onOptionsItemSelected Clicked");
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




}