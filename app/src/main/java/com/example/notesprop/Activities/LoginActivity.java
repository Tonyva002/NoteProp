package com.example.notesprop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.notesprop.R;
import com.example.notesprop.Helps.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    private EditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private ProgressBar progressBar;
    private TextView createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        onInit();
        
        btnLogin.setOnClickListener(v -> loginUser());
        createAccount.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)) );
    }

    private void loginUser() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();


        boolean isValidated = validateData(email, password);

        if (!isValidated){
            return;

        }
        loginAccountInFirebase(email, password);

    }
    private void loginAccountInFirebase(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    //login is success
                    if (Objects.requireNonNull(firebaseAuth.getCurrentUser()).isEmailVerified()){
                        //go to home activity
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();


                    }else {
                        Utility.showToast(LoginActivity.this, "Email not verified, Please verify your email");

                    }

                }else {
                    //login failed
                    Utility.showToast(LoginActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage());

                }
            }
        });

    }




    private void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }
    }


    boolean validateData(String email, String password){

        //Validate the data that are input by user
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Email is invalid");
            return false;
        }
        if (password.length()<6){
            etPassword.setError("Password length is invalid");
            return false;
        }
        return true;
    }


    private void onInit() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        createAccount = findViewById(R.id.tvCreateAccount);
        progressBar = findViewById(R.id.progressBar);
    }
}