package com.suraj.quizzer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.suraj.quizzer.databinding.ActivityResultBinding;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding activityResultBinding;
    String attempt,result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityResultBinding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(activityResultBinding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle("Quiz Result");
        Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getSupportActionBar()))).setBackgroundDrawable(AppCompatResources.getDrawable(this,R.drawable.toolbar_bg));

        Intent intent = getIntent();

        result = Integer.toString(intent.getIntExtra("Correct Answer",0));
        attempt = Integer.toString(intent.getIntExtra("Attempt Question",0));
        CircularProgressIndicator circularProgress = findViewById(R.id.circular_progress);

        if(Integer.parseInt(result)==0)
        {
            circularProgress.setProgress(0.0001, 5);
        }
        else
        {
            circularProgress.setProgress(Integer.parseInt(result), 10);
        }

        if(Integer.parseInt(result)>=6)
        {
            celebration();
        }

        setValuesOnView();
        activityResultBinding.backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHome = new Intent(ResultActivity.this,MainActivity.class);
                startActivity(backHome);
                finish();
            }
        });
    }

    private static final CircularProgressIndicator.ProgressTextAdapter TIME_TEXT_ADAPTER = new CircularProgressIndicator.ProgressTextAdapter() {
        @NonNull
        @Override
        public String formatText(double time)
        {
            return Double.toString(time);
        }
    };

    private void celebration()
    {
        KonfettiView konfettiView = (KonfettiView) findViewById(R.id.konfettiView);
        EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
        Party party = new PartyFactory(emitterConfig)
                .angle(270)
                .spread(90)
                .setSpeedBetween(1f, 5f)
                .timeToLive(5000L)
                .shapes(Shape.Square.INSTANCE,Shape.Circle.INSTANCE)
                .sizes(new nl.dionsegijn.konfetti.core.models.Size(12,5f,0.2f))
                .position(0.0, 0.0, 1.0, 0.0)
                .build();
        konfettiView.start(party);
    }

    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
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

    private void setValuesOnView(){

        int totalQuestion = 10;
        int wrong = totalQuestion - Integer.parseInt(result);
        String score = "Your Score " + Integer.toString(Integer.parseInt(result)*5);
        activityResultBinding.yourScore.setText(score);
        activityResultBinding.totalQuestionResult.setText(String.valueOf(totalQuestion));
        activityResultBinding.totalAttemptedResult.setText(attempt);
        activityResultBinding.totalRightQuestionResult.setText(result);
        activityResultBinding.totalWrongQuestionResult.setText(String.valueOf(wrong));
        activityResultBinding.totalUnAttemptedQuestionResult.setText(String.valueOf(totalQuestion-Integer.parseInt(attempt)));

        if(Integer.parseInt(result)<=3)
        {
            activityResultBinding.resultMedal.setImageResource(R.drawable.broze);
        }
        else if(Integer.parseInt(result)>3 && Integer.parseInt(result)<=7)
        {
            activityResultBinding.resultMedal.setImageResource(R.drawable.silver);
        }
        else
        {
            activityResultBinding.resultMedal.setImageResource(R.drawable.gold);
        }
    }
}