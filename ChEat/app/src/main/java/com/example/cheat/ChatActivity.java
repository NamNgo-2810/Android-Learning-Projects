package com.example.cheat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    ImageView imageViewBack;
    TextView textViewChat;
    EditText editTextMessage;
    FloatingActionButton fabSend;
    RecyclerView rvChat;
    MessageAdapter adapter;
    List<ModelClass> list;

    String username, otherName;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        imageViewBack = findViewById(R.id.imageViewBack);
        textViewChat = findViewById(R.id.textViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        fabSend = findViewById(R.id.fab);
        rvChat = findViewById(R.id.rvChat);

        rvChat.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        username = getIntent().getStringExtra("username");
        otherName = getIntent().getStringExtra("otherName");
        textViewChat.setText(otherName);

        imageViewBack.setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, MainActivity.class);
            startActivity(intent);
        });

        fabSend.setOnClickListener(v -> {
            String message = editTextMessage.getText().toString();
            if (!message.equals("")) {
                sendMessage(message);
                editTextMessage.setText("");
            }
        });

        getMessage();

    }

    public void sendMessage(String message) {
        String key = databaseReference.child("Messages").child(username).child(otherName).push().getKey();
        Map<String,Object> messageMap = new HashMap<>();
        messageMap.put("message", message);
        messageMap.put("from", username);
        assert key != null;
        databaseReference.child("Messages")
                .child(username)
                .child(otherName)
                .child(key)
                .setValue(messageMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        databaseReference.child("Messages")
                                .child(otherName)
                                .child(username)
                                .child(key)
                                .setValue(messageMap);
                    }
                });
    }

    public void getMessage() {
        databaseReference.child("Messages").child(username).child(otherName).addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ModelClass modelClass = snapshot.getValue(ModelClass.class);
                list.add(modelClass);
                adapter.notifyDataSetChanged();
                rvChat.scrollToPosition(list.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new MessageAdapter(list, username);
        rvChat.setAdapter(adapter);
    }

}