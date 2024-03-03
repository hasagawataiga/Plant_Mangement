package com.mobile.plantmanagement.Device.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobile.plantmanagement.Device.Device;
import com.mobile.plantmanagement.R;

import java.util.List;

public class DeviceListAdapter extends ArrayAdapter<Device> {
    private List<Device> deviceList;
    private final String TAG = "DEVICE_FRAGMENT";
    public DeviceListAdapter(@NonNull Context context, int resource, @NonNull List<Device> deviceList) {
        super(context, resource);
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d(TAG, "Device " + position + " fetching");
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_item_layout, parent, false);
        }

        Device device = deviceList.get(position);
        convertView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left));

        TextView tv_name = convertView.findViewById(R.id.device_name);
        TextView tv_status = convertView.findViewById(R.id.device_status);
        Switch sb_device = convertView.findViewById(R.id.device_switch_btn);

        tv_name.setText(device.getName());
        tv_status.setText(device.getStatus());
        sb_device.setChecked(device.isActive());
        Log.d(TAG, "Device " + position + " updated");
        return convertView;
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }
}
