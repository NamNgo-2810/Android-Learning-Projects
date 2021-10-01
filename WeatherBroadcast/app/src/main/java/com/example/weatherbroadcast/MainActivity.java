package com.example.weatherbroadcast;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView city, temperature, weatherCondition, humidity, maxTemperature, minTemperature, pressure, wind;
    ImageView imageView;
    FloatingActionButton fab;

    LocationManager locationManager;
    LocationListener locationListener;
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = findViewById(R.id.textViewCity);
        temperature = findViewById(R.id.textViewTemp);
        weatherCondition = findViewById(R.id.textViewWeatherCondition);
        humidity = findViewById(R.id.textViewHumidity);
        maxTemperature = findViewById(R.id.textViewMaxTemp);
        minTemperature = findViewById(R.id.textViewMinTemp);
        pressure = findViewById(R.id.textViewPressure);
        wind = findViewById(R.id.textViewWind);
        imageView = findViewById(R.id.imageView);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(intent);
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = location -> {
            lat = location.getLatitude();
            lon = location.getLongitude();

            Log.e("lat: ", String.valueOf(lat));
            Log.e("lon: ", String.valueOf(lon));

            getWeatherData(lat, lon);
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && permissions.length > 0 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }
    }

    public void getWeatherData(double lat, double lon) {
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<ModelClass> call = weatherAPI.getWeatherWithLocation(lat, lon);

        call.enqueue(new Callback<ModelClass>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ModelClass> call, @NonNull Response<ModelClass> response) {
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

            @Override
            public void onFailure(@NonNull Call<ModelClass> call, @NonNull Throwable t) {

            }
        });
    }

}