package com.example.photoalbum;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {

    private List<Image> images = new ArrayList<>();
    private onImageClickListener listener;

    public void setListener(onImageClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setImages(List<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public interface onImageClickListener {
        void onImageClick(Image image);
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_card, parent, false);

        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        Image image = images.get(position);
        holder.textViewTitle.setText(image.getImageTitle());
        holder.textViewDescription.setText(image.getImageDescription());
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(image.getImageFile(), 0, image.getImageFile().length));
    }

    public Image getPosition(int position) {
        return images.get(position);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textViewTitle, textViewDescription;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onImageClick(images.get(position));
                }
            });
        }
    }

}