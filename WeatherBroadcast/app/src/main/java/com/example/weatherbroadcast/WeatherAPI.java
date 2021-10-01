package com.example.weatherbroadcast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather?appid=221df5a62855365ae1a662fb3491d9f5&units=metric")
    Call<ModelClass> getWeatherWithLocation(@Query("lat") double lat, @Query("lon") double lon);

    @GET("weather?appid=221df5a62855365ae1a662fb3491d9f5&units=metric")
    Call<ModelClass> getWeatherWithCityName(@Query("q") String name);

}
