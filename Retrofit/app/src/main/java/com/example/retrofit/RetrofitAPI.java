package com.example.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitAPI {

    @GET("posts")
    Call<List<ModelClass>> getModelClass();

}
