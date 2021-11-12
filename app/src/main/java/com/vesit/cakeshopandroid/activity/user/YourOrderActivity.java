package com.vesit.cakeshopandroid.activity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vesit.cakeshopandroid.R;
import com.vesit.cakeshopandroid.adapter.ProductListAdapter;
import com.vesit.cakeshopandroid.adapter.YourOrderAdapter;
import com.vesit.cakeshopandroid.model.ProductModel;
import com.vesit.cakeshopandroid.model.YourOrderModel;

import java.util.ArrayList;
import java.util.List;

public class YourOrderActivity extends AppCompatActivity {

    RecyclerView rv_your_order;

    ArrayList<YourOrderModel> order_list;
    YourOrderAdapter yourOrderAdapter;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order);

        rv_your_order= (RecyclerView)findViewById(R.id.rv_your_order);

        // RECYCLER VIEW
        rv_your_order.setLayoutManager(new LinearLayoutManager(this));
        order_list = new ArrayList<>();
        yourOrderAdapter = new YourOrderAdapter(order_list);
        rv_your_order.setAdapter(yourOrderAdapter);


        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE); 
        String userId = sharedPreferences.getString("userId", "");

        db = FirebaseFirestore.getInstance();

        db.collection("orders").whereEqualTo("userId", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list) {
                    Log.d("HomePage", ">> DocumentSnapshot >>" + d);
                    YourOrderModel pmObject = d.toObject(YourOrderModel.class);
                    Log.d("HomePage", ">> pmObject >>" + pmObject.toString());
                    order_list.add(pmObject);
                    Log.d("HomePage", ">> order_list >>" + order_list);
                }
                yourOrderAdapter.notifyDataSetChanged();

                Log.d("HomePage", ">> order_list >>" + order_list);
            }
        });


    }
}