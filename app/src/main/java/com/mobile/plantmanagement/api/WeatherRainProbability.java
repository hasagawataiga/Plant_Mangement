package com.mobile.plantmanagement.api;

import com.google.gson.annotations.SerializedName;

public class WeatherRainProbability {
    @SerializedName("pop")
    private String pop;

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }
}
