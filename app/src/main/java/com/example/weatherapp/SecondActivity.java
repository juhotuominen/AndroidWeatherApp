package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class SecondActivity extends AppCompatActivity {

    private String userInput;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        userInput = getIntent().getStringExtra("USER_INPUT");

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getWeatherData();
    }

    public void getWeatherData() {
        String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q="+ userInput +"&appid=3dce35167550488d879a333fc89a272a&units=metric";
        StringRequest request = new StringRequest(Request.Method.GET, WEATHER_URL, response -> {

            parseWeatherJsonAndUpdateUi(response);
        }, error -> {
            //errorit
        });
        Volley.newRequestQueue(this).add(request);
    }

    private void parseWeatherJsonAndUpdateUi(String response) {
        try {
            JSONObject weatherJSON = new JSONObject(response);
            String weather = weatherJSON.getJSONArray("weather").getJSONObject(0).getString("main");
            double temperature = weatherJSON.getJSONObject("main").getDouble("temp");
            double wind = weatherJSON.getJSONObject("wind").getDouble("speed");
            cityName = weatherJSON.getString("name");
            String icon = weatherJSON.getJSONArray("weather").getJSONObject(0).getString("icon");

            ImageView weatherIconImageView = findViewById(R.id.weatherIconImageView);
            String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";

            Picasso.get()
                    .load(iconUrl)
                    .into(weatherIconImageView);

            TextView cityNameTextView = findViewById(R.id.cityNameTextView);
            cityNameTextView.setText(cityName);
            TextView weatherTextView = findViewById(R.id.weatherTextView);
            weatherTextView.setText(getString(R.string.weather) + " " + weather);
            TextView temperatureTextView = findViewById(R.id.temperatureTextView);
            temperatureTextView.setText(getString(R.string.temp) + " " + temperature + " Celsius");
            TextView windTextView = findViewById(R.id.windTextView);
            windTextView.setText(getString(R.string.wind) + " " + wind + "m/s");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void openAccuweather(View view) {
        String accu = "https://www.accuweather.com/en/search-locations?query=" + cityName;
        Uri accuUri = Uri.parse(accu);
        Intent intent = new Intent(Intent.ACTION_VIEW, accuUri);
        try{
            startActivity(intent);
        }catch(Exception e){
            // errorit
        }
    }
}