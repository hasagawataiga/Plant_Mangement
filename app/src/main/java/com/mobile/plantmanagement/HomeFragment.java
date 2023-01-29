package com.mobile.plantmanagement;

import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.InputType;
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
import android.widget.Toast;

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
    LinearLayout home_linearLayout_componentsContainer;
    Button home_btn_addComponent;
    String[] units;
    boolean isCalenderUsed = false;
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
        // Hide the display home button as up button
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.hideDisplayHomeUp();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        datePicker = view.findViewById(R.id.home_dayPicker);
        home_btn_switcher = view.findViewById(R.id.home_btn_switcher);
        home_btn_addComponent = view.findViewById(R.id.home_btn_addComponent);
        home_linearLayout_componentsContainer = view.findViewById(R.id.home_linearLayout_componentsContainer);
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                String date = day + " " + month + " " + year;
                Toast.makeText(getContext(),date,Toast.LENGTH_SHORT).show();
            }
        });
        home_btn_switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCalenderUsed){
                    datePicker.setSpinnersShown(true);
                    datePicker.setCalendarViewShown(false);
                    isCalenderUsed = false;
                }else{
                    datePicker.setSpinnersShown(false);
                    datePicker.setCalendarViewShown(true);
                    isCalenderUsed = true;
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
        home_btn_addComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComponent();
            }
        });

    }

    private void addComponent() {
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
        EditText et_component = new EditText(getContext());
        et_component.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        et_component.setHint("Component");
        // Details of component
        EditText et_details = new EditText(getContext());
        et_details.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        et_details.setHint("amount");
        et_details.setInputType(InputType.TYPE_CLASS_NUMBER);
        // Units of component
        Spinner spinner_unit = new Spinner(getContext());
        spinner_unit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        units = getActivity().getResources().getStringArray(R.array.units);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, units);
        spinner_unit.setAdapter(spinnerArrayAdapter);


        parent.addView(btn_del);
        parent.addView(et_component);
        parent.addView(et_details);
        parent.addView(spinner_unit);
    }

    private void delComponent(){

    }
}