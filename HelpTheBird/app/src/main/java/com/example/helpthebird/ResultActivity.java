package com.example.helpthebird;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView textViewInfo, textViewMyScore, textViewHighestScore;
    Button buttonAgain;

    int score;

    SharedPreferences sharedPreferences;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textViewInfo = findViewById(R.id.textViewInfo);
        textViewMyScore = findViewById(R.id.textViewMyScore);
        textViewHighestScore = findViewById(R.id.textViewHighestScore);
        buttonAgain = findViewById(R.id.buttonPlayAgain);

        score = getIntent().getIntExtra("score", 0);
        textViewMyScore.setText("Your Score: " + score);

        sharedPreferences = this.getSharedPreferences("Score", Context.MODE_PRIVATE);
        int highestScore = sharedPreferences.getInt("highestScore", 0);

        if (score > highestScore) {
            textViewInfo.setText("New record: " + score);
            textViewHighestScore.setText("Highest Score: " + score);
            sharedPreferences.edit().putInt("highestScore", score).apply();
        }
        else {
            textViewInfo.setText("You lose");
            textViewHighestScore.setText("Highest Score: " + highestScore);
        }

        buttonAgain.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
        builder.setTitle("Help The Bird");
        builder.setMessage("Are you sure you want to quit the game?");
        builder.setCancelable(false);
        builder.setNegativeButton("Quit game", (dialogInterface, i) -> {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        });
        builder.setPositiveButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
        builder.create().show();
    }
}