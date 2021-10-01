package com.example.cheat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    ImageView imageViewProfilePicture;
    EditText editTextEmail, editTextPassword, editTextUsername;
    Button buttonSignup;
    boolean imageControl = false;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imageViewProfilePicture = findViewById(R.id.imageViewChooseProfilePicture);
        editTextEmail = findViewById(R.id.editTextEmailSignup);
        editTextPassword = findViewById(R.id.editTextPasswordSignup);
        editTextUsername = findViewById(R.id.editTextUsernameSignup);
        buttonSignup = findViewById(R.id.buttonRegister);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        imageViewProfilePicture.setOnClickListener(v -> imageChooser());

        buttonSignup.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            String username = editTextUsername.getText().toString();

            if (!email.equals("") && !password.equals("") && !username.equals("")) {
                signUp(email, password, username);
            }
        });

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

    public void signUp(String email, String password, String username) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                databaseReference.child("Users")
                        .child(Objects.requireNonNull(auth.getUid()))
                        .child("userName")
                        .setValue(username);

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
                                        .addOnSuccessListener(unused -> Toast.makeText(RegisterActivity.this, "Write to database is successful.", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Write to database is not successful.", Toast.LENGTH_SHORT).show());
                                });
                            });
                }
                else {
                    databaseReference.child("Users")
                            .child(Objects.requireNonNull(auth.getUid()))
                            .child("image")
                            .setValue("null");
                }
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(RegisterActivity.this, "There is a problem.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}