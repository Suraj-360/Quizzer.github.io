package com.suraj.quizzer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.suraj.quizzer.databinding.ActivityLoginBinding;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding activityLoginBinding;
    FirebaseAuth firebaseAuth;
    UserModel userModel;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());
        progressBar = new ProgressBar(this);
        Objects.requireNonNull(getSupportActionBar()).show();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Login Account");
        Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getSupportActionBar()))).setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.toolbar_bg));
        activityLoginBinding.createANewAccount.setOnClickListener(v ->
        {
            Intent registerActivityIntent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(registerActivityIntent);
            finish();
        });
        firebaseAuth = FirebaseAuth.getInstance();

        activityLoginBinding.loginBtnMain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.VISIBLE);
                String email = activityLoginBinding.loginEmail.getText().toString();
                String password = activityLoginBinding.loginPassword.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            progressBar.setVisibility(View.GONE);
                            Intent dashBoardIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(dashBoardIntent);
                            finish();
                        } else
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, task.getException() + " :-(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        if (firebaseAuth.getCurrentUser() != null)
        {
            Intent dashBoardIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(dashBoardIntent);
            finish();
        }
    }

    public void onBackPressed() {
        Intent dashBoardIntent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(dashBoardIntent);
        finish();
    }
}