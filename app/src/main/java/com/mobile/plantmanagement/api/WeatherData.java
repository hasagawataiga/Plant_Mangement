package com.mobile.plantmanagement.api;

public class WeatherData {
    private float temp;
    private float feelsLike;
    private float tempMin;
    private float tempMax;
    private int humidity;
    private String descriptionLabel;
    private String descriptionDetail;
    private String icon;
    private int clouds;
    private float windSpeed;
    private float rainProbability;
    private String time;

    public WeatherData(float temp, float feelsLike, float tempMin, float tempMax, int humidity, String descriptionLabel, String descriptionDetail, String icon, int clouds, float windSpeed, float rainProbability, String time) {
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.humidity = humidity;
        this.descriptionLabel = descriptionLabel;
        this.descriptionDetail = descriptionDetail;
        this.icon = icon;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.rainProbability = rainProbability;
        this.time = time;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(float feelsLike) {
        this.feelsLike = feelsLike;
    }

    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getDescriptionLabel() {
        return descriptionLabel;
    }

    public void setDescriptionLabel(String descriptionLabel) {
        this.descriptionLabel = descriptionLabel;
    }

    public String getDescriptionDetail() {
        return descriptionDetail;
    }

    public void setDescriptionDetail(String descriptionDetail) {
        this.descriptionDetail = descriptionDetail;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public float getRainProbability() {
        return rainProbability;
    }

    public void setRainProbability(float rainProbability) {
        this.rainProbability = rainProbability;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "time='" + time + '\'' +
                '}';
    }
}
