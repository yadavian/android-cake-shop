package com.vesit.cakeshopandroid.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vesit.cakeshopandroid.R;
import com.vesit.cakeshopandroid.model.ProductModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class AddressActivity extends AppCompatActivity {

    Button btn_delivery_address;
    EditText et_address,et_pincode,et_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        btn_delivery_address = (Button) findViewById(R.id.btn_delivery_address);
        et_address=(EditText)findViewById(R.id.et_address);
        et_pincode=(EditText)findViewById(R.id.et_pincode);
        et_city=(EditText)findViewById(R.id.et_city);


        btn_delivery_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = et_address.getText().toString().trim();
                String pincode = et_pincode.getText().toString().trim();
                String city = et_city.getText().toString().trim();

                String fullAddress = address +", "+ pincode +", "+ city;

                HashMap<String,String> addressData = new HashMap<String,String>();
                addressData.put("address",fullAddress);

                if (address.isEmpty()) {
                    et_address.setError("Email Required");
                    et_address.requestFocus();
                    return;
                }
                if (pincode.isEmpty()) {
                    et_pincode.setError("Email Required");
                    et_pincode.requestFocus();
                    return;
                }
                if (pincode.length() < 5) {
                    et_pincode.setError("Password Length should be 6 Characters !");
                    et_pincode.requestFocus();
                    return;
                }

                if (city.isEmpty()) {
                    et_city.setError("Email Required");
                    et_city.requestFocus();
                    return;
                }

                Intent intent=new Intent(AddressActivity.this,OrderSummaryActivity.class);
                intent.putExtra("FULL_ADDRESS",fullAddress);
                startActivity(intent);

//                SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
//                String fetchedSpProductList = sharedPreferences.getString("spProductList", "");
//                Log.d("AddressScreen => list", ">>" + fetchedSpProductList);
//                Log.d("AddressScreen => list", ">>" +fetchedSpProductList.getClass());
//
//
//                Gson gson = new Gson();
//                Type type=new TypeToken<ArrayList<HashMap<String,String>>>() {}.getType();
//                ArrayList<HashMap<String,String>> list = gson.fromJson(fetchedSpProductList,type);
//                Log.d("AddressScreen => list", ">>" + list);
//                Log.d("AddressScreen => list", ">>" +list.getClass());
//
//                list.add(addressData);
//
//                Log.d("AddressScreen => list", ">>" + list);
//                Log.d("AddressScreen => list", ">>" +list.getClass());




            }
        });
    }
}