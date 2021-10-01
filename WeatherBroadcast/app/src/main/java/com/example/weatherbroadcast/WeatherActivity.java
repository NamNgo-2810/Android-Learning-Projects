package com.example.weatherbroadcast;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    TextView city, temperature, weatherCondition, humidity, maxTemperature, minTemperature, pressure, wind;
    ImageView imageView;
    Button search;
    EditText editTextCityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        city = findViewById(R.id.textViewCityWeather);
        temperature = findViewById(R.id.textViewTempWeather);
        weatherCondition = findViewById(R.id.textViewWeatherConditionWeather);
        humidity = findViewById(R.id.textViewHumidityWeather);
        maxTemperature = findViewById(R.id.textViewMaxTempWeather);
        minTemperature = findViewById(R.id.textViewMinTempWeather);
        pressure = findViewById(R.id.textViewPressureWeather);
        wind = findViewById(R.id.textViewWindWeather);
        imageView = findViewById(R.id.imageViewWeather);
        search = findViewById(R.id.search);
        editTextCityName = findViewById(R.id.editTextCityName);

        search.setOnClickListener(v -> {
            String cityName = editTextCityName.getText().toString();
            getWeatherData(cityName);
            editTextCityName.setText("");
        });
    }

    public void getWeatherData(String cityName) {
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<ModelClass> call = weatherAPI.getWeatherWithCityName(cityName);

        call.enqueue(new Callback<ModelClass>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ModelClass> call, @NonNull Response<ModelClass> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    city.setText(response.body().getName() + ", " + response.body().getSys().getCountry());
                    temperature.setText(response.body().getMain().getTemp() + "°C");
                    weatherCondition.setText(response.body().getWeather().get(0).getDescription());
                    humidity.setText(": " + response.body().getMain().getHumidity() + "%");
                    maxTemperature.setText(": " + response.body().getMain().getTempMax() + "°C");
                    minTemperature.setText(": " + response.body().getMain().getTempMin() + "°C");
                    pressure.setText(": " + response.body().getMain().getPressure());
                    wind.setText(": " + response.body().getWind().getSpeed());

                    String iconCode = response.body().getWeather().get(0).getIcon();
                    Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png")
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(imageView);
                }
                else {
                    Toast.makeText(WeatherActivity.this, "Can not find this city", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelClass> call, @NonNull Throwable t) {

            }
        });
    }

}