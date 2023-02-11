package com.mobile.plantmanagement;

import android.util.Log;

import com.mobile.plantmanagement.api.WeatherAPI;
import com.mobile.plantmanagement.api.WeatherData;
import com.mobile.plantmanagement.api.WeatherResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherFetcher {
    private String url = "http://api.openweathermap.org/";
    private double longitude = 21.01;
    private double latitude = 52.23;
    private String appid = "339d49519e5ba2bd213c20c5f73c1a29";
    private String units = "metric";
    public List<WeatherData> weatherDataList = new ArrayList<>();
    private String TAG = "WEATHER_FETCHER";

    public void fetchWeatherData(WeatherCallback callback) {
        // Make API call here
        // HTTPS Request handle
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);
        Call<WeatherResponse> call = weatherAPI.getWeather(latitude, longitude, appid, units);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Status code: " + response.code());
                    // handle the response and update the UI
                    WeatherResponse weatherResponse = response.body();
                    for (WeatherResponse.WeatherAllData weatherMainData : weatherResponse.getWeatherAllDataList()) {
                        float temp = weatherMainData.getWeatherMainData().getTemp();
                        float feelsLike = weatherMainData.getWeatherMainData().getFeelsLike();
                        float tempMin = weatherMainData.getWeatherMainData().getTempMin();
                        float tempMax = weatherMainData.getWeatherMainData().getTempMax();
                        int humidity = weatherMainData.getWeatherMainData().getHumidity();
                        String descriptionLabel = weatherMainData.getWeatherDescription().get(0).getMain();
                        String descriptionDetail = weatherMainData.getWeatherDescription().get(0).getDescription();
                        String icon = weatherMainData.getWeatherDescription().get(0).getIcon();
                        int clouds = weatherMainData.getClouds().getAll();
                        float windSpeed = weatherMainData.getWind().getSpeed();
                        float rainProbability = weatherMainData.getPop();
                        String time = weatherMainData.getDt_txt();
                        WeatherData tempWeatherData = new WeatherData(temp, feelsLike, tempMin, tempMax, humidity, descriptionLabel, descriptionDetail, icon, clouds, windSpeed, rainProbability, time);
                        weatherDataList.add(tempWeatherData);
//                        Log.d(TAG, "weatherDataList" + weatherDataList.toString());
                    }
                    callback.onWeatherDataFetched(weatherDataList);
                    Log.d(TAG, "Get weatherDataList successful" + weatherDataList.toString());
                } else {
                    Log.d(TAG, "Get response failed. Status code: " + response.code());
                    // handle the error
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // handle the failure
                Log.d(TAG, "Error calling API " + t.getMessage());
            }
        });
        // After getting the result from the API, pass it to the callback function
        Log.d(TAG, weatherDataList.toString());
    }
}
