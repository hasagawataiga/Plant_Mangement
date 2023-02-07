package com.mobile.plantmanagement.api;

import com.google.gson.annotations.SerializedName;

public class WeatherMainData {
    @SerializedName("temp")
    private double temperature;

    @SerializedName("humidity")
    private int humidity;

    @SerializedName("feel_like")
    private int feelLike;

}
