package com.mobile.plantmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


import com.mobile.plantmanagement.databinding.ActivityMainBinding;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    final String TAG = "MainActivity";
    ActivityMainBinding binding;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentsController();

        changeFragment(new ProfileFragment());
        // Listen for changes in the back stack of fragments
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    // give me source code for example about retrieve the data from database of Cloud Firestore, stores those data into ViewModel as object and transfer it into view, and otherwise in MVVM using Java

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
        if (item.getItemId() == android.R.id.home) {
            if (canGoBack()) {
                getSupportFragmentManager().popBackStack();
            }
        }
        Log.d(TAG, "onOptionItemSelected clicked.");
        return super.onOptionsItemSelected(item);
    }

    public void hideDisplayHomeUp() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
    }
    public void showDisplayHomeUp(){
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackStackChanged() {
        Log.d(TAG, "Click on Up navigation");
    }

    private boolean canGoBack(){
        return getSupportFragmentManager().getBackStackEntryCount() > 0;
    }
}