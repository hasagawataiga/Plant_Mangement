package com.mobile.plantmanagement.api;

import com.google.gson.annotations.SerializedName;

public class WeatherRainProbability {
    @SerializedName("pop")
    private int pop;

    public int getPop() {
        return pop;
    }

    public void setPop(int pop) {
        this.pop = pop;
    }
}
