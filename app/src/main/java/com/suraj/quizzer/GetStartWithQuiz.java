package com.suraj.quizzer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.suraj.quizzer.databinding.ActivityGetStartWithQuizBinding;

import java.util.Objects;

public class GetStartWithQuiz extends AppCompatActivity {

    ActivityGetStartWithQuizBinding activityGetStartWithQuizBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGetStartWithQuizBinding = ActivityGetStartWithQuizBinding.inflate(getLayoutInflater());
        setContentView(activityGetStartWithQuizBinding.getRoot());

        Intent intent = getIntent();
        String domainName = intent.getStringExtra("Domain Name");
        String image = intent.getStringExtra("Domain Image");
        String categoryId = intent.getStringExtra("Domain ID");

        activityGetStartWithQuizBinding.domainNameGetStart.setText(domainName);
        Glide.with(getApplicationContext()).load(image).into(activityGetStartWithQuizBinding.domainImageGetStart);
        Objects.requireNonNull(getSupportActionBar()).hide();

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        activityGetStartWithQuizBinding.startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startQuizIntent = new Intent(GetStartWithQuiz.this, QuizBoard.class);
                startQuizIntent.putExtra("Domain ID", categoryId);
                startActivity(startQuizIntent);
                finish();
            }
        });

        activityGetStartWithQuizBinding.backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToHomeIntent = new Intent(GetStartWithQuiz.this, MainActivity.class);
                startActivity(backToHomeIntent);
                finish();
            }
        });
    }
}