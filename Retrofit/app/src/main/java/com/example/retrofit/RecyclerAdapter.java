package com.example.retrofit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<ModelClass> data;

    public RecyclerAdapter(List<ModelClass> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textA.setText("" + data.get(position).getUserId());
        holder.textB.setText("" + data.get(position).getId());
        holder.textC.setText("" + data.get(position).getTitle());
        holder.textD.setText("" + data.get(position).getSubTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textA, textB, textC, textD;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textA = itemView.findViewById(R.id.textView1);
            textB = itemView.findViewById(R.id.textView2);
            textC = itemView.findViewById(R.id.textView3);
            textD = itemView.findViewById(R.id.textView4);
        }
    }

}
