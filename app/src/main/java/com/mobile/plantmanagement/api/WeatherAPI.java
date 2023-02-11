package com.mobile.plantmanagement.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("/data/2.5/forecast/")
    Call<WeatherResponse> getWeather(@Query("lat") double lat, @Query("lon") double lon,
                                     @Query("appid") String appid, @Query("units") String units);
}
