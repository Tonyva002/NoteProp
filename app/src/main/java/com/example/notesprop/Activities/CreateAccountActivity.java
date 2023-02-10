package com.example.notesprop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class CreateAccountActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirmPassword;
    private MaterialButton btnCreateAccount;
    private ProgressBar progressBar;
    private TextView login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        onInit();

        btnCreateAccount.setOnClickListener(v -> createAccount());

        login.setOnClickListener(v -> finish());
    }

    private void createAccount() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        boolean isValidated = validateData(email, password, confirmPassword);

        if (!isValidated){
            return;
            
        }
        
        createAccountInFirebase(email, password);


    }

    private void createAccountInFirebase(String email, String password) {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    //creating acc is done
                    Utility.showToast(CreateAccountActivity.this, "Succesfully create account, Check email to verify");
                    Objects.requireNonNull(firebaseAuth.getCurrentUser()).sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                }else {
                    //Failure
                    Utility.showToast(CreateAccountActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage());

                }
            }
        });
    }

    private void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btnCreateAccount.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            btnCreateAccount.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password, String confirmPassword){

        //Validate the data that are input by user
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Email is invalid");
            return false;
        }
        if (password.length()<6){
            etPassword.setError("Password length is invalid");
            return false;
        }
        if (!password.equals(confirmPassword)){
            etConfirmPassword.setError("Password not matched");
            return false;
        }

        return true;
    }


    private void onInit() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        login = findViewById(R.id.tvLogin);
        progressBar = findViewById(R.id.progressBar);
    }
}