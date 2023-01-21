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

import com.mobile.plantmanagement.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//            setTheme(R.style.Theme_PlantManagement_LightMode_Mode);//when dark mode is enabled, we use the dark theme
//        } else {
//            setTheme(R.style.Theme_PlantManagement_DarkMode_Mode);  //default app theme
//        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentsController();


    }



    private void fragmentsController(){
        // Always begin with Home fragment
        changeFragment(new HomeFragment());
        // Fragment navigation Controller
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home:
                    changeFragment(new HomeFragment());
                    break;
                case R.id.chart:
                    changeFragment(new ChartFragment());
                    break;
                case R.id.profile:
                    changeFragment(new ProfileFragment());
                    break;
                case R.id.settings:
                    changeFragment(new SettingsFragment());
                    break;
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    protected void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout1, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
//        updateHomeUI();
    }
}