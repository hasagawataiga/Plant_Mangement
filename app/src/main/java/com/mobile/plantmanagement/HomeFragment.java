package com.mobile.plantmanagement;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviderGetKt;

import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatePicker datePicker;
    Button home_btn_switcher;
    TextView home_tv_save;
    LinearLayout home_linearLayout_componentsContainer;
    EditText home_et_notepad;
    ImageButton home_btn_addComponent;
    String[] units;
    String[] componentList;
    Map <String, Object> events;
    ArrayAdapter<String> spinnerArrayAdapter;
    private CalendarViewModel calendarViewModel;
    String datePicked;
    boolean isCalenderUsed = false;
    final String TAG = "HOME_FRAGMENT";
    public HomeFragment() {
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
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        datePicker = view.findViewById(R.id.home_dayPicker);
        home_tv_save = view.findViewById(R.id.home_tv_save);
        home_btn_switcher = view.findViewById(R.id.home_btn_switcher);
        home_btn_addComponent = view.findViewById(R.id.home_btn_addComponent);
        home_btn_addComponent.setBackgroundResource(R.drawable.btn_add);
        home_btn_addComponent.setImageResource(R.drawable.ic_baseline_add_box_24);
        home_et_notepad = view.findViewById(R.id.home_et_notepad);
        home_linearLayout_componentsContainer = view.findViewById(R.id.home_linearLayout_componentsContainer);
        // Hide the display home button as up button
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.hideDisplayHomeUp();
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        calendarViewModel.getSelectedDateEvents().observe(getViewLifecycleOwner(), new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(Map<String, Object> events) {
                home_linearLayout_componentsContainer.removeAllViews();
                if (events != null){
                    for(Map.Entry<String, Object> entry : events.entrySet()){
                        CalendarEvent event = new CalendarEvent(entry.getKey(), (String) entry.getValue());
                        String name = event.getName();
                        String amount = event.getAmount();
                        String unit = event.getUnit();
                        Log.d(TAG, name + ": " + amount + " " + unit);
                        addComponent(name, amount, unit);
                    }
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

//        HomeFragment homeFragment = (HomeFragment) getActivity().getSupportFragmentManager().findFragmentByTag("HomeFragment");
        datePicker.setOnDateChangedListener((datePicker, i, i1, i2) -> {
            removeAllChildViews(home_linearLayout_componentsContainer);
            resetNotepad(home_et_notepad);
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1;
            int year = datePicker.getYear();
            datePicked = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
            calendarViewModel.retrieveEvents(datePicked);
            Toast.makeText(getContext(),datePicked,Toast.LENGTH_SHORT).show();
        });

        home_btn_switcher.setOnClickListener(view12 -> {
            if(isCalenderUsed){
                datePicker.setSpinnersShown(true);
                datePicker.setCalendarViewShown(false);
                isCalenderUsed = false;
            }else{
                datePicker.setSpinnersShown(false);
                datePicker.setCalendarViewShown(true);
                isCalenderUsed = true;
            }
        });

        home_btn_addComponent.setOnClickListener(view1 -> addComponent("", "", ""));

        home_tv_save.setOnClickListener(v -> {
            events = saveEvents();
            calendarViewModel.updateEvents(datePicked, events);
        });

        super.onViewCreated(view, savedInstanceState);

    }

    private Map<String, Object> saveEvents(){
        Map <String, Object> events = new HashMap<String, Object>();
        for (int i = 0; i < home_linearLayout_componentsContainer.getChildCount(); i++){
            try{
                LinearLayout linearLayout = (LinearLayout) home_linearLayout_componentsContainer.getChildAt(i);
                Spinner spinner_name = (Spinner) linearLayout.getChildAt(1);
                EditText et_amount = (EditText) linearLayout.getChildAt(2);
                Spinner spinner_unit = (Spinner) linearLayout.getChildAt(3);
                String name = spinner_name.getSelectedItem().toString();
                String amount = et_amount.getText().toString();
                String unit = spinner_unit.getSelectedItem().toString();
//                CalendarEvent calendarEvent = new CalendarEvent(name, amount + "@" + unit);
                events.put(name, amount + "@" + unit);
            }catch(Exception e){
                Log.d(TAG, "Error saving events", e);
            }
        }
        return events;
    }

    private void resetNotepad(EditText editText){
        editText.setText("");
    }

    private void removeAllChildViews(ViewGroup viewGroup){
        viewGroup.removeAllViewsInLayout();
    }

    public void addComponent(String name, String amount, String unit) {
        Log.d(TAG, "Starting adding events");
        // Init the linearLayout contains the views of component
        LinearLayout parent = new LinearLayout(getContext());
        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.HORIZONTAL);
        home_linearLayout_componentsContainer.addView(parent);
        // Init attributes of component
        // Delete button
        ImageButton btn_del = new ImageButton(getContext());
        btn_del.setLayoutParams(new LinearLayout.LayoutParams(70,70));
        btn_del.setForegroundGravity(Gravity.CENTER_VERTICAL);
        btn_del.setBackgroundResource(R.drawable.btn_del);
        btn_del.setImageResource(R.drawable.ic_baseline_horizontal_rule_24);

        // Label of component
        Spinner spinner_component = new Spinner(getContext());
        spinner_component.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        componentList = getActivity().getResources().getStringArray(R.array.components);
        spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, componentList);
        spinner_component.setAdapter(spinnerArrayAdapter);
        spinner_component.setGravity(Gravity.LEFT);
        if(name != ""){
            int index = spinnerArrayAdapter.getPosition(name);
            spinner_component.setSelection(index);
            Log.d(TAG, "index of label entry" + index);
        }

        // Details of component
        EditText et_details = new EditText(getContext());
        et_details.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        et_details.setHint("amount");
        et_details.setInputType(InputType.TYPE_CLASS_NUMBER);
        if(amount != ""){
            et_details.setText(amount);
        }

        // Units of component
        Spinner spinner_unit = new Spinner(getContext());
        spinner_unit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        units = getActivity().getResources().getStringArray(R.array.units);
        spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, units);
        spinner_unit.setAdapter(spinnerArrayAdapter);
        spinner_unit.setGravity(Gravity.RIGHT);
        if(unit != ""){
            int index2 = spinnerArrayAdapter.getPosition(unit);
            spinner_unit.setSelection(index2);
        }

        // Add those views to container (LinearLayout named parent)
        parent.addView(btn_del);
        parent.addView(spinner_component);
        parent.addView(et_details);
        parent.addView(spinner_unit);
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ViewGroup) parent.getParent()).removeView(parent);
                Log.d(TAG, "delete button clicked.");
            }
        });
    }

}