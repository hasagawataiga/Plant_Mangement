package com.mobile.plantmanagement.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.mobile.plantmanagement.BuildConfig;
import com.mobile.plantmanagement.MainActivity;
import com.mobile.plantmanagement.R;

public class SettingsFragment extends PreferenceFragmentCompat{
    Preference profile, notification, aboutUs, version;
    final String TAG = "Settings";
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        profile = findPreference("profile_info");
        notification = findPreference("notification_settings");
        aboutUs = findPreference("about_us");
        version = findPreference("app_version");

        // Declare application version to version preference
        String versionName = BuildConfig.VERSION_NAME;
        version.setSummary((CharSequence) versionName);

        profile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Fragment profileFragment = new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout1, profileFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
                return false;
            }
        });

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

    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.hideDisplayHomeUp();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

//    private void isDarkMode (){
//        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
//            appMode.setTitle("Dark Mode");
//            appMode.setChecked(true);
//            Log.d(TAG, "Currently selected: Dark Mode");
//        }else{
//            appMode.setTitle("Light Mode");
//            appMode.setChecked(false);
//            Log.d(TAG, "Currently selected: Light Mode");
//        }
//    }

}