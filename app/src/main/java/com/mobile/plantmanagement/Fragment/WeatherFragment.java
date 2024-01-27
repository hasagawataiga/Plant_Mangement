package com.mobile.plantmanagement.Fragment;

import static com.mobile.plantmanagement.Weather.Network.InternetConnectivity.isInternetConnected;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.mobile.plantmanagement.MainActivity;
import com.mobile.plantmanagement.R;
import com.mobile.plantmanagement.Weather.Adapter.DaysAdapter;
import com.mobile.plantmanagement.Weather.Location.LocationCord;
import com.mobile.plantmanagement.Weather.Toast.Toaster;
import com.mobile.plantmanagement.Weather.Url.URL;
import com.mobile.plantmanagement.Weather.WeatherCallback;
import com.mobile.plantmanagement.Weather.WeatherFetcher;
import com.mobile.plantmanagement.Weather.WeatherViewModel;
import com.mobile.plantmanagement.api.*;
import com.mobile.plantmanagement.databinding.WeatherActivityHomeBinding;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment implements WeatherCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = "WEATHER";
    private List<WeatherData> weatherDataList;
    private WeatherFetcher weatherFetcher;
    private WeatherViewModel weatherViewModel;

    private final int WEATHER_FORECAST_APP_UPDATE_REQ_CODE = 101;   // for app update
    private static final int PERMISSION_CODE = 1;                   // for user location permission
    private String name, updated_at, description;
    private int pressure, humidity;
    private String temperature, min_temperature, max_temperature, wind_speed;
    private String city = "";
    private final int REQUEST_CODE_EXTRA_INPUT = 101;
    private WeatherActivityHomeBinding weatherActivityHomeBinding;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        weatherActivityHomeBinding = WeatherActivityHomeBinding.inflate(inflater, container, false);
        View view = weatherActivityHomeBinding.getRoot();

        // Hide the display home button as up button
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.hideDisplayHomeUp();

        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        weatherFetcher = new WeatherFetcher();
        weatherFetcher.fetchWeatherData(this);
        weatherViewModel.getWeatherDataList().observe(getViewLifecycleOwner(), list -> {
            hideMainLayout();
            weatherDataList = list;
            updateUI(weatherDataList.get(0));
            setUpDaysRecyclerView(list);
//            DaysAdapter daysAdapter = new DaysAdapter(list, getContext());
//            weatherActivityHomeBinding.dayRv.setLayoutManager(
//                    new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//            );
//            weatherActivityHomeBinding.dayRv.setAdapter(daysAdapter);
            hideProgressBar();
            Log.d(TAG, "Data changed: " + list.get(0).getCityName());
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set navigation bar color
        setNavigationBarColor();

        //check for new app update
//        checkUpdate();

        // set refresh color schemes
        setRefreshLayoutColor();

        // when user do search and refresh
        listeners();

        // getting data using internet connection
        getDataUsingNetwork();
    }

    @Override
    public void onWeatherDataFetched(List<WeatherData> weatherDataList) {
        Log.d(TAG, "onWeatherDataFetched: " + weatherDataList.toString());
        weatherViewModel.setWeatherDataList(weatherDataList);
    }

    private void setNavigationBarColor() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.navBarColor));
        }
    }

    private void setUpDaysRecyclerView(List<WeatherData> list) {
        DaysAdapter daysAdapter = new DaysAdapter(list, requireContext());
        weatherActivityHomeBinding.dayRv.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        weatherActivityHomeBinding.dayRv.setAdapter(daysAdapter);
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(WeatherData weatherData) {
        name = weatherData.getCityName();
        // Condition Icon
        Glide.with(getContext())
                .load("https://openweathermap.org/img/wn/" + weatherData.getIcon() + "@4x.png")
                .fitCenter()
                .into(weatherActivityHomeBinding.layout.conditionIv);
        Log.d(TAG, "https://openweathermap.org/img/wn/" + weatherData.getIcon() + "@4x.png");
        // Convert date and time into day of week
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
        temperature = String.format("%.2f", weatherData.getTemp());
        min_temperature = String.format("%.2f", weatherData.getTempMin());
        max_temperature = String.format("%.2f", weatherData.getTempMax());
        pressure = weatherData.getPressure();
        wind_speed = String.format("%.2f", weatherData.getWindSpeed());
        humidity = weatherData.getHumidity();
        weatherActivityHomeBinding.layout.nameTv.setText(name);
        weatherActivityHomeBinding.layout.updatedAtTv.setText(updated_at);
        weatherActivityHomeBinding.layout.conditionDescTv.setText(description);
        weatherActivityHomeBinding.layout.tempTv.setText(temperature + "°C");
        weatherActivityHomeBinding.layout.minTempTv.setText(min_temperature + "°C");
        weatherActivityHomeBinding.layout.maxTempTv.setText(max_temperature + "°C");
        weatherActivityHomeBinding.layout.pressureTv.setText(pressure + " mb");
        weatherActivityHomeBinding.layout.windTv.setText(wind_speed + " km/h");
        weatherActivityHomeBinding.layout.humidityTv.setText(humidity + "%");
        Log.i(TAG, "UI updated: " + name);
    }

    private void checkUpdate() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(requireContext());
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, requireActivity(), WEATHER_FORECAST_APP_UPDATE_REQ_CODE);
                } catch (IntentSender.SendIntentException exception) {
                    Toaster.errorToast(requireContext(), "Update Failed");
                }
            }
        });
    }

    private void getDataUsingNetwork() {
//        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(requireActivity());
//        //check permission
//        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
//        } else {
//            client.getLastLocation().addOnSuccessListener(location -> {
//                setLongitudeLatitude(location);
//                city = getCityNameUsingNetwork(requireContext(), location);
//                getTodayWeatherInfo(city);
//            });
//        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listeners() {
        weatherActivityHomeBinding.layout.mainLayout.setOnTouchListener((view, motionEvent) -> {
            hideKeyboard(view);
            return false;
        });
        weatherActivityHomeBinding.layout.searchBarIv.setOnClickListener(view ->
                searchCity(weatherActivityHomeBinding.layout.cityEt.getText().toString()));
        weatherActivityHomeBinding.layout.searchBarIv.setOnTouchListener((view, motionEvent) -> {
            searchCity(weatherActivityHomeBinding.layout.cityEt.getText().toString());
            hideKeyboard(view);
            return false;
        });
        weatherActivityHomeBinding.layout.cityEt.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_GO) {
                searchCity(weatherActivityHomeBinding.layout.cityEt.getText().toString());
                hideKeyboard(textView);
                return true;
            }
            return false;
        });
        weatherActivityHomeBinding.layout.cityEt.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                hideKeyboard(view);
            }
        });
        weatherActivityHomeBinding.mainRefreshLayout.setOnRefreshListener(() -> {
            checkConnection();
            Log.i("refresh", "Refresh Done.");
            weatherActivityHomeBinding.mainRefreshLayout.setRefreshing(false);  //for the next time
        });
        //Mic Search
        weatherActivityHomeBinding.layout.micSearchId.setOnClickListener(view -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, REQUEST_CODE_EXTRA_INPUT);
            try {
                //it was deprecated but still work
                startActivityForResult(intent, REQUEST_CODE_EXTRA_INPUT);
            } catch (Exception e) {
                Log.d(TAG, "Mic Error:  " + e);
            }
        });
    }

    private void checkConnection() {
        if (!isInternetConnected(requireContext())) {
            hideMainLayout();
            Toaster.errorToast(requireContext(), "Please check your internet connection");
        } else {
            hideProgressBar();
            getDataUsingNetwork();
        }
    }

    private void setRefreshLayoutColor() {
        weatherActivityHomeBinding.mainRefreshLayout.setProgressBackgroundColorSchemeColor(
                getResources().getColor(R.color.textColor)
        );
        weatherActivityHomeBinding.mainRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.navBarColor)
        );
    }

    private void hideProgressBar() {
        weatherActivityHomeBinding.progress.setVisibility(View.GONE);
        weatherActivityHomeBinding.layout.mainLayout.setVisibility(View.VISIBLE);
    }

    private void hideMainLayout() {
        weatherActivityHomeBinding.layout.mainLayout.setVisibility(View.GONE);
        weatherActivityHomeBinding.progress.setVisibility(View.VISIBLE);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void searchCity(String cityName) {
        hideMainLayout();
        if (cityName == null || cityName.isEmpty()) {
            Toaster.errorToast(requireContext(), "Please enter the city name");
        } else {
            setLatitudeLongitudeUsingCity(cityName);
        }
        hideProgressBar();
    }

    private void setLatitudeLongitudeUsingCity(String cityName) {
        URL.setCity_url(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL.getCity_url(), null, response -> {
            try {
                LocationCord.lat = response.getJSONObject("coord").getString("lat");
                LocationCord.lon = response.getJSONObject("coord").getString("lon");
//                getTodayWeatherInfo(cityName);
                // After the successfully city search the cityEt(editText) is Empty.
                weatherFetcher.fetchWeatherData(this);
                weatherActivityHomeBinding.layout.cityEt.setText("");
                Log.i(TAG, "City updated");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(TAG, "City updated failed");
            }
        }, error -> Toaster.errorToast(requireContext(), "Please enter the correct city name"));
        requestQueue.add(jsonObjectRequest);
    }
}

