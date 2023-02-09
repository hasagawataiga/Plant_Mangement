package com.mobile.plantmanagement.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherData {
    @SerializedName("main")
    private WeatherMainData weatherMainData;

    @SerializedName("weather")
    private List<WeatherWeather> weatherWeatherList;

    @SerializedName("cloud")
    private WeatherCloud weatherCloud;

    @SerializedName("pop")
    private float weatherRainProbability;

    @SerializedName("dt_txt")
    private String dateTime;

    public WeatherMainData getWeatherMainData() {
        return weatherMainData;
    }

    public void setWeatherMainData(WeatherMainData weatherMainData) {
        this.weatherMainData = weatherMainData;
    }

    public List<WeatherWeather> getWeatherWeatherList() {
        return weatherWeatherList;
    }

    public void setWeatherWeatherList(List<WeatherWeather> weatherWeatherList) {
        this.weatherWeatherList = weatherWeatherList;
    }

    public WeatherCloud getWeatherCloud() {
        return weatherCloud;
    }

    public void setWeatherCloud(WeatherCloud weatherCloud) {
        this.weatherCloud = weatherCloud;
    }

    public float getWeatherRainProbability() {
        return weatherRainProbability;
    }

    public void setWeatherRainProbability(float weatherRainProbability) {
        this.weatherRainProbability = weatherRainProbability;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
