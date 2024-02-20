package com.mobile.plantmanagement.Fragment;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.plantmanagement.Calendar.Adapter.EventListAdapter;
import com.mobile.plantmanagement.Calendar.CalendarEvent;
import com.mobile.plantmanagement.Calendar.CalendarEventModel;
import com.mobile.plantmanagement.Calendar.Event.EventDetailDialogFragment;
import com.mobile.plantmanagement.MainActivity;
import com.mobile.plantmanagement.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Declaration of views in Layout
    CalendarView calendarView;
    TextView tv_pickedDate;
    ListView eventListView;
    Button addEventButton;
    View overlayBackground;
    LinearLayout addEventPanel;
    EditText addedEventTitle;
    EditText addedEventContent;
    Button addBtnPanel;
    Button cancelBtnPanel;
    LinearLayout eventListContainer;

    private CalendarEventModel calendarEventModel;
    private EventListAdapter eventListAdapter;
    private List<CalendarEvent> eventList;

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

        initialView(view);

        tv_pickedDate.setAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right));
        eventListContainer.setAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up));

        addEventButton.setOnClickListener(v -> showAddEventPanel());

        // 2 buttons in Add Event Panel
        addBtnPanel.setOnClickListener(v -> {
            saveEvent();
            closeAddEventPanel();
        });
        cancelBtnPanel.setOnClickListener(v -> closeAddEventPanel());

        // Calendar Event liveData declaration
        ViewModelProvider.Factory factory = (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        calendarEventModel = new ViewModelProvider(this, factory).get(CalendarEventModel.class);
        calendarEventModel.getSelectedDateEvents().observe(getViewLifecycleOwner(), events -> {
            eventListAdapter.setDate(datePicked);
            eventListAdapter.removeAll();
            if (events != null) {
                Log.d(TAG, "Events Data changed (Updated)");
                eventListAdapter.addAll(events);
            }
            eventListAdapter.notifyDataSetChanged();
        });

        // Initialize the adapter
        eventList = new ArrayList<>();
        eventListAdapter = new EventListAdapter(getContext(), R.layout.calendar_event_list_layout, eventList, calendarEventModel);
        eventListView.setAdapter(eventListAdapter);

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            resetEvents(year, month, dayOfMonth);
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Fetch the current date events (initialize) when switching to Calendar Fragment
        calendarEventModel.retrieveEvents(datePicked);
        long date = calendarView.getDate();
        calendarView.setDate(date);
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "Clicked");
                String title = eventListAdapter.getEventList().get(position).getTitle();
                String content = eventListAdapter.getEventList().get(position).getContent();
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                    // Show the event detail popup panel
                    EventDetailDialogFragment dialogFragment = new EventDetailDialogFragment(title, content);
                    dialogFragment.show(getActivity().getSupportFragmentManager(), "EventDetailDialogFragment");
                } else {
                    // Handle the case where the fragment or its activity is null
                    Log.e(TAG, "Fragment or activity is null");
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void resetEvents(int year, int month, int dayOfMonth) {
        datePicked = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);

        // Format the picked date
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MM-yyyy", Locale.getDefault());
        String formattedDate = sdf.format(calendar.getTime());
        tv_pickedDate.setText(formattedDate);
        tv_pickedDate.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right));
        eventListContainer.setAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up));
        calendarEventModel.retrieveEvents(datePicked);
        Log.d(TAG, "Change Date picked to: " + datePicked);
        Toast.makeText(getContext(),datePicked,Toast.LENGTH_SHORT).show();
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
        calendarEventModel.updateEvents(datePicked, events);
        addedEventTitle.setText("");
        addedEventContent.setText("");
    }

    private void initialView(View view) {

        // Hide the display home button as up button
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.hideDisplayHomeUp();

        calendarView = view.findViewById(R.id.calendarView);

        tv_pickedDate = view.findViewById(R.id.tv_pickedDate);
        eventListContainer = view.findViewById(R.id.eventList_container);
        eventListView = view.findViewById(R.id.eventListView);
        overlayBackground = view.findViewById(R.id.overlayBackground);
        overlayBackground.setVisibility(View.GONE);
        addEventButton = view.findViewById(R.id.addEventBtn);
        addEventPanel = view.findViewById(R.id.addEventPanel);
        addEventPanel.setVisibility(View.GONE);
        addedEventTitle = view.findViewById(R.id.et_title);
        addedEventContent = view.findViewById(R.id.et_content);
        addBtnPanel = view.findViewById(R.id.addBtnPanel);
        cancelBtnPanel = view.findViewById(R.id.cancelBtnPanel);
    }
}