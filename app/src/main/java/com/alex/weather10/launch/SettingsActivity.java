package com.alex.weather10.launch;

/**
 * Created by Admin on 05.12.2015.
 */
import android.os.Bundle;
import android.preference.PreferenceActivity;


import com.alex.weather10.R;




public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // загружаем предпочтения из ресурсов
        addPreferencesFromResource(R.xml.sitings);

    }
}