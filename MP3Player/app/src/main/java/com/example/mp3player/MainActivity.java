package com.example.mp3player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> songList = new ArrayList<>();
    MusicAdapter adapter;

    private static final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("Media path", MEDIA_PATH);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        else {
            getAllAudioFiles();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getAllAudioFiles();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getAllAudioFiles() {
        if (MEDIA_PATH != null) {
            File mainFile = new File(MEDIA_PATH);
            File[] fileList = mainFile.listFiles();
            assert fileList != null;
            for (File file: fileList) {
                Log.e("Media path", file.toString());
                if (file.isDirectory()) {
                    scanDirectory(file);
                }
                else {
                    String path = file.getAbsolutePath();
                    if (path.endsWith(".mp3")) {
                        songList.add(path);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }

        adapter = new MusicAdapter(songList, MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    public void scanDirectory(File directory) {
        assert directory != null;
        File[] fileList = directory.listFiles();

        assert fileList != null;
        for (File file: fileList) {
            Log.e("Media path", file.toString());
            if (file.isDirectory()) {
                scanDirectory(file);
            }
            else {
                String path = file.getAbsolutePath();
                if (path.endsWith(".mp3")) {
                    songList.add(path);
                }
            }
        }
    }

}