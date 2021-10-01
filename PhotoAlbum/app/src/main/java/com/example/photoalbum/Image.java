package com.example.photoalbum;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "image")
public class Image {
    @PrimaryKey(autoGenerate = true)
    int image_id;
    String image_title;
    String image_description;
    byte[] image_file;

    public Image(String image_title, String image_description, byte[] image_file) {
        this.image_title = image_title;
        this.image_description = image_description;
        this.image_file = image_file;
    }

    public int getImageId() {
        return image_id;
    }

    public String getImageTitle() {
        return image_title;
    }

    public String getImageDescription() {
        return image_description;
    }

    public byte[] getImageFile() {
        return image_file;
    }

    public void setImageId(int image_id) {
        this.image_id = image_id;
    }
}
