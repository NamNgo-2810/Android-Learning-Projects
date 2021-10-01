package com.example.balloonburst;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView textViewTime, textViewCountDown, textViewScore;
    ImageView balloon1, balloon2, balloon3, balloon4, balloon5, balloon6, balloon7, balloon8, balloon9;
    GridLayout gridLayout;
    int score = 0;

    Runnable runnable;
    Handler handler;

    ImageView[] balloonsArray;
    MediaPlayer mediaPlayer;

    boolean volumeStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTime = findViewById(R.id.textViewTime);
        textViewCountDown = findViewById(R.id.textViewCountDown);
        textViewScore = findViewById(R.id.textViewScore);

        balloon1 = findViewById(R.id.balloon1);
        balloon2 = findViewById(R.id.balloon2);
        balloon3 = findViewById(R.id.balloon3);
        balloon4 = findViewById(R.id.balloon4);
        balloon5 = findViewById(R.id.balloon5);
        balloon6 = findViewById(R.id.balloon6);
        balloon7 = findViewById(R.id.balloon7);
        balloon8 = findViewById(R.id.balloon8);
        balloon9 = findViewById(R.id.balloon9);

        gridLayout = findViewById(R.id.gridLayout);

        balloonsArray = new ImageView[] {
                balloon1, balloon2, balloon3,
                balloon4, balloon5, balloon6,
                balloon7, balloon8, balloon9
        };
        mediaPlayer = MediaPlayer.create(this, R.raw.balloon_sound);

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewCountDown.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {

                balloonsControl();

                new CountDownTimer(30000, 1000) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTick(long millisUntilFinished) {
                        textViewTime.setText("Remaining Time: " + millisUntilFinished/1000);
                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                        intent.putExtra("score", score);
                        startActivity(intent);
                    }
                }.start();
            }
        }.start();

    }

    @SuppressLint("SetTextI18n")
    public void increaseScoreByOne(View view) {
        score++;
        textViewScore.setText("Score: " + score);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
        }
        mediaPlayer.start();

        if (view.getId() == balloon1.getId()) balloon1.setImageResource(R.drawable.boom);
        else if (view.getId() == balloon2.getId()) balloon2.setImageResource(R.drawable.boom);
        else if (view.getId() == balloon3.getId()) balloon3.setImageResource(R.drawable.boom);
        else if (view.getId() == balloon4.getId()) balloon4.setImageResource(R.drawable.boom);
        else if (view.getId() == balloon5.getId()) balloon5.setImageResource(R.drawable.boom);
        else if (view.getId() == balloon6.getId()) balloon6.setImageResource(R.drawable.boom);
        else if (view.getId() == balloon7.getId()) balloon7.setImageResource(R.drawable.boom);
        else if (view.getId() == balloon8.getId()) balloon8.setImageResource(R.drawable.boom);
        else if (view.getId() == balloon9.getId()) balloon9.setImageResource(R.drawable.boom);
    }

    public void balloonsControl() {
        textViewCountDown.setVisibility(View.INVISIBLE);
        textViewTime.setVisibility(View.VISIBLE);
        textViewScore.setVisibility(View.VISIBLE);
        gridLayout.setVisibility(View.VISIBLE);

        handler = new Handler();
        runnable = () -> {
            for (ImageView balloon: balloonsArray) {
                balloon.setVisibility(View.INVISIBLE);
                balloon.setImageResource(R.drawable.balloon);
            }
            gridLayout.setVisibility(View.VISIBLE);

            Random random = new Random();
            int i = random.nextInt(balloonsArray.length);
            balloonsArray[i].setVisibility(View.VISIBLE);


            if (score <= 5) {
                handler.postDelayed(runnable, 2000);
            }
            else if (score <= 10) {
                handler.postDelayed(runnable, 1500);
            }
            else if (score <= 15) {
                handler.postDelayed(runnable, 1000);
            }
            else {
                handler.postDelayed(runnable, 500);
            }

        };

        handler.post(runnable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.volume) {
            if (!volumeStatus) {
                mediaPlayer.setVolume(0, 0);
                item.setIcon(R.drawable.volume_off);
                volumeStatus = true;
            }
            else {
                mediaPlayer.setVolume(1, 1);
                item.setIcon(R.drawable.volume_up);
                volumeStatus = false;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}