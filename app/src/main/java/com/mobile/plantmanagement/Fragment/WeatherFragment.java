package com.mobile.plantmanagement.Fragment;

import static com.mobile.plantmanagement.Weather.Location.CityFinder.getCityNameUsingNetwork;
import static com.mobile.plantmanagement.Weather.Location.CityFinder.setLongitudeLatitude;
import static com.mobile.plantmanagement.Weather.Network.InternetConnectivity.isInternetConnected;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.mobile.plantmanagement.Weather.Update.UpdateUI;
import com.mobile.plantmanagement.Weather.Url.URL;
import com.mobile.plantmanagement.WeatherCallback;
import com.mobile.plantmanagement.WeatherFetcher;
import com.mobile.plantmanagement.WeatherViewModel;
import com.mobile.plantmanagement.api.*;
import com.mobile.plantmanagement.databinding.FragmentHomeBinding;
import com.mobile.plantmanagement.databinding.WeatherActivityHomeBinding;

import org.json.JSONException;

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
    private final String TAG = "PROFILE";
    private TextView tv_currentDateTime;
    private RecyclerView recyclerView_weatherContainer;
    private WeatherAdapter weatherAdapter;
    private WeatherFetcher weatherFetcher;
    private WeatherViewModel weatherViewModel;


    private final int WEATHER_FORECAST_APP_UPDATE_REQ_CODE = 101;   // for app update
    private static final int PERMISSION_CODE = 1;                   // for user location permission
    private String name, updated_at, description;
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

//
//        weatherActivityHomeBinding = WeatherActivityHomeBinding.inflate(getLayoutInflater());
//        View view = weatherActivityHomeBinding.getRoot();
//        setContentView(view);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.weather_activity_home, container, false);
        weatherActivityHomeBinding = WeatherActivityHomeBinding.inflate(inflater, container, false);
        View view = weatherActivityHomeBinding.getRoot();

        // Hide the display home button as up button
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.hideDisplayHomeUp();

//        tv_currentDateTime = view.findViewById(R.id.tv_currentDateTime);
        recyclerView_weatherContainer = view.findViewById(R.id.recyclerView_weatherContainer);

        weatherAdapter = new WeatherAdapter(requireContext());
        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        weatherFetcher = new WeatherFetcher();
        weatherFetcher.fetchWeatherData(this);
        weatherViewModel.getWeatherDataList().observe(getViewLifecycleOwner(), new Observer<List<WeatherData>>() {
            @Override
            public void onChanged(List<WeatherData> weatherDataList) {
                weatherAdapter = new WeatherAdapter(weatherDataList, requireContext());
//                weatherAdapter.setWeatherDataList(weatherDataList);
//                weatherAdapter.notifyDataSetChanged();
//                recyclerView_weatherContainer.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//                recyclerView_weatherContainer.setAdapter(weatherAdapter);
//                recyclerView_weatherContainer.setHasFixedSize(true);
                Log.d("API called", "Data changed");
            }
        });
//        // HTTPS Request handle

        // Inflate the layout for this fragment




        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set navigation bar color
        setNavigationBarColor();

        //check for new app update
        checkUpdate();

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

    private void setUpDaysRecyclerView() {
        DaysAdapter daysAdapter = new DaysAdapter(requireContext());
        weatherActivityHomeBinding.dayRv.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        weatherActivityHomeBinding.dayRv.setAdapter(daysAdapter);
    }

    @SuppressLint("DefaultLocale")
    private void getTodayWeatherInfo(String name) {
//        URL url = new URL();
//        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url.getLink(), null, response -> {
//            try {
//                this.name = name;
//                update_time = response.getJSONObject("current").getLong("dt");
//                updated_at = new SimpleDateFormat("EEEE hh:mm a", Locale.ENGLISH).format(new Date(update_time * 1000));
//
//                condition = response.getJSONArray("daily").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getInt("id");
//                sunrise = response.getJSONArray("daily").getJSONObject(0).getLong("sunrise");
//                sunset = response.getJSONArray("daily").getJSONObject(0).getLong("sunset");
//                description = response.getJSONObject("current").getJSONArray("weather").getJSONObject(0).getString("main");
//
//                temperature = String.valueOf(Math.round(response.getJSONObject("current").getDouble("temp") - 273.15));
//                min_temperature = String.format("%.0f", response.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getDouble("min") - 273.15);
//                max_temperature = String.format("%.0f", response.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getDouble("max") - 273.15);
//                pressure = response.getJSONArray("daily").getJSONObject(0).getString("pressure");
//                wind_speed = response.getJSONArray("daily").getJSONObject(0).getString("wind_speed");
//                humidity = response.getJSONArray("daily").getJSONObject(0).getString("humidity");
//
//                updateUI();
//                hideProgressBar();
//                setUpDaysRecyclerView();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }, null);
//        requestQueue.add(jsonObjectRequest);
//        Log.i("json_req", "Day 0");
    }

//    @SuppressLint("SetTextI18n")
//    private void updateUI(WeatherData weatherData) {
//        updated_at = weatherData.getTime();
//        updated_at = translate(updated_at);
//        sunrise = weatherData.getSunrise();
//        sunset = weatherData.getSunset();
//        condition = weatherData.getCondition();
//        update_time = weatherData.getDt();
//
//        description = weatherData.getDescriptionLabel();
//        temperature = weatherData.getTemp() - 273.15;
//        min_temperature = weatherData.getTempMin() - 273.15;
//        max_temperature = weatherData.getTempMax() - 273.15;
//        pressure = weatherData.getPressure();
//        wind_speed = weatherData.getWindSpeed();
//        humidity = weatherData.getHumidity();
//
//    }

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
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(requireActivity());
        //check permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        } else {
            client.getLastLocation().addOnSuccessListener(location -> {
                setLongitudeLatitude(location);
                city = getCityNameUsingNetwork(requireContext(), location);
                getTodayWeatherInfo(city);
            });
        }
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
                Log.d("Error Voice", "Mic Error:  " + e);
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

    private String translate(String dayToTranslate) {
        String[] dayToTranslateSplit = dayToTranslate.split(" ");
        dayToTranslateSplit[0] = UpdateUI.TranslateDay(dayToTranslateSplit[0].trim(), requireContext());
        return dayToTranslateSplit[0].concat(" " + dayToTranslateSplit[1]);
    }

    private void hideProgressBar() {
        weatherActivityHomeBinding.progress.setVisibility(View.GONE);
        weatherActivityHomeBinding.layout.mainLayout.setVisibility(View.VISIBLE);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void hideMainLayout() {
        weatherActivityHomeBinding.progress.setVisibility(View.VISIBLE);
        weatherActivityHomeBinding.layout.mainLayout.setVisibility(View.GONE);
    }

    private void searchCity(String cityName) {
        if (cityName == null || cityName.isEmpty()) {
            Toaster.errorToast(requireContext(), "Please enter the city name");
        } else {
            setLatitudeLongitudeUsingCity(cityName);
        }
    }

    private void setLatitudeLongitudeUsingCity(String cityName) {
        URL.setCity_url(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL.getCity_url(), null, response -> {
            try {
                LocationCord.lat = response.getJSONObject("coord").getString("lat");
                LocationCord.lon = response.getJSONObject("coord").getString("lon");
                getTodayWeatherInfo(cityName);
                // After the successfully city search the cityEt(editText) is Empty.
                weatherActivityHomeBinding.layout.cityEt.setText("");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toaster.errorToast(requireContext(), "Please enter the correct city name"));
        requestQueue.add(jsonObjectRequest);
    }
}

