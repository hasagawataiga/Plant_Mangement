package com.mobile.plantmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.mobile.plantmanagement.databinding.ActivityMainBinding;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    final String TAG = "MainActivity";
    ActivityMainBinding binding;
    FrameLayout frameLayout;
    DatePicker datePicker;
    FragmentManager fragmentManager;
    LinearLayout home_linearLayout_componentsContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentsController();

//        home_linearLayout_componentsContainer = (LinearLayout) findViewById(R.id.home_linearLayout_componentsContainer);
//        Log.d(TAG, "linearLayout id " + home_linearLayout_componentsContainer + " " + home_linearLayout_componentsContainer.getOrientation());

//        datePicker = (DatePicker) findViewById(R.id.home_dayPicker);
//        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
//
//            }
//        });
//        frameLayout = findViewById(R.id.frameLayout1);
        changeFragment(new HomeFragment());
        // Listen for changes in the back stack of fragments
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }


    private void fragmentsController(){
        // Always begin with Home fragment
        changeFragment(new HomeFragment());
        // Fragment navigation Controller
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.profile:
                    Log.d(TAG, "Binding Click profile fragment");
                    changeFragment(new ProfileFragment());
                    break;
                case R.id.home:
                    Log.d(TAG, "Binding Click home fragment");
                    changeFragment(new HomeFragment());
                    break;
                case R.id.chart:
                    Log.d(TAG, "Binding Click chart fragment");
                    changeFragment(new ChartFragment());
                    break;
                case R.id.settings:
                    Log.d(TAG, "Binding Click settings fragment");
                    changeFragment(new SettingsFragment());
                    break;
                default:
                    Log.d(TAG, "Binding not matched any fragment id");
            }
            return true;
        });
    }


    protected void changeFragment(Fragment fragment){
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout1, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Up navigation for fragments
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                if(canGoBack()){
                    getSupportFragmentManager().popBackStack();
                }
                break;
        }
        Log.d(TAG, "onOptionItemSelected clicked.");
        return super.onOptionsItemSelected(item);
    }

    public void hideDisplayHomeUp() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
    public void showDisplayHomeUp(){getSupportActionBar().setDisplayHomeAsUpEnabled(true);}

    @Override
    public void onBackStackChanged() {
        Log.d(TAG, "Click on Up navigation");
    }

    private boolean canGoBack(){
        boolean canGoBack = false;
        canGoBack = getSupportFragmentManager().getBackStackEntryCount() > 0;
        return canGoBack;
    }
}