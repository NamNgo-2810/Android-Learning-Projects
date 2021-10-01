package com.example.photoalbum;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ImageDAO {

    @Insert
    void Insert(Image image);

    @Update
    void Update(Image image);

    @Delete
    void Delete(Image image);

    @Query("SELECT * FROM image ORDER BY image_id ASC")
    LiveData<List<Image>> getAllImages();

}
