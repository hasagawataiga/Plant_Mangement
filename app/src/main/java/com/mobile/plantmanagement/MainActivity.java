package com.mobile.plantmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.mobile.plantmanagement.databinding.ActivityMainBinding;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    TextView tv_signIn;

    final String TAG = "MainActivity";
    ActivityMainBinding binding;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentsController();

        // Sign In textview on Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.sign_in_textview);
        tv_signIn = findViewById(R.id.main_tv_sign_in);

        changeFragment(new ProfileFragment());
        // Listen for changes in the back stack of fragments
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        MenuItem signInItem = menu.findItem(R.id.action_sign_in);
//        signInItem.setActionView(R.layout.sign_in_textview);
//        View view = signInItem.getActionView();
//        tv_signIn = view.findViewById(R.id.main_tv_sign_in);
        tv_signIn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
        });
        return true;
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