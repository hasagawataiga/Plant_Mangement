package com.mobile.plantmanagement.Fragment;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.plantmanagement.Calendar.Adapter.EventListAdapter;
import com.mobile.plantmanagement.Calendar.Adapter.NoteListAdapter;
import com.mobile.plantmanagement.Calendar.CalendarEvenListItem;
import com.mobile.plantmanagement.Calendar.CalendarEvent;
import com.mobile.plantmanagement.Calendar.CalendarNotes;
import com.mobile.plantmanagement.Calendar.CalendarViewModel;
import com.mobile.plantmanagement.MainActivity;
import com.mobile.plantmanagement.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // Declaration of views in Layout
    CalendarView calendarView;
    ListView eventListView;
    ListView noteListView;
    Button addEventButton;
    View overlayBackground;
    LinearLayout addEventPanel;
    EditText addedEventTitle;
    EditText addedEventContent;
    Button addBtnPanel;
    Button cancelBtnPanel;

    // Declaration of storing sets (events and notes)
    Map <String, Object> notes;
    private CalendarViewModel calendarViewModel;
    private EventListAdapter eventListAdapter;
    private NoteListAdapter noteListAdapter;
    private List<CalendarEvent> eventList;
    private List<CalendarNotes> noteList;

    String datePicked;
    final String TAG = "CALENDAR FRAGMENT";

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_main_layout, container, false);

        datePicked = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Updated initialization of views for the new layout
        calendarView = view.findViewById(R.id.calendarView);
        eventListView = view.findViewById(R.id.eventListView);
        noteListView = view.findViewById(R.id.noteListView);

        overlayBackground = view.findViewById(R.id.overlayBackground);
        overlayBackground.setVisibility(View.GONE);
        addEventButton = view.findViewById(R.id.addEventBtn);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEventPanel();
            }
        });
        addEventPanel = view.findViewById(R.id.addEventPanel);
        addEventPanel.setVisibility(View.GONE);
        addedEventTitle = view.findViewById(R.id.et_title);
        addedEventContent = view.findViewById(R.id.et_content);
        addBtnPanel = view.findViewById(R.id.addBtnPanel);
        addBtnPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
                closeAddEventPanel();
            }
        });
        cancelBtnPanel = view.findViewById(R.id.cancelBtnPanel);
        cancelBtnPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAddEventPanel();
            }
        });

        // Initialize the adapter
        eventList = new ArrayList<>();
        eventListAdapter = new EventListAdapter(getContext(), R.layout.calendar_event_list_layout, eventList);
        eventListView.setAdapter(eventListAdapter);
        noteList = new ArrayList<>();
        noteListAdapter = new NoteListAdapter(getContext(), R.layout.calendar_note_list_layout, noteList);
        noteListView.setAdapter(noteListAdapter);

        // Hide the display home button as up button
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.hideDisplayHomeUp();

        // LiveData declaration
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        calendarViewModel.getSelectedDateNotes().observe(getViewLifecycleOwner(), new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(Map<String, Object> notes) {
                if (notes != null) {
                    Log.d(TAG, "Notes Data changed (Updated)");
                    noteListAdapter.clear();
                    noteListAdapter.addAll(notes);
                    noteListAdapter.notifyDataSetChanged();
                } else {
                    noteListAdapter.clear();
                    noteListAdapter.notifyDataSetChanged();
                }
            }
        });
        calendarViewModel.getSelectedDateEvents().observe(getViewLifecycleOwner(), new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(Map<String, Object> events) {
                if (events != null) {
                    Log.d(TAG, "Events Data changed (Updated)");
                    eventListAdapter.clear();
                    eventListAdapter.addAll(events);
                    eventListAdapter.notifyDataSetChanged();
                } else {
                    eventListAdapter.clear();
                    eventListAdapter.notifyDataSetChanged();
                }

            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                datePicked = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);
                calendarViewModel.retrieveEvents(datePicked);
                calendarViewModel.retrieveNotes(datePicked);
                Log.d(TAG, "Date Picked: " + datePicked);
                Toast.makeText(getContext(),datePicked,Toast.LENGTH_SHORT).show();
            }
        });

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click here
                CalendarEvent selectedItem = eventList.get(position);
                Toast.makeText(getContext(), "Clicked: " + selectedItem.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // get the date of datePicker view when the fragment firstly initialize
//        getDatePicked(datePicker);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                datePicked = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);
                calendarViewModel.retrieveEvents(datePicked);
                calendarViewModel.retrieveNotes(datePicked);
                Log.d(TAG, "Date Picked: " + datePicked);
                Toast.makeText(getContext(),datePicked,Toast.LENGTH_SHORT).show();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void showAddEventPanel() {
        addEventPanel.setVisibility(View.VISIBLE);
        overlayBackground.setVisibility(View.VISIBLE);
    }

    private void closeAddEventPanel() {
        addEventPanel.setVisibility(View.GONE);
        overlayBackground.setVisibility(View.GONE);
    }

    private void saveEvent() {
        String name = addedEventTitle.getText().toString();
        String content = addedEventContent.getText().toString();
        Map <String, Object> events = new HashMap<>();
        events.put(name, content);
        calendarViewModel.updateEvents(datePicked, events);
    }

    private void resetNotepad(EditText editText){
        editText.setText("");
    }

    private void removeAllChildViews(ViewGroup viewGroup){
        viewGroup.removeAllViewsInLayout();
    }
}