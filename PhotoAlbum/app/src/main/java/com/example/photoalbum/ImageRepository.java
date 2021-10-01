package com.example.photoalbum;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ImageRepository {

    ImageDAO imageDAO;
    LiveData<List<Image>> imageList;

    public ImageRepository(Application application) {
        ImageDatabase database = ImageDatabase.getInstance(application);
        imageDAO = database.imageDAO();
        imageList = imageDAO.getAllImages();
    }

    public void insert(Image image) {
        new InsertImageAsyncTask(imageDAO).execute(image);
    }

    public void update(Image image) {
        new UpdateImageAsyncTask(imageDAO).execute(image);
    }

    public void delete(Image image) {
        new DeleteImageAsyncTask(imageDAO).execute(image);
    }

    public LiveData<List<Image>> getAllImages() {
        return imageList;
    }

    public static class InsertImageAsyncTask extends AsyncTask<Image, Void, Void> {

        ImageDAO imageDAO;

        public InsertImageAsyncTask(ImageDAO imageDAO) {
            this.imageDAO = imageDAO;
        }

        @Override
        protected Void doInBackground(Image... images) {
            imageDAO.Insert(images[0]);

            return null;
        }
    }

    public static class UpdateImageAsyncTask extends AsyncTask<Image, Void, Void> {

        ImageDAO imageDAO;

        public UpdateImageAsyncTask(ImageDAO imageDAO) {
            this.imageDAO = imageDAO;
        }

        @Override
        protected Void doInBackground(Image... images) {
            imageDAO.Update(images[0]);

            return null;
        }
    }

    public static class DeleteImageAsyncTask extends AsyncTask<Image, Void, Void> {

        ImageDAO imageDAO;

        public DeleteImageAsyncTask(ImageDAO imageDAO) {
            this.imageDAO = imageDAO;
        }

        @Override
        protected Void doInBackground(Image... images) {
            imageDAO.Delete(images[0]);

            return null;
        }
    }

}
