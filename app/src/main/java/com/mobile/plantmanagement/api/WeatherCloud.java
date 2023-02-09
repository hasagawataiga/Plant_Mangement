package com.mobile.plantmanagement.api;

import com.google.gson.annotations.SerializedName;

public class WeatherCloud {
    @SerializedName("all")
    private String all;

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }
}
