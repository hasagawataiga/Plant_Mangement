package com.mobile.plantmanagement.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mobile.plantmanagement.Device.Adapter.DeviceListAdapter;
import com.mobile.plantmanagement.Device.Device;
import com.mobile.plantmanagement.MainActivity;
import com.mobile.plantmanagement.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView noDevice;
    LinearLayout deviceList_container;
    ListView deviceListView;

    private List<Device> deviceList;
    private DeviceListAdapter deviceListAdapter;

    final String TAG = "DEVICE_FRAGMENT";
    public DeviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceFragment newInstance(String param1, String param2) {
        DeviceFragment fragment = new DeviceFragment();
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
        View view = inflater.inflate(R.layout.device_main_layout, container, false);
        // Hide the display home button as up button
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.hideDisplayHomeUp();

        initialView(view);

        deviceList_container.setAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up));
        deviceList = new ArrayList<>();
        deviceListAdapter = new DeviceListAdapter(getContext(), R.layout.device_item_layout, deviceList);
        deviceListView.setAdapter(deviceListAdapter);
        getDeviceInfo();

        // Inflate the layout for this fragment
        return view;
    }

    private void getDeviceInfo () {
        // Temp data until we have the sensor connection
        deviceList.add(new Device("Humidity", "Super", true));
        deviceList.add(new Device("Temperature", "Good", true));
        deviceList.add(new Device("Wind", "Inactive", false));
        deviceList.add(new Device("Soil", "Super", true));
        noDevice.setVisibility(View.GONE);
        Log.d(TAG, deviceListAdapter.getCount() + " devices found");
        deviceListAdapter.notifyDataSetChanged();
        Log.d(TAG, "Device get from database DONE");
    }

    private void initialView(View view) {
        deviceList_container = view.findViewById(R.id.deviceList_container);
        deviceListView = view.findViewById(R.id.deviceListView);
        noDevice = view.findViewById(R.id.no_device_show);
    }
}