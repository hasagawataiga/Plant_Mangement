package com.mobile.plantmangement;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    SwitchPreferenceCompat appMode;
    final String TAG = "Settings";
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        appMode = findPreference("appMode");

        // Check the theme settings at the beginning whether dark mode is enabled, and set the title of SwitchPreferenceCompat accordingly to the theme settings
        isDarkMode();

        appMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                boolean isChecked = false;
                if(newValue instanceof Boolean){
                    isChecked = ((Boolean) newValue).booleanValue();
                }
                if(isChecked){
                    appMode.setTitle("Dark Mode");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Log.d(TAG, "Switch to dark");
                }else{
                    appMode.setTitle("Light Mode");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Log.d(TAG, "Switch to light");
                }
                return true;
            }
        });
    }

    private void isDarkMode (){
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            appMode.setTitle("Dark Mode");
            appMode.setChecked(true);
            Log.d(TAG, "Currently selected: Dark Mode");
        }else{
            appMode.setTitle("Light Mode");
            appMode.setChecked(false);
            Log.d(TAG, "Currently selected: Light Mode");
        }

    }
}