package com.mobile.plantmanagement.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    @SerializedName("list")
    private List<WeatherData> weatherDataList;

    public List<WeatherData> getWeatherDataList() {
        return weatherDataList;
    }

    public void setWeatherDataList(List<WeatherData> weatherDataList) {
        this.weatherDataList = weatherDataList;
    }
}
