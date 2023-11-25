package com.mobile.plantmanagement.api;

import static com.mobile.plantmanagement.R.layout.weather_activity_home;
import static com.mobile.plantmanagement.R.layout.weather_display_template;
import static com.mobile.plantmanagement.R.layout.weather_main_layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.mobile.plantmanagement.R;
import com.mobile.plantmanagement.Weather.Adapter.DaysAdapter;
import com.mobile.plantmanagement.Weather.Update.UpdateUI;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {

    private Context context;
    private List<WeatherData> weatherDataList;
    private WeatherData weatherData;
    private static final int weather_display_template = R.layout.weather_display_template;
    private static final int weather_activity_home = R.layout.weather_activity_home;

    private String updated_at, description, icon;
    private int pressure;
    private double humidity, temperature, min_temperature, max_temperature, wind_speed;

    private final String TAG = "WEATHER_ADAPTER";
    public WeatherAdapter (List<WeatherData> weatherDataList, Context context){
        this.weatherDataList = weatherDataList;
        this.context = context;
    }
    public WeatherAdapter (Context context) {
        this.context = context;
    }
    public void setWeatherDataList (List<WeatherData> weatherDataList){
        this.weatherDataList = weatherDataList;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(weather_display_template, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(weather_main_layout, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Log.d(TAG, "Position: " + position + ", weatherDataList size: " + String.valueOf(weatherDataList.size()));
        weatherData = weatherDataList.get(position);
        // Holder For old approach
        icon = weatherData.getIcon();
        Picasso.get()
                .load("http://openweathermap.org/img/wn/" + weatherData.getIcon() + "@" + icon + ".png")
                .into(holder.getConditionIv());

        // Holder for new approach
        String time = weatherData.getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        updated_at = dayFormat.format(time);

        description = weatherData.getDescriptionLabel();
        temperature = weatherData.getTemp() - 273.15;
        min_temperature = weatherData.getTempMin() - 273.15;
        max_temperature = weatherData.getTempMax() - 273.15;
        pressure = weatherData.getPressure();
        wind_speed = weatherData.getWindSpeed();
        humidity = weatherData.getHumidity();

        updateUI(holder);
//        hideProgressBar(holder);
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(WeatherViewHolder holder) {



        holder.nameTv.setText(weatherData.getCityName());
        holder.updatedAtTv.setText(updated_at);
        holder.conditionDescTv.setText(description);
        holder.tempTv.setText(temperature + "°C");
        holder.minTempTv.setText(min_temperature + "°C");
        holder.maxTempTv.setText(max_temperature + "°C");
        holder.pressureTv.setText(pressure + " mb");
        holder.windTv.setText(wind_speed + " km/h");
        holder.humidityTv.setText(humidity + "%");
    }

    @Override
    public int getItemCount() {
        return weatherDataList.size();
    }

//    private void hideProgressBar(WeatherViewHolder holder) {
//        holder.progress.setVisibility(View.GONE);
//        holder.layout.setVisibility(View.VISIBLE);
//    }
}


class WeatherViewHolder extends RecyclerView.ViewHolder {
    TextView nameTv;
    TextView updatedAtTv;
    ImageView conditionIv;
    TextView conditionDescTv;
    TextView tempTv;
    TextView minTempTv;
    TextView maxTempTv;
    TextView pressureTv;
    TextView windTv;
    TextView humidityTv;
//    SpinKitView progress;
//    RelativeLayout layout;
//    TextView dTime, temp_min, temp_max, pressure, wind, humidity;
//    ImageView icon;

    public WeatherViewHolder(@NonNull View itemView) {
        super(itemView);
        nameTv = itemView.findViewById(R.id.name_tv);
        updatedAtTv = itemView.findViewById(R.id.updated_at_tv);
        conditionIv = itemView.findViewById(R.id.condition_iv);
        conditionDescTv = itemView.findViewById(R.id.conditionDesc_tv);
        tempTv = itemView.findViewById(R.id.temp_tv);
        minTempTv = itemView.findViewById(R.id.min_temp_tv);
        maxTempTv = itemView.findViewById(R.id.max_temp_tv);
        pressureTv = itemView.findViewById(R.id.pressure_tv);
        windTv = itemView.findViewById(R.id.wind_tv);
        humidityTv = itemView.findViewById(R.id.humidity_tv);
    }

    public TextView getNameTv() {
        return nameTv;
    }

    public void setNameTv(TextView nameTv) {
        this.nameTv = nameTv;
    }

    public TextView getUpdatedAtTv() {
        return updatedAtTv;
    }

    public void setUpdatedAtTv(TextView updatedAtTv) {
        this.updatedAtTv = updatedAtTv;
    }

    public ImageView getConditionIv() {
        return conditionIv;
    }

    public void setConditionIv(ImageView conditionIv) {
        this.conditionIv = conditionIv;
    }

    public TextView getConditionDescTv() {
        return conditionDescTv;
    }

    public void setConditionDescTv(TextView conditionDescTv) {
        this.conditionDescTv = conditionDescTv;
    }

    public TextView getTempTv() {
        return tempTv;
    }

    public void setTempTv(TextView tempTv) {
        this.tempTv = tempTv;
    }

    public TextView getMinTempTv() {
        return minTempTv;
    }

    public void setMinTempTv(TextView minTempTv) {
        this.minTempTv = minTempTv;
    }

    public TextView getMaxTempTv() {
        return maxTempTv;
    }

    public void setMaxTempTv(TextView maxTempTv) {
        this.maxTempTv = maxTempTv;
    }

    public TextView getPressureTv() {
        return pressureTv;
    }

    public void setPressureTv(TextView pressureTv) {
        this.pressureTv = pressureTv;
    }

    public TextView getWindTv() {
        return windTv;
    }

    public void setWindTv(TextView windTv) {
        this.windTv = windTv;
    }

    public TextView getHumidityTv() {
        return humidityTv;
    }

    public void setHumidityTv(TextView humidityTv) {
        this.humidityTv = humidityTv;
    }
}
