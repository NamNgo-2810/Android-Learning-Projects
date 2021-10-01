package com.example.cheat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<String> userList;
    String username;
    Context mContext;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    public UserAdapter(List<String> userList, String username, Context mContext) {
        this.userList = userList;
        this.username = username;
        this.mContext = mContext;

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        databaseReference.child("Users").child(userList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String otherName = Objects.requireNonNull(snapshot.child("userName").getValue()).toString();
                String imageURL = Objects.requireNonNull(snapshot.child("image").getValue()).toString();

                holder.textViewUser.setText(otherName);

                if (imageURL.equals("null")) {
                    holder.imageViewUser.setImageResource(R.drawable.ic_baseline_account_circle_24);
                }
                else {
                    Picasso.get().load(imageURL).into(holder.imageViewUser);
                }

                holder.cardView.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("otherName", otherName);
                    mContext.startActivity(intent);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUser;
        CircleImageView imageViewUser;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUser = itemView.findViewById(R.id.textViewUser);
            imageViewUser = itemView.findViewById(R.id.imageViewUser);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

}
