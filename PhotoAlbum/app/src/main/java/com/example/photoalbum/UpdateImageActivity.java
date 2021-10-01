package com.example.photoalbum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class UpdateImageActivity extends AppCompatActivity {

    private ImageView imageViewUpdateImage;
    private EditText editTextUpdateTitle, editTextUpdateDescription;
    private Button buttonUpdate;

    private int id;
    private String title, description;
    private byte[] imageFile;

    private Bitmap selectedImage;
    private Bitmap scaledImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Update Image");
        setContentView(R.layout.activity_update_image);

        imageViewUpdateImage = findViewById(R.id.imageViewUpdateImage);
        editTextUpdateTitle = findViewById(R.id.editTextUpdateTitle);
        editTextUpdateDescription = findViewById(R.id.editTextUpdateDescription);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        imageFile = getIntent().getByteArrayExtra("imageFile");

        editTextUpdateTitle.setText(title);
        editTextUpdateDescription.setText(description);
        imageViewUpdateImage.setImageBitmap(BitmapFactory.decodeByteArray(imageFile, 0, imageFile.length));

        imageViewUpdateImage.setOnClickListener(v -> {
            Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(imageIntent, 5);
        });

        buttonUpdate.setOnClickListener(v -> updateData());

    }

    public void updateData() {
        if (id == -1) {
            Toast.makeText(UpdateImageActivity.this, "These is a problem!", Toast.LENGTH_SHORT).show();
        }
        else {
            String updateTitle = editTextUpdateTitle.getText().toString();
            String updateDescription = editTextUpdateDescription.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("updateTitle", updateTitle);
            intent.putExtra("updateDescription", updateDescription);

            if (selectedImage == null) {
                intent.putExtra("imageFile", imageFile);
            }
            else {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                scaledImage = makeSmall(selectedImage, 300);
                scaledImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                byte[] imageFile = outputStream.toByteArray();
                intent.putExtra("imageFile", imageFile);
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 5 && resultCode == RESULT_OK && data != null) {
            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), data.getData());
                    selectedImage = ImageDecoder.decodeBitmap(source);
                }
                else selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                imageViewUpdateImage.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public Bitmap makeSmall(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float ratio = (float) width / (float) height;
        if (ratio > 1) {
            width = maxSize;
            height = (int) (width / ratio);
        }
        else {
            height = maxSize;
            width = (int) (height * ratio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
