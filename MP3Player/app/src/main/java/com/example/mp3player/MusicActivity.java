package com.example.mp3player;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {

    Button buttonPlayPause, buttonPrevious, buttonNext;
    TextView textViewFileNameMusic, textViewProgress, textViewTotalTime;
    SeekBar seekBarVolume, seekBarMusic;

    String title, filePath;
    int position;
    ArrayList<String> list;

    MediaPlayer mediaPlayer;

    Runnable runnable;
    Handler handler;
    int totalTime;

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        buttonPlayPause = findViewById(R.id.buttonPlayPause);
        buttonPrevious = findViewById(R.id.buttonPrevious);
        buttonNext = findViewById(R.id.buttonNext);

        textViewFileNameMusic = findViewById(R.id.textViewFileNameMusic);
        textViewProgress = findViewById(R.id.textViewProgress);
        textViewTotalTime = findViewById(R.id.textViewTotalTime);

        seekBarVolume = findViewById(R.id.volumeSeekBar);
        seekBarMusic = findViewById(R.id.musicSeekBar);

        animation = AnimationUtils.loadAnimation(MusicActivity.this, R.anim.translate_animation);
        textViewFileNameMusic.setAnimation(animation);

        title = getIntent().getStringExtra("title");
        filePath = getIntent().getStringExtra("filepath");
        position = getIntent().getIntExtra("position", 0);
        list = getIntent().getStringArrayListExtra("list");

        textViewFileNameMusic.setText(title);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        buttonPrevious.setOnClickListener(v -> {
            mediaPlayer.reset();
            position = position > 0 ? position - 1 : list.size() - 1;
            String newFilePath = list.get(position);

            try {
                mediaPlayer.setDataSource(newFilePath);
                mediaPlayer.prepare();
                mediaPlayer.start();
                buttonPlayPause.setBackgroundResource(R.drawable.pause);
                String newTitle = newFilePath.substring(newFilePath.lastIndexOf("/") + 1);
                textViewFileNameMusic.setText(newTitle);
                textViewFileNameMusic.clearAnimation();
                textViewFileNameMusic.startAnimation(animation);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        buttonPlayPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                buttonPlayPause.setBackgroundResource(R.drawable.play_arrow);
            }
            else {
                mediaPlayer.start();
                buttonPlayPause.setBackgroundResource(R.drawable.pause);
            }
        });

        buttonNext.setOnClickListener(v -> {
            mediaPlayer.reset();
            position = position < list.size() - 1 ? position + 1 : 0;
            String newFilePath = list.get(position);

            try {
                mediaPlayer.setDataSource(newFilePath);
                mediaPlayer.prepare();
                mediaPlayer.start();
                buttonPlayPause.setBackgroundResource(R.drawable.pause);
                String newTitle = newFilePath.substring(newFilePath.lastIndexOf("/") + 1);
                textViewFileNameMusic.setText(newTitle);
                textViewFileNameMusic.clearAnimation();
                textViewFileNameMusic.startAnimation(animation);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekBarVolume.setProgress(progress);
                    float volumeLevel = progress / 100f;
                    mediaPlayer.setVolume(volumeLevel, volumeLevel);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    seekBarMusic.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler = new Handler();
        runnable = () -> {
            totalTime = mediaPlayer.getDuration();
            seekBarMusic.setMax(totalTime);

            int currentPosition = mediaPlayer.getCurrentPosition();
            seekBarMusic.setProgress(currentPosition);
            handler.postDelayed(runnable, 1000);

            String elapsedTime = createTimeLabel(currentPosition);
            String lastTime = createTimeLabel(totalTime);

            textViewProgress.setText(elapsedTime);
            textViewTotalTime.setText(lastTime);

            if (elapsedTime.equals(lastTime)) {
                mediaPlayer.reset();
                position = position < list.size() - 1 ? position + 1 : 0;
                String newFilePath = list.get(position);

                try {
                    mediaPlayer.setDataSource(newFilePath);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    buttonPlayPause.setBackgroundResource(R.drawable.pause);
                    String newTitle = newFilePath.substring(newFilePath.lastIndexOf("/") + 1);
                    textViewFileNameMusic.setText(newTitle);

                    textViewFileNameMusic.clearAnimation();
                    textViewFileNameMusic.startAnimation(animation);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        handler.post(runnable);
    }

    public String createTimeLabel(int currentPosition) {
        String timeLabel;
        int minute, second;

        minute = currentPosition / 1000 / 60;
        second = currentPosition / 1000 % 60;

        timeLabel = minute + (second < 10 ? ":0" : ":") + second;

        return timeLabel;
    }
}