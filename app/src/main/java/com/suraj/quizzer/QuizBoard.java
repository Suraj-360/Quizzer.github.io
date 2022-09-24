package com.suraj.quizzer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.suraj.quizzer.databinding.ActivityQuizBoardBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class QuizBoard extends AppCompatActivity {

    ActivityQuizBoardBinding activityQuizBoardBinding;
    ArrayList<QuestionModel> questionModelArrayList;
    CountDownTimer countDownTimer;
    int questionIndex = 1;
    int correctAnswer = 0;
    int attempt = 0;
    int timer = 0;
    String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityQuizBoardBinding = ActivityQuizBoardBinding.inflate(getLayoutInflater());
        setContentView(activityQuizBoardBinding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setTitle("Quiz Board");
        Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(getSupportActionBar()))).setBackgroundDrawable(AppCompatResources.getDrawable(this,R.drawable.toolbar_bg));
        Intent receive = getIntent();
        categoryId = receive.getStringExtra("Domain ID");
        questionModelArrayList = new ArrayList<>();

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Categories").document(categoryId).collection("Questions").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                    QuestionModel questionModel = snapshot.toObject(QuestionModel.class);
                    assert questionModel != null;
                    questionModelArrayList.add(questionModel);
                }
                Collections.shuffle(questionModelArrayList);
                activityQuizBoardBinding.question.setText(questionModelArrayList.get(questionIndex - 1).getQuestion());
                activityQuizBoardBinding.option1.setText(questionModelArrayList.get(questionIndex - 1).getOption1());
                activityQuizBoardBinding.option2.setText(questionModelArrayList.get(questionIndex - 1).getOption2());
                activityQuizBoardBinding.option3.setText(questionModelArrayList.get(questionIndex - 1).getOption3());
                activityQuizBoardBinding.option4.setText(questionModelArrayList.get(questionIndex - 1).getOption4());
                activityQuizBoardBinding.options.clearCheck();

            }
        });

        timer = 30000;
        countDownTimer = new CountDownTimer(timer, 1000) {

            public void onTick(long millisUntilFinished) {
                String time = Long.toString(millisUntilFinished / 1000);
                activityQuizBoardBinding.questionCountDown.setText(time);
            }

            public void onFinish() {
                nextQuestionSet();
            }

        }.start();

        activityQuizBoardBinding.quitQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuizBoard.this);
                builder.setMessage("Do you want to exit quiz ?");
                builder.setTitle("Alert exit quiz!");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent startQuizIntent = new Intent(QuizBoard.this, MainActivity.class);
                        startActivity(startQuizIntent);
                        finish();
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

        });



        optionBackgroundChange();

        activityQuizBoardBinding.nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestionSet();
            }
        });

    }

    private void optionBackgroundChange() {
        activityQuizBoardBinding.option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityQuizBoardBinding.option1.setBackgroundResource(R.drawable.selected_option_bg);
                activityQuizBoardBinding.option2.setBackgroundResource(R.drawable.option_background);
                activityQuizBoardBinding.option3.setBackgroundResource(R.drawable.option_background);
                activityQuizBoardBinding.option4.setBackgroundResource(R.drawable.option_background);
            }
        });

        activityQuizBoardBinding.option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityQuizBoardBinding.option1.setBackgroundResource(R.drawable.option_background);
                activityQuizBoardBinding.option2.setBackgroundResource(R.drawable.selected_option_bg);
                activityQuizBoardBinding.option3.setBackgroundResource(R.drawable.option_background);
                activityQuizBoardBinding.option4.setBackgroundResource(R.drawable.option_background);
            }
        });

        activityQuizBoardBinding.option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityQuizBoardBinding.option1.setBackgroundResource(R.drawable.option_background);
                activityQuizBoardBinding.option2.setBackgroundResource(R.drawable.option_background);
                activityQuizBoardBinding.option3.setBackgroundResource(R.drawable.selected_option_bg);
                activityQuizBoardBinding.option4.setBackgroundResource(R.drawable.option_background);
            }
        });
        activityQuizBoardBinding.option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityQuizBoardBinding.option1.setBackgroundResource(R.drawable.option_background);
                activityQuizBoardBinding.option2.setBackgroundResource(R.drawable.option_background);
                activityQuizBoardBinding.option3.setBackgroundResource(R.drawable.option_background);
                activityQuizBoardBinding.option4.setBackgroundResource(R.drawable.selected_option_bg);
            }
        });
    }

    private void nextQuestionSet() {
        // 1<=4
        if (questionIndex <= questionModelArrayList.size() && questionIndex <=10) {
            if (activityQuizBoardBinding.options.getCheckedRadioButtonId() != -1) {
                attempt++;
                RadioButton radioButton = (RadioButton) findViewById(activityQuizBoardBinding.options.getCheckedRadioButtonId());
                String userAnswer = radioButton.getText().toString();
                if (userAnswer.equals(questionModelArrayList.get(questionIndex - 1).answer)) {
                    ++correctAnswer;
                }
            }
            countDownTimer.cancel();
            ++questionIndex;
        }
        clearOptionBackground();
        if (questionIndex <= questionModelArrayList.size() && questionIndex <=10) {
            activityQuizBoardBinding.question.setText(questionModelArrayList.get(questionIndex - 1).getQuestion());
            activityQuizBoardBinding.option1.setText(questionModelArrayList.get(questionIndex - 1).getOption1());
            activityQuizBoardBinding.option2.setText(questionModelArrayList.get(questionIndex - 1).getOption2());
            activityQuizBoardBinding.option3.setText(questionModelArrayList.get(questionIndex - 1).getOption3());
            activityQuizBoardBinding.option4.setText(questionModelArrayList.get(questionIndex - 1).getOption4());
            activityQuizBoardBinding.options.clearCheck();
            timer = 30000;
            countDownTimer.start();
        } else {
            countDownTimer.cancel();
            Intent resultIntent = new Intent(QuizBoard.this, ResultActivity.class);
            resultIntent.putExtra("Correct Answer", correctAnswer);
            resultIntent.putExtra("Attempt Question",attempt);
            startActivity(resultIntent);
        }
    }

    private void clearOptionBackground() {
        activityQuizBoardBinding.option1.setBackgroundResource(R.drawable.option_background);
        activityQuizBoardBinding.option2.setBackgroundResource(R.drawable.option_background);
        activityQuizBoardBinding.option3.setBackgroundResource(R.drawable.option_background);
        activityQuizBoardBinding.option4.setBackgroundResource(R.drawable.option_background);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // Not calling **super**, disables back button in current screen.
        Toast.makeText(QuizBoard.this, "Not allow back button here", Toast.LENGTH_SHORT).show();
    }
}