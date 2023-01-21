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
import android.widget.FrameLayout;

import com.mobile.plantmanagement.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    final String TAG = "MainActivity";
    ActivityMainBinding binding;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//            setTheme(R.style.Theme_PlantManagement_LightMode_Mode);//when dark mode is enabled, we use the dark theme
//        } else {
//            setTheme(R.style.Theme_PlantManagement_DarkMode_Mode);  //default app theme
//        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentsController();

//        frameLayout = findViewById(R.id.frameLayout1);
        changeFragment(new HomeFragment());
        // Listen for changes in the back stack of fragments
        ActionBar actionBar = getSupportActionBar();
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        // Check whether fragment could go up. Display back_button if yes, otherwise
    }



    private void fragmentsController(){
        // Always begin with Home fragment
        changeFragment(new HomeFragment());
        // Fragment navigation Controller
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.profile:
                    Log.d(TAG, "Click profile fragment");
                    changeFragment(new ProfileFragment());
//                    hideDisplayHomeUp();
                    break;
                case R.id.home:
                    Log.d(TAG, "Click home fragment");
                    changeFragment(new HomeFragment());
//                    hideDisplayHomeUp();
                    break;
                case R.id.chart:
                    Log.d(TAG, "Click chart fragment");
                    changeFragment(new ChartFragment());
//                    hideDisplayHomeUp();
                    break;
                case R.id.settings:
                    Log.d(TAG, "Click settings fragment");
                    changeFragment(new SettingsFragment());
                    break;
                default:
                    Log.d(TAG, "not matched any fragment id");
            }
            isDisplayHomeUp();
            return true;
        });
    }

//    @Override
//    public void onBackPressed() {
//        if (getFragmentManager().getBackStackEntryCount() > 0) {
//            getFragmentManager().popBackStack();
//        } else {
//            super.onBackPressed();
//        }
//    }

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
            case R.id.profile:
                Log.d(TAG, "Click profile fragment");
                changeFragment(new ProfileFragment());
                break;
            case R.id.home:
                Log.d(TAG, "Click home fragment");
                changeFragment(new HomeFragment());
                break;
            case R.id.chart:
                Log.d(TAG, "Click chart fragment");
                changeFragment(new ChartFragment());
                break;
            case R.id.settings:
                Log.d(TAG, "Click settings fragment");
                changeFragment(new SettingsFragment());
                break;
        }
        isDisplayHomeUp();
        return super.onOptionsItemSelected(item);
    }

    private void hideDisplayHomeUp() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onBackStackChanged() {
        Log.d(TAG, "Click on Up navigation");
        isDisplayHomeUp();
    }
    private void isDisplayHomeUp(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack());
    }
    private boolean canGoBack(){
        boolean canGoBack = false;
        canGoBack = getSupportFragmentManager().getBackStackEntryCount() > 0;
        return canGoBack;
    }
//    @Override
//    public boolean onSupportNavigateUp() {
//
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        getSupportFragmentManager().popBackStack();
//        return true;
//    }
}