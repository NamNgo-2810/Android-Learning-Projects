package com.example.photoalbum;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ImageViewModel extends AndroidViewModel {

    ImageRepository repository;
    LiveData<List<Image>> imageList;

    public ImageViewModel(@NonNull Application application) {
        super(application);

        repository = new ImageRepository(application);
        imageList = repository.getAllImages();
    }

    public void insert(Image image) {
        repository.insert(image);
    }

    public void update(Image image) {
        repository.update(image);
    }

    public void delete(Image image) {
        repository.delete(image);
    }

    public LiveData<List<Image>> getAllImages() {
        return imageList;
    }

}
