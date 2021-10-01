package com.example.flagquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView textViewTotalCorrect, textViewTotalWrong, textViewTotalEmpty, textViewSuccessRate;
    Button buttonPlayAgain, buttonQuit;
    int correct, wrong, empty;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textViewTotalCorrect = findViewById(R.id.textViewTotalCorrect);
        textViewTotalWrong = findViewById(R.id.textViewTotalWrong);
        textViewTotalEmpty = findViewById(R.id.textViewTotalEmpty);
        textViewSuccessRate = findViewById(R.id.textViewSuccessRate);

        buttonPlayAgain = findViewById(R.id.buttonPlayAgain);
        buttonQuit = findViewById(R.id.buttonQuit);

        correct = getIntent().getIntExtra("correct", 0);
        wrong = getIntent().getIntExtra("wrong", 0);
        empty = getIntent().getIntExtra("empty", 0);

        textViewTotalCorrect.setText("Total Correct Answer: " + correct);
        textViewTotalWrong.setText("Total Wrong Answer: " + wrong);
        textViewTotalEmpty.setText("Total Empty Answer: " + empty);
        textViewSuccessRate.setText("Success Rate: " + correct + "/3");

        buttonPlayAgain.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        buttonQuit.setOnClickListener(v -> {
            Intent newIntent = new Intent(Intent.ACTION_MAIN);
            newIntent.addCategory(Intent.CATEGORY_HOME);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            finish();
        });

    }
}