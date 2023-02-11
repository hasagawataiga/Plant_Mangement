package com.mobile.plantmanagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobile.plantmanagement.api.WeatherData;

import java.util.List;

public class WeatherViewModel extends ViewModel {
    private MutableLiveData<List<WeatherData>> weatherDataList = new MutableLiveData<>();

    public void setWeatherDataList(List<WeatherData> weatherDataList) {
        this.weatherDataList.setValue(weatherDataList);
    }

    public LiveData<List<WeatherData>> getWeatherDataList() {
        return weatherDataList;
    }
}
