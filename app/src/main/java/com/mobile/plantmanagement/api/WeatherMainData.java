package com.mobile.plantmanagement.api;

import com.google.gson.annotations.SerializedName;

public class WeatherMainData {
    @SerializedName("temp")
    private float temperature;

    @SerializedName("humidity")
    private int humidity;

    @SerializedName("feel_like")
    private float feelLike;

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getFeelLike() {
        return feelLike;
    }

    public void setFeelLike(float feelLike) {
        this.feelLike = feelLike;
    }
}
