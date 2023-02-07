package com.mobile.plantmanagement.api;

import com.mobile.plantmanagement.R;

import java.util.HashMap;
import java.util.Map;

public class WeatherStatusIconMapping {
    private Map<String, WeatherStatus> weatherIconMapping = new HashMap<>();

    private void initializeWeatherIconMapping() {
        weatherIconMapping.put("200", new WeatherStatus("Thunderstorm", "thunderstorm with light rain", "01d"));
    }
}
