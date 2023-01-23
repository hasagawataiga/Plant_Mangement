package com.mobile.plantmanagement;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat{

    SwitchPreferenceCompat appMode;
    Preference aboutUs;
    EditTextPreference version;
    final String TAG = "Settings";
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        appMode = findPreference("appMode");
        aboutUs = findPreference("aboutUs");
        version = findPreference("version");
        // Check the theme settings at the beginning whether dark mode is enabled, and set the title of SwitchPreferenceCompat accordingly to the theme settings
        isDarkMode();

        // Declare application version to version preference
        String versionName = BuildConfig.VERSION_NAME;
        version.setSummary((CharSequence) versionName);

        aboutUs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Fragment nextFrag= new AboutUsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout1, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
                return false;
            }
        });

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

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.hideDisplayHomeUp();
        return super.onCreateView(inflater, container, savedInstanceState);
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