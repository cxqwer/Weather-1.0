package com.alex.weather10.launch;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.weather10.R;
import com.alex.weather10.utils.DateUtils;

public class DetailActivity extends AppCompatActivity {
    long date;
    int dayNumber;

    private int image;

    private String tempMin;
    private String tempMax;

    private String weatherType;
    private String weather;
    private int pressure;
    private int humidity;
    private int speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        date = getIntent().getExtras().getInt("date");
        dayNumber = getIntent().getExtras().getInt("dateDay");

        image = getIntent().getExtras().getInt("image");

        tempMin = getIntent().getExtras().getString("tempMin");
        tempMax = getIntent().getExtras().getString("tempMax");

        weatherType = getIntent().getExtras().getString("weatherType");
        weather = getIntent().getExtras().getString("weather");

        pressure = getIntent().getExtras().getInt("pressure");
        humidity = getIntent().getExtras().getInt("humidity");
        speed = getIntent().getExtras().getInt("speed");


        ((ImageView) findViewById(R.id.Image)).setImageResource(image);
        ((TextView) findViewById(R.id.Weather)).setText("" + weather);
        ((TextView) findViewById(R.id.Day)).setText("" + getDayInString(DateUtils.getDayOfWeek(date)));
        ((TextView) findViewById(R.id.Date)).setText("" + DateUtils.getDayOfWeek(date) + " " + DateUtils.getMonth(date));
        ((TextView) findViewById(R.id.TempMin)).setText("От " + (int)Math.round(Double.parseDouble(tempMin)) + " c°");
        ((TextView) findViewById(R.id.TempMax)).setText("До " + (int)Math.round(Double.parseDouble(tempMax)) + " c°");

        ((TextView) findViewById(R.id.Speed)).setText("Ветер " + speed + " м/с");
        ((TextView) findViewById(R.id.Humidity)).setText("Влажность " + humidity + "%");
    }

    public String getDayInString(int day) {
        switch (day)
        {
            case 1: return("Воскресенье");
            case 2: return("Понедельник");
            case 3: return("Вторник");
            case 4: return("Среда");
            case 5: return("Четверг");
            case 6: return("Пятница");
            case 7: return("Суббота");
            default: return("День");
        }
    }


}
