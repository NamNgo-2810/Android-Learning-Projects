package com.example.retrofit;

import com.google.gson.annotations.SerializedName;

public class ModelClass {

    private int userId;
    private int id;
    private String title;

    @SerializedName("body")
    private String subTitle;

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }
}
