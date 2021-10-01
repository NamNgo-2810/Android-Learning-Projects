package com.example.helpthebird;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView bird, enemy1, enemy2, coin, volume;
    Button buttonStart;

    Animation animation;
    MediaPlayer mediaPlayer;
    boolean audioStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bird = findViewById(R.id.bird);
        enemy1 = findViewById(R.id.red_bee);
        enemy2 = findViewById(R.id.blue_bat);
        coin = findViewById(R.id.coin);
        volume = findViewById(R.id.volume);
        buttonStart = findViewById(R.id.buttonStart);

        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_animation);
        bird.setAnimation(animation);
        enemy1.setAnimation(animation);
        enemy2.setAnimation(animation);
        coin.setAnimation(animation);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.audio_for_game);
        mediaPlayer.start();

        volume.setOnClickListener(v -> {
            if (!audioStatus) {
                mediaPlayer.setVolume(0, 0);
                volume.setImageResource(R.drawable.volume_off);
                audioStatus = true;
            }
            else {
                mediaPlayer.setVolume(1, 1);
                volume.setImageResource(R.drawable.volume_up);
                audioStatus = false;
            }
        });

        buttonStart.setOnClickListener(v -> {
            mediaPlayer.reset();
            volume.setImageResource(R.drawable.volume_up);

            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        });
    }
}