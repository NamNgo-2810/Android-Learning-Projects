package com.example.cheat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText editTextEmail;
    Button buttonReset;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        editTextEmail = findViewById(R.id.editTextEmailReset);
        buttonReset = findViewById(R.id.buttonReset);

        auth = FirebaseAuth.getInstance();

        buttonReset.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            if (!email.equals("")) passwordReset(email);
        });

    }

    public void passwordReset(String email) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ResetPasswordActivity.this, "Please check your email.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(ResetPasswordActivity.this, "There is a problem.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}