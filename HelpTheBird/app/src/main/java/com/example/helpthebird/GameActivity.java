package com.example.helpthebird;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    ImageView bird, enemy1, enemy2, coin1, coin2, right1, right2, right3;
    TextView textViewScore, textViewInfo;
    ConstraintLayout constraintLayout;

    boolean touchControl = false;
    boolean beginControl = false;

    Runnable runnable;
    Handler handler;
    MediaPlayer mediaPlayer;

    // Position
    int birdX, enemy1X, enemy2X, coin1X, coin2X;
    int birdY, enemy1Y, enemy2Y, coin1Y, coin2Y;

    // Dimensions of screen
    int screenWidth;
    int screenHeight;

    // Remaining right
    int right = 3;
    int score = 0;

    int up = 1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        bird = findViewById(R.id.imageViewBird);
        enemy1 = findViewById(R.id.imageViewEnemy1);
        enemy2 = findViewById(R.id.imageViewEnemy2);
        coin1 = findViewById(R.id.imageViewCoin1);
        coin2 = findViewById(R.id.imageViewCoin2);
        right1 = findViewById(R.id.right1);
        right2 = findViewById(R.id.right2);
        right3 = findViewById(R.id.right3);

        textViewScore = findViewById(R.id.textViewScore);
        textViewInfo = findViewById(R.id.textViewStartInfo);

        mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.collision);
        constraintLayout = findViewById(R.id.constrainLayout);

        constraintLayout.setOnTouchListener((view, motionEvent) -> {
            textViewInfo.setVisibility(View.INVISIBLE);

            if (!beginControl) {
                beginControl = true;
                screenWidth = constraintLayout.getWidth();
                screenHeight = constraintLayout.getHeight();

                birdX = (int) bird.getX();
                birdY = (int) bird.getY();

                handler = new Handler();
                runnable = () -> {
                    moveToBird();
                    entityControl();
                    collisionControl();

                    if (up == 1) {
                        bird.setImageResource(R.drawable.bird_1);
                        enemy1.setImageResource(R.drawable.red_bee_2);
                        enemy2.setImageResource(R.drawable.skeleton_fly_00);
                        up++;
                    }
                    else {
                        bird.setImageResource(R.drawable.bird_2);
                        enemy1.setImageResource(R.drawable.red_bee_1);
                        enemy2.setImageResource(R.drawable.skeleton_fly_02);
                        up--;
                    }


                };
                handler.post(runnable);
            }
            else {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    touchControl = true;

                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    touchControl = false;
                }
            }

            return true;
        });
    }

    public void moveToBird() {
        if (touchControl) birdY -= screenHeight / 50;
        else birdY += screenHeight / 50;


        if (birdY <= 0 ) birdY = 0;
        else if (birdY >= screenHeight - bird.getHeight()) {
            birdY = screenHeight - bird.getHeight();
        }

        bird.setY(birdY);
    }

    public void entityControl() {
        enemy1.setVisibility(View.VISIBLE);
        enemy2.setVisibility(View.VISIBLE);
        coin1.setVisibility(View.VISIBLE);
        coin2.setVisibility(View.VISIBLE);

        enemy1X -= screenWidth / 150;
        if (enemy1X < 0) {
            enemy1X = screenWidth + 200;
            enemy1Y = (int) Math.floor(Math.random() * screenHeight);
            if (enemy1Y <= 0 ) enemy1Y = 0;
            else if (enemy1Y >= screenHeight - enemy1.getHeight()) {
                enemy1Y = screenHeight - enemy1.getHeight();
            }
        }

        enemy1.setX(enemy1X);
        enemy1.setY(enemy1Y);

        enemy2X -= screenWidth / 120;
        if (enemy2X < 0) {
            enemy2X = screenWidth + 200;
            enemy2Y = (int) Math.floor(Math.random() * screenHeight);
            if (enemy2Y <= 0 ) enemy2Y = 0;
            else if (enemy2Y >= screenHeight - enemy2.getHeight()) {
                enemy2Y = screenHeight - enemy2.getHeight();
            }
        }

        enemy2.setX(enemy2X);
        enemy2.setY(enemy2Y);

        coin1X -= screenWidth / 120;
        if (coin1X < 0) {
            coin1X = screenWidth + 200;
            coin1Y = (int) Math.floor(Math.random() * screenHeight);
            if (coin1Y <= 0 ) coin1Y = 0;
            else if (coin1Y >= screenHeight - coin1.getHeight()) {
                coin1Y = screenHeight - coin1.getHeight();
            }
        }

        coin1.setX(coin1X);
        coin1.setY(coin1Y);

        coin2X -= screenWidth / 110;
        if (coin2X < 0) {
            coin2X = screenWidth + 200;
            coin2Y = (int) Math.floor(Math.random() * screenHeight);
            if (coin2Y <= 0 ) coin2Y = 0;
            else if (coin2Y >= screenHeight - coin2.getHeight()) {
                coin2Y = screenHeight - coin2.getHeight();
            }
        }

        coin2.setX(coin2X);
        coin2.setY(coin2Y);
    }

    @SuppressLint("SetTextI18n")
    public void collisionControl() {
        int centerEnemy1X = enemy1X + enemy1.getWidth()/2;
        int centerEnemy1Y = enemy1Y + enemy1.getHeight()/2;

        if (centerEnemy1X >= birdX && centerEnemy1X <= birdX + bird.getWidth()
                && centerEnemy1Y >= birdY && centerEnemy1Y <= birdY + bird.getHeight()) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(0);
            }
            mediaPlayer.start();
            enemy1X = screenWidth + 200;
            right--;
        }

        int centerEnemy2X = enemy2X + enemy2.getWidth()/2;
        int centerEnemy2Y = enemy2Y + enemy2.getHeight()/2;

        if (centerEnemy2X >= birdX && centerEnemy2X <= birdX + bird.getWidth()
                && centerEnemy2Y >= birdY && centerEnemy2Y <= birdY + bird.getHeight()) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(0);
            }
            mediaPlayer.start();
            enemy2X = screenWidth + 200;
            right--;
        }

        int centerCoin1X = coin1X + coin1.getWidth()/2;
        int centerCoin1Y = coin1Y + coin1.getHeight()/2;

        if (centerCoin1X >= birdX && centerCoin1X <= birdX + bird.getWidth()
                && centerCoin1Y >= birdY && centerCoin1Y <= birdY + bird.getHeight()) {
            coin1X = screenWidth + 200;
            score += 10;
            textViewScore.setText("" + score);
        }

        int centerCoin2X = coin2X + coin2.getWidth()/2;
        int centerCoin2Y = coin2Y + coin2.getHeight()/2;

        if (centerCoin2X >= birdX && centerCoin2X <= birdX + bird.getWidth()
                && centerCoin2Y >= birdY && centerCoin2Y <= birdY + bird.getHeight()) {
            enemy1X = screenWidth + 200;
            score += 10;
            textViewScore.setText("" + score);
        }

        if (right > 0 && score < 200) {
            if (right == 2) right1.setImageResource(R.drawable.favorite_grey);
            else if (right == 1) right2.setImageResource(R.drawable.favorite_grey);
            handler.postDelayed(runnable, 20);
        }
        else if (right == 0) {
            handler.removeCallbacks(runnable);
            right3.setImageResource(R.drawable.favorite_grey);
            Intent intent = new Intent(GameActivity.this, ResultActivity.class);
            intent.putExtra("score", score);
            startActivity(intent);
        }
    }

}