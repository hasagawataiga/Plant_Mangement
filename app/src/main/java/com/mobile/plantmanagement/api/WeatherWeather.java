package com.mobile.plantmanagement.api;

import com.google.gson.annotations.SerializedName;

public class WeatherWeather {
    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
