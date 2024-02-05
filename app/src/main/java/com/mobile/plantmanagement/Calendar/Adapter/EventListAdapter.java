package com.mobile.plantmanagement.Calendar.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobile.plantmanagement.Calendar.CalendarEvent;
import com.mobile.plantmanagement.Calendar.CalendarEventModel;
import com.mobile.plantmanagement.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventListAdapter extends ArrayAdapter<CalendarEvent>{
    private List<CalendarEvent> eventList;
    private CalendarEventModel calendarEventModel;
    private String date;
    ImageButton removeImgBtn;

    private final String TAG = "CALENDAR_EVENT_ADAPTER";
    public EventListAdapter(@NonNull Context context, int resource, @NonNull List<CalendarEvent> eventList) {
        super(context, resource, eventList);
        this.eventList = eventList;
    }

    public EventListAdapter(@NonNull Context context, int resource, @NonNull List<CalendarEvent> eventList, CalendarEventModel calendarEventModel) {
        super(context, resource);
        this.eventList = eventList;
        this.calendarEventModel = calendarEventModel;
    }

    public EventListAdapter(@NonNull Context context, int resource, CalendarEventModel calendarEventModel) {
        super(context, resource);
        this.eventList = new ArrayList<>();
        this.calendarEventModel = calendarEventModel;
    }

    public void addAll(Map<String, Object> events) {
        Log.d(TAG, "Adding new events: " + events.size());
        for(Map.Entry<String, Object> entry : events.entrySet()){
            CalendarEvent event = new CalendarEvent(entry.getKey(), (String) entry.getValue());
            eventList.add(event);
            Log.d(TAG, "Add new events successful: " + event);
        }
    }

    public void remove(int position) {
        eventList.remove(Integer.valueOf(position));
    }

    public void removeAll() {
        this.eventList = new ArrayList<>();
    }

    public void setDate(String date){
        this.date = date;
        Log.d(TAG, "New Date Set: " + date);
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
        removeImgBtn = convertView.findViewById(R.id.buttonRemove);
        titleTextView.setText(currentEvent.getTitle());
        contentTextView.setText(currentEvent.getContent());
        removeImgBtn.setOnClickListener(v ->
                showRemoveEventDialog(position));

        Log.d(TAG, "Reset " + position + " Event View successful");
        return convertView;
    }

    private void showRemoveEventDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

//         Inflate custom button layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customButtonsView = inflater.inflate(R.layout.calendar_remove_button_dialog, null);

        ImageView positiveButton = customButtonsView.findViewById(R.id.positiveButton);
        ImageView negativeButton = customButtonsView.findViewById(R.id.negativeButton);
        builder.setView(customButtonsView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
                dialog.dismiss();
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void removeItem(int position) {
        calendarEventModel.deleteEvent(date, eventList.get(position));
        eventList.remove(position);
        notifyDataSetChanged();
    }

    public List<CalendarEvent> getEventList() {
        return eventList;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }
}
