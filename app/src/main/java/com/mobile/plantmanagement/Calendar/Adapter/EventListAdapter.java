package com.mobile.plantmanagement.Calendar.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobile.plantmanagement.Calendar.CalendarEvent;
import com.mobile.plantmanagement.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventListAdapter extends ArrayAdapter<CalendarEvent> {
    List<CalendarEvent> eventList;
    private final String TAG = "CALENDAR_EVENT_ADAPTER";
    public EventListAdapter(@NonNull Context context, int resource, @NonNull List<CalendarEvent> eventList) {
        super(context, resource, eventList);
        this.eventList = eventList;
    }

    public EventListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.eventList = new ArrayList<>();
    }

    public void addAll(Map<String, Object> events) {
        Log.d(TAG, "Adding new events: " + events.size());
        for(Map.Entry<String, Object> entry : events.entrySet()){
            CalendarEvent event = new CalendarEvent(entry.getKey(), (String) entry.getValue());
            eventList.add(event);
            Log.d(TAG, "Add new events successful: " + event);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.calendar_event_list_layout, parent, false);
        }

        // Get the current item from the data list
        CalendarEvent currentEvent = eventList.get(position);

        // Set data to the views
        TextView titleTextView = convertView.findViewById(R.id.title);
        TextView contentTextView = convertView.findViewById(R.id.content);

        titleTextView.setText(currentEvent.getTitle());
        contentTextView.setText(currentEvent.getContent());
        Log.d(TAG, "Reset Event View successful");
        return convertView;
    }

}
