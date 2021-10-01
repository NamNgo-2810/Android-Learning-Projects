package com.example.flagquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class QuizActivity extends AppCompatActivity {

    TextView textViewCorrect, textViewWrong, textViewEmpty, textViewQuestion;
    ImageView imageViewFlag;
    Button buttonA, buttonB, buttonC, buttonD;
    ImageButton imageButtonNext;

    FlagDatabase flagDatabase;
    ArrayList<FlagModel> questionList;

    int correct = 0, wrong = 0, empty = 0, question = 0;

    FlagModel correctFlag;
    ArrayList<FlagModel> wrongOptions;
    Set<FlagModel> mixOptions = new HashSet<>();
    ArrayList<FlagModel> options;

    boolean buttonControl = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewCorrect = findViewById(R.id.textViewCorrect);
        textViewWrong = findViewById(R.id.textViewWrong);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        textViewQuestion = findViewById(R.id.textViewQuestion);

        imageViewFlag = findViewById(R.id.imageViewFlag);
        buttonA = findViewById(R.id.buttonA);
        buttonB = findViewById(R.id.buttonB);
        buttonC = findViewById(R.id.buttonC);
        buttonD = findViewById(R.id.buttonD);

        flagDatabase = new FlagDatabase(QuizActivity.this);
        questionList = new FlagDAO().getRandomThreeQuestion(flagDatabase);

        loadQuestion();

        imageButtonNext = findViewById(R.id.imageButtonNext);

        buttonA.setOnClickListener(v -> answerControl(buttonA));
        buttonB.setOnClickListener(v -> answerControl(buttonB));
        buttonC.setOnClickListener(v -> answerControl(buttonC));
        buttonD.setOnClickListener(v -> answerControl(buttonD));

        imageButtonNext.setOnClickListener(v -> {
            question++;

            if (!buttonControl && question < 3) {
                empty++;
                textViewEmpty.setText("Empty: " + empty);
                loadQuestion();
            }
            else if (buttonControl && question < 3) {
                loadQuestion();

                buttonA.setClickable(true);
                buttonB.setClickable(true);
                buttonC.setClickable(true);
                buttonD.setClickable(true);

                buttonA.setBackgroundColor(Color.parseColor("#3F51B5"));
                buttonB.setBackgroundColor(Color.parseColor("#3F51B5"));
                buttonC.setBackgroundColor(Color.parseColor("#3F51B5"));
                buttonD.setBackgroundColor(Color.parseColor("#3F51B5"));
            }
            else if (question == 3) {
                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                intent.putExtra("correct", correct);
                intent.putExtra("wrong", wrong);
                intent.putExtra("empty", empty);
                startActivity(intent);
                finish();
            }

            buttonControl = false;
        });

    }

    @SuppressLint("SetTextI18n")
    public void loadQuestion() {
        textViewQuestion.setText("Question: " + question);
        correctFlag = questionList.get(question);
        imageViewFlag.setImageResource(getResources().getIdentifier(correctFlag.getFlag(), "drawable", getPackageName()));
        wrongOptions = new FlagDAO().getRandomThreeWrongOptions(flagDatabase, correctFlag.getIndex());
        mixOptions.clear();
        mixOptions.add(correctFlag);
        mixOptions.addAll(wrongOptions);

        options.clear();
        options.addAll(mixOptions);
        buttonA.setText(options.get(0).getName());
        buttonB.setText(options.get(1).getName());
        buttonC.setText(options.get(2).getName());
        buttonD.setText(options.get(3).getName());
    }

    @SuppressLint("SetTextI18n")
    public void answerControl(Button button) {
        String buttonText = button.getText().toString();
        String correctAnswer = correctFlag.getName();

        if (buttonText.equals(correctAnswer)) {
            correct++;
            button.setBackgroundColor(Color.GREEN);
        }
        else {
            wrong++;
            button.setBackgroundColor(Color.RED);
            if (buttonA.getText().toString().equals(correctAnswer)) {
                buttonA.setBackgroundColor(Color.GREEN);
            }
            else if (buttonB.getText().toString().equals(correctAnswer)) {
                buttonB.setBackgroundColor(Color.GREEN);
            }
            else if (buttonC.getText().toString().equals(correctAnswer)) {
                buttonC.setBackgroundColor(Color.GREEN);
            }
            else if (buttonD.getText().toString().equals(correctAnswer)) {
                buttonD.setBackgroundColor(Color.GREEN);
            }
        }

        buttonA.setClickable(false);
        buttonB.setClickable(false);
        buttonC.setClickable(false);
        buttonD.setClickable(false);

        textViewCorrect.setText("Correct: " + correct);
        textViewWrong.setText("Wrong: " + wrong);

        buttonControl = true;
    }

}