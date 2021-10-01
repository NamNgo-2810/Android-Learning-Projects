package com.example.balloonburst;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView textViewInfo, textViewMyScore, textViewHighestScore;
    Button buttonPlayAgain, buttonQuitGame;

    int myScore;

    SharedPreferences sharedPreferences; // Save highest score

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textViewInfo = findViewById(R.id.textViewInfo);
        textViewMyScore = findViewById(R.id.textViewMyScore);
        textViewHighestScore = findViewById(R.id.textViewHighest);

        buttonPlayAgain = findViewById(R.id.buttonPlayAgain);
        buttonQuitGame = findViewById(R.id.buttonQuitGame);

        myScore = getIntent().getIntExtra("score", 0);
        textViewMyScore.setText(myScore);

        sharedPreferences = this.getSharedPreferences("Score", Context.MODE_PRIVATE);
        int highestScore = sharedPreferences.getInt("highestScore", 0);

        if (myScore > highestScore) {
            sharedPreferences.edit().putInt("highestScore", myScore).apply();
            highestScore = myScore;
            textViewInfo.setText("Congratulations. The new high score. Do you want to get better scores?");
        }
        else if (myScore > 10) textViewInfo.setText("You must get a little faster!");
        else if ((highestScore - myScore) > 3 && (highestScore - myScore) <= 10) {
            textViewInfo.setText("Good. How about getting a little faster?");
        }
        else if (highestScore - myScore <= 3) {
            textViewInfo.setText("Excellent. You are so close to get the new record.");
        }
        textViewHighestScore.setText("Highest Score: " + highestScore);

        buttonPlayAgain.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        buttonQuitGame.setOnClickListener(v -> {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        });
    }
}