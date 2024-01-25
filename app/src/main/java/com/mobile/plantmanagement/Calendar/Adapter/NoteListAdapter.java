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
import com.mobile.plantmanagement.Calendar.CalendarNotes;
import com.mobile.plantmanagement.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoteListAdapter extends ArrayAdapter<CalendarNotes> {
    List<CalendarNotes> noteList;
    private final String TAG = "CALENDAR_NOTE_ADAPTER";
    public NoteListAdapter(@NonNull Context context, int resource, @NonNull List<CalendarNotes> noteList) {
        super(context, resource, noteList);
        this.noteList = noteList;
    }

    public NoteListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.noteList = new ArrayList<>();
    }

    public void addAll(Map<String, Object> notes) {
        Log.d(TAG, "Adding new note: " + notes.size());
        for(Map.Entry<String, Object> entry : notes.entrySet()){
            CalendarNotes note = new CalendarNotes(entry.getKey(), (String) entry.getValue());
            noteList.add(note);
            Log.d(TAG, "Add new note successful: " + note);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.calendar_note_list_layout, parent, false);
        }

        // Get the current item from the data list
        CalendarNotes currentNote = noteList.get(position);

        // Set data to the views
        TextView titleTextView = convertView.findViewById(R.id.note_title);
        TextView contentTextView = convertView.findViewById(R.id.note_content);

        titleTextView.setText("Comment");
        contentTextView.setText(currentNote.getContent());
        Log.d(TAG, "Reset Note View successful");
        return convertView;
    }
}
