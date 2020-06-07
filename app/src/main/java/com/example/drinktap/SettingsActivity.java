package com.example.drinktap;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        // The settings of the weight and gender are automatically saved
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        // If there is a top bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Then allow to display the back button.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            // root_preferences.xml contains the layout of the settings which is necessary to it,
            // to automatically save the settings.
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}