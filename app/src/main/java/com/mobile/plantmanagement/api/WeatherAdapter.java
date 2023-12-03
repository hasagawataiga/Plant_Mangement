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


import java.text.ParseException;
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
    public WeatherAdapter (WeatherData weatherData, Context context) {
        this.weatherData = weatherData;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(weather_main_layout, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return weatherData != null ? 1 : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
//        Log.d(TAG, "Position: " + position + ", weatherDataList size: " + String.valueOf(weatherDataList.size()));
//        weatherData = weatherDataList.get(0);

        // Holder for new approach
        String timeString = weatherData.getTime();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date;
        try {
            date = inputFormat.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the parsing exception appropriately
            return;
        }
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        updated_at = dayFormat.format(date);

        description = weatherData.getDescriptionLabel();
        temperature = Math.round(weatherData.getTemp());
        min_temperature = Math.round(weatherData.getTempMin());
        max_temperature = Math.round(weatherData.getTempMax());
        pressure = weatherData.getPressure();
        wind_speed = Math.round(weatherData.getWindSpeed());
        humidity = weatherData.getHumidity();
        Log.i(TAG, "Begin fetching " + "update_at: " +
                updated_at + ", description: " +
                description + ", temperature: " +
                temperature + ", min: " +
                min_temperature + ", max: " +
                max_temperature + ", pressure: " +
                pressure + ", wind speed: " +
                wind_speed + ", humidity: " +
                humidity
        );
        updateUI(holder);
        Log.i(TAG, "Update UI main weather: " + weatherData.getCityName());
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(WeatherViewHolder holder) {
        // Holder For old approach
        Picasso.get()
                .load("http://openweathermap.org/img/wn/" + weatherData.getIcon() + ".png")
                .fit()
                .centerCrop()
                .into(holder.getConditionIv());
        holder.nameTv.setText(weatherData.getCityName());
        holder.updatedAtTv.setText(updated_at);
        holder.conditionDescTv.setText(description);
        holder.tempTv.setText(temperature + "°C");
        holder.minTempTv.setText(min_temperature + "°C");
        holder.maxTempTv.setText(max_temperature + "°C");
        holder.pressureTv.setText(pressure + " mb");
        holder.windTv.setText(wind_speed + " km/h");
        holder.humidityTv.setText(humidity + "%");
        hideProgressBar(holder);
    }
    private void hideProgressBar(WeatherViewHolder holder) {
        holder.progress.setVisibility(View.GONE);
        holder.layout.setVisibility(View.VISIBLE);
    }
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
    SpinKitView progress;
    RelativeLayout layout;
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
        progress = itemView.findViewById(R.id.progress);
        layout = itemView.findViewById(R.id.layout);
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
