package com.example.cheat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonSignIn, buttonSignUp;
    TextView textViewForgotPassword;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmailLogin);
        editTextPassword = findViewById(R.id.editTextPasswordLogin);
        buttonSignIn = findViewById(R.id.buttonSignin);
        buttonSignUp = findViewById(R.id.buttonSignup);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);

        auth = FirebaseAuth.getInstance();

        buttonSignIn.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if (!email.equals("") && !password.equals("")) {
                signIn(email, password);
            }
            else Toast.makeText(LoginActivity.this, "Please enter an email and password.", Toast.LENGTH_SHORT).show();
        });

        buttonSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        textViewForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });

    }

    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(LoginActivity.this, "Sign in is successful.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(LoginActivity.this, "Sign in is not successful.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}