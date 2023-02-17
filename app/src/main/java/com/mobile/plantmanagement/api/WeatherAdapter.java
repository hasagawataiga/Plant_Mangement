package com.mobile.plantmanagement.api;

import static com.mobile.plantmanagement.R.layout.weather_display_template;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.plantmanagement.R;
import com.squareup.picasso.Picasso;


import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {
    private List<WeatherData> weatherDataList;
    private static final int weather_display_template = R.layout.weather_display_template;
    private final String TAG = "WEATHER_ADAPTER";
    public WeatherAdapter (List<WeatherData> weatherDataList){
        this.weatherDataList = weatherDataList;
    }

    public void setWeatherDataList (List<WeatherData> weatherDataList){
        this.weatherDataList = weatherDataList;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(weather_display_template, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Log.d(TAG, "Position: " + position + ", weatherDataList size: " + String.valueOf(weatherDataList.size()));
        WeatherData weatherData = weatherDataList.get(position);
        Picasso.get()
                .load("http://openweathermap.org/img/wn/" + weatherData.getIcon() + "@4x.png")
                .into(holder.getIcon());
        holder.getTime().setText(weatherData.getTime());
        holder.getTemperature().setText(Float.toString(weatherData.getTemp()));
        holder.getFeelLike().setText(Float.toString(weatherData.getFeelsLike()));
        holder.getHumidity().setText(Integer.toString(weatherData.getHumidity()));
        holder.getCloudPercentage().setText(Integer.toString(weatherData.getClouds()));
        holder.getRainProbability().setText(Float.toString(weatherData.getRainProbability()));
    }

    @Override
    public int getItemCount() {
        return weatherDataList.size();
    }
}

class WeatherViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView_icon;
    private TextView tv_time;
    private TextView tv_temperature;
    private TextView tv_feelLike;
    private TextView tv_humidity;
    private TextView tv_cloudPercentage;
    private TextView tv_rainProbability;

    public WeatherViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView_icon = itemView.findViewById(R.id.imageView_icon);
        tv_time = itemView.findViewById(R.id.tv_time);
        tv_temperature = itemView.findViewById(R.id.tv_temperature);
        tv_feelLike = itemView.findViewById(R.id.tv_feelLike);
        tv_humidity = itemView.findViewById(R.id.tv_humidity);
        tv_cloudPercentage = itemView.findViewById(R.id.tv_cloudPercentage);
        tv_rainProbability = itemView.findViewById(R.id.tv_rainProbability);
    }
    public ImageView getIcon() {
        return imageView_icon;
    }

    public void setIcon(ImageView imageView_icon) {
        this.imageView_icon = imageView_icon;
    }

    public TextView getTime() {
        return tv_time;
    }

    public void setTime(TextView tv_time) {
        this.tv_time = tv_time;
    }

    public TextView getTemperature() {
        return tv_temperature;
    }

    public void setTemperature(TextView tv_temperature) {
        this.tv_temperature = tv_temperature;
    }

    public TextView getFeelLike() {
        return tv_feelLike;
    }

    public void setFeelLike(TextView tv_feelLike) {
        this.tv_feelLike = tv_feelLike;
    }

    public TextView getHumidity() {
        return tv_humidity;
    }

    public void setHumidity(TextView tv_humidity) {
        this.tv_humidity = tv_humidity;
    }

    public TextView getCloudPercentage() {
        return tv_cloudPercentage;
    }

    public void setCloudPercentage(TextView tv_cloudPercentage) {
        this.tv_cloudPercentage = tv_cloudPercentage;
    }

    public TextView getRainProbability() {
        return tv_rainProbability;
    }

    public void setRainProbability(TextView tv_rainProbability) {
        this.tv_rainProbability = tv_rainProbability;
    }
}
