package com.vesit.cakeshopandroid.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.vesit.cakeshopandroid.R;
import com.vesit.cakeshopandroid.model.UserModel;
import com.vesit.cakeshopandroid.model.YourOrderModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView banner, updateUser;
    private EditText editTextFullName, editTextPhoneNumber, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    String userId;

    ArrayList<UserModel> userList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        updateUser = (Button) findViewById(R.id.updateUser);
        updateUser.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextPhoneNumber = (EditText) findViewById(R.id.phone_number);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
         userId = sharedPreferences.getString("userId", "");
        Log.d("ProfilePage >> ", ">> userId >>" + userId);
        db = FirebaseFirestore.getInstance();
//        Toast.makeText(ProfileActivity.this, userId, Toast.LENGTH_LONG).show();


        db.collection("users").whereEqualTo(FieldPath.documentId(), userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list) {
                    Log.d("ProfilePage", ">> DocumentSnapshot >>" + d);

                    UserModel pmObject = d.toObject(UserModel.class);
                    Log.d("ProfilePage", ">> pmObject >>" + pmObject.getClass());
                    userList = new ArrayList<>(Arrays.asList(pmObject));

                    editTextFullName.setText(userList.get(0).getName());
                    editTextEmail.setText(userList.get(0).getEmail());
                    editTextPassword.setText(userList.get(0).getPassword());
                    editTextPhoneNumber.setText(userList.get(0).getPhone());
                }
                Log.d("ProfilePage", ">> userList >>" + userList);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateUser:
                updateUser();
                break;
        }
    }

    private void updateUser() {

        progressBar.setVisibility(View.VISIBLE);
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String phone_number = editTextPhoneNumber.getText().toString().trim();

        UserModel userModel=new UserModel(fullName,email,password,phone_number);

        db.collection("users").document(userId).set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                    Toast.makeText(ProfileActivity.this, "Updated the Profile Data !", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });





    }
}