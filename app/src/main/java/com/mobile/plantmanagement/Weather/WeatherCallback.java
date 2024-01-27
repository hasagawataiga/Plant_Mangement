package com.mobile.plantmanagement.Weather;

import com.mobile.plantmanagement.api.WeatherData;

import java.util.List;

public interface WeatherCallback {
    void onWeatherDataFetched(List<WeatherData> weatherDataList);
}
