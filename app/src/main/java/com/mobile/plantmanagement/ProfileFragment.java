package com.mobile.plantmanagement;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobile.plantmanagement.api.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = "PROFILE";
    private TextView tv_currentDateTime;
    private HorizontalScrollView scrollView_horizontalScrollView;
    private RecyclerView recyclerView_weatherContainer;
    private TextView tv_next7Days;
    private ListView lv_weatherForeCast;

    private WeatherAdapter weatherAdapter;
    private double longitude = 21.01;
    private double latitude = 52.23;
    private String appid = "339d49519e5ba2bd213c20c5f73c1a29";
    private String units = "imperial";

    private List<WeatherData> weatherDataList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
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
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Hide the display home button as up button
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.hideDisplayHomeUp();

        tv_currentDateTime = view.findViewById(R.id.tv_currentDateTime);
        scrollView_horizontalScrollView = view.findViewById(R.id.scrollView_horizontalScrollView);
        recyclerView_weatherContainer = view.findViewById(R.id.recyclerView_weatherContainer);
        tv_next7Days = view.findViewById(R.id.tv_next7Days);
        lv_weatherForeCast = view.findViewById(R.id.lv_weatherForecase);

        // HTTPS Request handle
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);

        Call<WeatherResponse> call = weatherAPI.getWeather(latitude, longitude, appid, units);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Status code: " +  response.code());
                    // handle the response and update the UI
                    WeatherResponse weatherResponse = response.body();
                    weatherDataList = weatherResponse.getWeatherDataList();
                    Log.d(TAG, "Get weatherDataList successful");
                } else {
                    Log.d(TAG, "Get response failed. Status code: " + response.code());
                    // handle the error
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // handle the failure
                Log.d(TAG, "Error calling API " + t.getMessage());
            }
        });

        recyclerView_weatherContainer.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView_weatherContainer.setAdapter(weatherAdapter);
        recyclerView_weatherContainer.setHasFixedSize(true);
        weatherAdapter.setWeatherDataList(weatherDataList);
        weatherAdapter.notifyDataSetChanged();
        // Inflate the layout for this fragment
        return view;
    }
}