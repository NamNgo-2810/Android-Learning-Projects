package com.example.cheat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    ImageView imageViewProfilePicture;
    EditText editTextUsername;
    Button buttonUpdate;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth auth;
    FirebaseUser user;

    Uri imageUri;
    boolean imageControl = false;

    String name, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageViewProfilePicture = findViewById(R.id.imageViewNewProfilePicture);
        editTextUsername = findViewById(R.id.editTextNewUserName);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        getUserInfo();

        imageViewProfilePicture.setOnClickListener(v -> imageChooser());

        buttonUpdate.setOnClickListener(v -> updateProfile());

    }

    public void getUserInfo() {
        databaseReference.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = Objects.requireNonNull(snapshot.child("userName").getValue()).toString();
                image = Objects.requireNonNull(snapshot.child("image").getValue()).toString();
                editTextUsername.setText(name);
                if (image.equals("null")) {
                    imageViewProfilePicture.setImageResource(R.drawable.ic_baseline_account_circle_24);
                }
                else {
                    Picasso.get().load(image).into(imageViewProfilePicture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateProfile() {
        String username = editTextUsername.getText().toString();
        databaseReference.child("Users").child(user.getUid()).child("userName").setValue(username);
        if (imageControl) {
            UUID randomID = UUID.randomUUID();
            String imageName = "images/" + randomID + ".jpg";
            storageReference.child(imageName)
                    .putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        StorageReference ref = storage.getReference(imageName);
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            String filePath = uri.toString();
                            databaseReference.child("Users")
                                    .child(Objects.requireNonNull(auth.getUid()))
                                    .child("image")
                                    .setValue(filePath)
                                    .addOnSuccessListener(unused -> Toast.makeText(ProfileActivity.this, "Write to database is successful.", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Write to database is not successful.", Toast.LENGTH_SHORT).show());
                        });
                    });
        }
        else {
            databaseReference.child("Users")
                    .child(Objects.requireNonNull(auth.getUid()))
                    .child("image")
                    .setValue("null");
        }
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

    public void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageViewProfilePicture);
            imageControl = true;
        }
        else {
            imageControl = false;
        }
    }

}