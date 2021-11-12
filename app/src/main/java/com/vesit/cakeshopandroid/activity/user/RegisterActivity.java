package com.vesit.cakeshopandroid.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Patterns;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vesit.cakeshopandroid.R;
import com.vesit.cakeshopandroid.model.UserModel;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView banner, registerUser;
    private EditText editTextFullName, editTextPhoneNumber, editTextEmail, editTextPassword;
    private ProgressBar progressBar;


    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.register);
        registerUser.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextPhoneNumber = (EditText) findViewById(R.id.phone_number);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.register:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String phone_number = editTextPhoneNumber.getText().toString().trim();

        if (fullName.isEmpty()) {
            editTextFullName.setError("Name Required");
            editTextFullName.requestFocus();
            return;
        }
        if (phone_number.isEmpty()) {
            editTextPhoneNumber.setError("Age Required");
            editTextPhoneNumber.requestFocus();
            return;
        }
        if (phone_number.length() == 9) {
            editTextPhoneNumber.setError("Phone Number Length should be 10 Characters !");
            editTextPhoneNumber.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextEmail.setError("Email Required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email Format Invalid !");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password Required");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Password Length should be greater than 6 Characters !");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserModel userModel=new UserModel(fullName,email,password,phone_number);

                    db.collection("users").document().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.sendEmailVerification();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                Toast.makeText(RegisterActivity.this, "Please Verify your Email, Then Login. !", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });

                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to  Register, Try Again !", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}