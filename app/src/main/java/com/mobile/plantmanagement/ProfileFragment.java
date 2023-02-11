package com.mobile.plantmanagement;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
public class ProfileFragment extends Fragment implements WeatherCallback{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = "PROFILE";
    private TextView tv_currentDateTime;
    private RecyclerView recyclerView_weatherContainer;

    private WeatherFetcher weatherFetcher;
    private WeatherAdapter weatherAdapter;
    private WeatherViewModel weatherViewModel;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Hide the display home button as up button
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.hideDisplayHomeUp();

        tv_currentDateTime = view.findViewById(R.id.tv_currentDateTime);
        recyclerView_weatherContainer = view.findViewById(R.id.recyclerView_weatherContainer);


        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        weatherFetcher = new WeatherFetcher();
        weatherFetcher.fetchWeatherData(this);
        weatherViewModel.getWeatherDataList().observe(getViewLifecycleOwner(), new Observer<List<WeatherData>>() {
            @Override
            public void onChanged(List<WeatherData> weatherDataList) {
                weatherAdapter = new WeatherAdapter(weatherDataList);
                weatherAdapter.notifyDataSetChanged();
                recyclerView_weatherContainer.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                recyclerView_weatherContainer.setAdapter(weatherAdapter);
                recyclerView_weatherContainer.setHasFixedSize(true);
            }
        });
//        // HTTPS Request handle

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onWeatherDataFetched(List<WeatherData> weatherDataList) {
        Log.d(TAG, "onWeatherDataFetched: " + weatherDataList.toString());
        weatherViewModel.setWeatherDataList(weatherDataList);
    }
}

