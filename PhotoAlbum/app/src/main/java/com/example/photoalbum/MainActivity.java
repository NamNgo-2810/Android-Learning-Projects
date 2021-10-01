package com.example.photoalbum;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ImageViewModel imageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv);
        fab = findViewById(R.id.fab);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageAdapter adapter = new ImageAdapter();
        recyclerView.setAdapter(adapter);

        imageViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(ImageViewModel.class);
        imageViewModel.getAllImages().observe(MainActivity.this, adapter::setImages);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddImageActivity.class);
            startActivityForResult(intent, 3);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                imageViewModel.delete(adapter.getPosition(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setListener(image -> {
            Intent intent = new Intent(MainActivity.this, UpdateImageActivity.class);
            intent.putExtra("id", image.getImageId());
            intent.putExtra("title", image.getImageTitle());
            intent.putExtra("description", image.getImageDescription());
            intent.putExtra("imageFile", image.getImageFile());
            startActivityForResult(intent, 4);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3 && resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("title");
            String description = data.getStringExtra("description");
            byte[] imageFile = data.getByteArrayExtra("imageFile");

            Image image = new Image(title, description, imageFile);
            imageViewModel.insert(image);
        }

        else if (requestCode == 4 && resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("updateTitle");
            String description = data.getStringExtra("updateDescription");
            byte[] imageFile = data.getByteArrayExtra("imageFile");
            int id = data.getIntExtra("id", -1);

            Image image = new Image(title, description, imageFile);
            image.setImageId(id);
            imageViewModel.update(image);
        }

    }
}