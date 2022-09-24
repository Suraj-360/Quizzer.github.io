package com.suraj.quizzer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.suraj.quizzer.databinding.ActivityMainBinding;

import java.util.Objects;

import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Dashboard");
        Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getSupportActionBar()))).setBackgroundDrawable(AppCompatResources.getDrawable(this,R.drawable.toolbar_bg));

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerBase, new HomeFragment());
        fragmentTransaction.commit();

        activityMainBinding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (i) {
                    case 0:
                        Objects.requireNonNull(getSupportActionBar()).setTitle("Dashboard");
                        fragmentTransaction.replace(R.id.containerBase, new HomeFragment());
                        fragmentTransaction.commit();
                        break;
                    case 1:
                        Objects.requireNonNull(getSupportActionBar()).setTitle("User Profile");
                        fragmentTransaction.replace(R.id.containerBase, new ProfileFragment());
                        fragmentTransaction.commit();
                        break;
                    case 2:
                        Objects.requireNonNull(getSupportActionBar()).setTitle("About Quizzer");
                        fragmentTransaction.replace(R.id.containerBase, new AboutFragment());
                        fragmentTransaction.commit();
                        break;
                }
                return true;
            }
        });
    }

    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to exit Quizzer?");
        builder.setTitle("Close Application Alert!");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System. exit(0);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}