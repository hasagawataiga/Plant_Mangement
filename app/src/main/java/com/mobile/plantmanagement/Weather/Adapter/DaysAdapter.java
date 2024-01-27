package com.mobile.plantmanagement.Weather.Adapter;

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

import com.mobile.plantmanagement.R;
import com.mobile.plantmanagement.Weather.Update.UpdateUI;
import com.github.ybq.android.spinkit.SpinKitView;
import com.mobile.plantmanagement.api.WeatherData;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.DayViewHolder> {
    private final Context context;
    private List<WeatherData> weatherDataList;

    public DaysAdapter(Context context) {
        this.context = context;
    }

    public DaysAdapter(List<WeatherData> weatherData, Context context) {
        this.weatherDataList = weatherData;
        this.context = context;
    }

    private String updated_at, icon, update_time;
    private int condition, pressure, wind_speed, humidity;
    private long sunset, sunrise, min_temperature, max_temperature;
    private final static String TAG = "WEATHER_FETCHER";

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_day_item_layout, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        getDailyWeatherInfo(position + 1, holder);
    }

    @Override
    public int getItemCount() {
        return weatherDataList.size() - 1;
    }

    @SuppressLint("DefaultLocale")
    private void getDailyWeatherInfo(int i, DayViewHolder holder) {
        // Holder For old approach
        icon = weatherDataList.get(i).getIcon();
        Picasso.get()
                .load("http://openweathermap.org/img/wn/" + weatherDataList.get(i).getIcon() + "@" + icon + ".png")
                .into(holder.icon);

        updated_at = extractDateOfWeek(weatherDataList.get(i).getTime());
        update_time = extractTimeOfDay(weatherDataList.get(i).getTime());
        min_temperature = Math.round(weatherDataList.get(i).getTempMin());
        max_temperature = Math.round(weatherDataList.get(i).getTempMax());
        pressure = weatherDataList.get(i).getPressure();
        wind_speed = Math.round(weatherDataList.get(i).getWindSpeed());
        humidity = weatherDataList.get(i).getHumidity();

        updateUI(holder);
        Log.i(TAG, "Day " + i);
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(DayViewHolder holder) {
        String day = UpdateUI.TranslateDay(updated_at, context);
        holder.hour.setText(update_time);
        holder.dTime.setText(day);
        holder.temp_min.setText(min_temperature + "°C");
        holder.temp_max.setText(max_temperature + "°C");
        holder.pressure.setText(pressure + " mb");
        holder.wind.setText(wind_speed + " km/h");
        holder.humidity.setText(humidity + "%");
        hideProgressBar(holder);
    }

    private String extractDateOfWeek(String dateTime) {
        // Holder for new approach
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Date date;
        try {
            date = inputFormat.parse(dateTime);
            return dayFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the parsing exception appropriately
            return "";
        }
    }

    private String extractTimeOfDay(String dateTime) {
        // Holder for new approach
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a", Locale.US);
        Date date;
        try {
            date = inputFormat.parse(dateTime);
            return timeFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the parsing exception appropriately
            return "";
        }
    }

    private void hideProgressBar(DayViewHolder holder) {
        holder.progress.setVisibility(View.GONE);
        holder.layout.setVisibility(View.VISIBLE);
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        SpinKitView progress;
        RelativeLayout layout;
        TextView dTime, temp_min, temp_max, pressure, wind, humidity;
        ImageView icon;
        TextView hour;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            progress = itemView.findViewById(R.id.day_progress_bar);
            layout = itemView.findViewById(R.id.day_relative_layout);
            dTime = itemView.findViewById(R.id.day_time);
            temp_min = itemView.findViewById(R.id.day_min_temp);
            temp_max = itemView.findViewById(R.id.day_max_temp);
            pressure = itemView.findViewById(R.id.day_pressure);
            wind = itemView.findViewById(R.id.day_wind);
            humidity = itemView.findViewById(R.id.day_humidity);
            icon = itemView.findViewById(R.id.day_icon);
            hour = itemView.findViewById(R.id.hour);
        }
    }
}
