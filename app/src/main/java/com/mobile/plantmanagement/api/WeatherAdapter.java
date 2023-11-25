package com.mobile.plantmanagement.api;

import static com.mobile.plantmanagement.R.layout.weather_activity_home;
import static com.mobile.plantmanagement.R.layout.weather_display_template;

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
    private static final int weather_display_template = R.layout.weather_display_template;
    private static final int weather_activity_home = R.layout.weather_activity_home;

    private String updated_at;
    private int condition, pressure;
    private float rainPop, clouds, min, max, humidity, windSpeed;
    private long update_time;
    private final long sunrise = 1661834187;
    private final long sunset = 1661882248;

    private final String TAG = "WEATHER_ADAPTER";
    public WeatherAdapter (List<WeatherData> weatherDataList){
        this.weatherDataList = weatherDataList;
    }

    public WeatherAdapter(Context context) {
        this.context = context;
    }

    public void setWeatherDataList (List<WeatherData> weatherDataList){
        this.weatherDataList = weatherDataList;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(weather_display_template, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(weather_activity_home, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Log.d(TAG, "Position: " + position + ", weatherDataList size: " + String.valueOf(weatherDataList.size()));
        WeatherData weatherData = weatherDataList.get(position);
        // Holder For old approach
        Picasso.get()
                .load("http://openweathermap.org/img/wn/" + weatherData.getIcon() + "@4x.png")
                .into(holder.getIcon());
        holder.getTime().setText(weatherData.getTime());
        holder.getTemperature().setText(Float.toString(weatherData.getTemp()));
        holder.getFeelLike().setText(Float.toString(weatherData.getFeelsLike()));
        holder.getHumidity().setText(Integer.toString(weatherData.getHumidity()));
        holder.getCloudPercentage().setText(Integer.toString(weatherData.getClouds()));
        holder.getRainProbability().setText(Float.toString(weatherData.getRainProbability()));


        // Holder for new approach
        update_time = Long.parseLong(weatherData.getTime());
        updated_at = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date((update_time * 1000) + (position * 864_000_00L)));   // i=0
        condition = weatherData.getCondition();
        rainPop = weatherData.getRainProbability();
        clouds = weatherData.getClouds();
        min = weatherData.getTempMin();
        max = weatherData.getTempMax();
        pressure = weatherData.getPressure();
        windSpeed = weatherData.getWindSpeed();
        humidity = weatherData.getHumidity();

        updateUI(holder);
        hideProgressBar(holder);
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(WeatherViewHolder holder) {
        String day = UpdateUI.TranslateDay(updated_at, context);
        holder.dTime.setText(day);
        holder.temp_min.setText(min + "°C");
        holder.temp_max.setText(max + "°C");
        holder.pressure.setText(pressure + " mb");
        holder.wind.setText(windSpeed + " km/h");
        holder.humidity.setText(humidity + "%");
        holder.icon.setImageResource(
                context.getResources().getIdentifier(
                        UpdateUI.getIconID(condition, update_time, sunrise, sunset),
                        "drawable",
                        context.getPackageName()
                ));
    }

    @Override
    public int getItemCount() {
        return weatherDataList.size();
    }

    private void hideProgressBar(WeatherViewHolder holder) {
        holder.progress.setVisibility(View.GONE);
        holder.layout.setVisibility(View.VISIBLE);
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

    SpinKitView progress;
    RelativeLayout layout;
    TextView dTime, temp_min, temp_max, pressure, wind, humidity;
    ImageView icon;

    public WeatherViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView_icon = itemView.findViewById(R.id.imageView_icon);
        tv_time = itemView.findViewById(R.id.tv_time);
        tv_temperature = itemView.findViewById(R.id.tv_temperature);
        tv_feelLike = itemView.findViewById(R.id.tv_feelLike);
        tv_humidity = itemView.findViewById(R.id.tv_humidity);
        tv_cloudPercentage = itemView.findViewById(R.id.tv_cloudPercentage);
        tv_rainProbability = itemView.findViewById(R.id.tv_rainProbability);

        progress = itemView.findViewById(R.id.day_progress_bar);
        layout = itemView.findViewById(R.id.day_relative_layout);
        dTime = itemView.findViewById(R.id.day_time);
        temp_min = itemView.findViewById(R.id.day_min_temp);
        temp_max = itemView.findViewById(R.id.day_max_temp);
        pressure = itemView.findViewById(R.id.day_pressure);
        wind = itemView.findViewById(R.id.day_wind);
        humidity = itemView.findViewById(R.id.day_humidity);
        icon = itemView.findViewById(R.id.day_icon);
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
