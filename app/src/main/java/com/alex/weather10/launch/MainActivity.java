package com.alex.weather10.launch;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.alex.weather10.R;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.alex.weather10.model.Day;
import com.alex.weather10.model.WeatherListAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import com.alex.weather10.model.DBHelper;
import com.alex.weather10.utils.API;
import com.alex.weather10.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    // Инициализируем наш класс-обёртку
    DBHelper dbh;
    boolean on=false;

    // База нам нужна для записи и чтения
    SQLiteDatabase sqdb;

    ArrayList<Day> days = new ArrayList<Day>();
    WeatherListAdapter weatherListAdapter;

    String cityCode;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle("Ростов-на-Дону");
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        cityCode = "498677";
        dbh = new DBHelper(this);

        if (dbh.isNotNull()) {
            getWeatherFromDB();
        } else
        {
        sqdb = dbh.getWritableDatabase();

        if (isNetworkConnected(getApplicationContext())) {
            getWeather();
            getWeatherFromDB();
        } else
            Toast.makeText(getApplicationContext(), "Отсутствует подключение к Интернету", Toast.LENGTH_SHORT).show();
     }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else
        if (id == R.id.action_update) {
            if (isNetworkConnected(getApplicationContext())) {
               getWeather(); getWeatherFromDB();
            }
            else
                Toast.makeText(getApplicationContext(),"Отсутствует подключение к Интернету", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   // @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_config) {
                Intent intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                return true;

        } else if (id == R.id.nav_help) {
            Intent intent = new Intent();
            intent.setClass(this, AboutActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent();
            intent.setClass(this, ContactActivity.class);
            startActivity(intent);
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getWeather() {
        new AsyncTask<Void, Void, API.ApiResponse>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected API.ApiResponse doInBackground(Void... x) {
                ArrayList<String> params = new ArrayList<String>();
                params.add("id");
                params.add(cityCode);
                params.add("APPID");
                params.add("881d7043bd505696f921d3a7c42954b0");
                params.add("lang");params.add("ru");
                params.add("units");params.add("metric");
                return API.execute(API.ApiMethod.GET_WEATHER.format(), API.HttpMethod.GET, params.toArray(new String[params.size()]));
            }

            @Override
            protected void onPostExecute(API.ApiResponse apiResponse) {
                super.onPostExecute(apiResponse);
                JSONObject dataJsonObj = null;
                try {
                    if (apiResponse.isSuccess()) {
                        int delCount = sqdb.delete(DBHelper.TABLE_NAME_CITY, null, null);
                        int delCount1 = sqdb.delete(DBHelper.TABLE_NAME_DATE, null, null);
                        int delCount2 = sqdb.delete(DBHelper.TABLE_NAME_MAIN, null, null);
                        int delCount3 = sqdb.delete(DBHelper.TABLE_NAME_WEATHER, null, null);
                        int delCount4 = sqdb.delete(DBHelper.TABLE_NAME_WIND, null, null);

                        // создаем объект для данных
                        ContentValues cityInfo = new ContentValues();
                        ContentValues dateInfo = new ContentValues();
                        ContentValues mainInfo = new ContentValues();
                        ContentValues weatherInfo = new ContentValues();
                        ContentValues windInfo = new ContentValues();

                        dataJsonObj = apiResponse.getJson();
                        JSONObject data = dataJsonObj.getJSONObject("city");
                        JSONArray list = dataJsonObj.getJSONArray("list");
                        JSONObject coords = data.getJSONObject("coord");

                        cityInfo.put("name", data.getString("name"));
                        cityInfo.put("coord_lon", coords.getString("lon"));
                        cityInfo.put("coord_lat", coords.getString("lat"));
                        cityInfo.put("country", data.getString("country"));

                        long rowIDCity = sqdb.insert(DBHelper.TABLE_NAME_CITY, null, cityInfo);
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject obj = list.getJSONObject(i);

                            dateInfo.put("date", obj.getString("dt"));
                            dateInfo.put("date_day", DateUtils.getDay(Long.parseLong(obj.getString("dt"))));
                            dateInfo.put("date_text", obj.getString("dt_txt"));

                            JSONObject main = obj.getJSONObject("main");

                            mainInfo.put("temp", main.getString("temp"));
                            mainInfo.put("pressure", main.getString("pressure"));
                            mainInfo.put("humidity", main.getString("humidity"));

                            JSONArray weather = obj.getJSONArray("weather");
                            JSONObject weatherObj = weather.getJSONObject(0);

                            weatherInfo.put("main", weatherObj.getString("main"));
                            if (weatherObj.getString("description").length() > 10)
                                weatherInfo.put("description", weatherObj.getString("description").replace(" ", "\n"));
                            else
                                weatherInfo.put("description", weatherObj.getString("description"));

                            JSONObject wind = obj.getJSONObject("wind");

                            windInfo.put("speed", wind.getString("speed"));

                            long rowIDMain = sqdb.insert(DBHelper.TABLE_NAME_MAIN, null, mainInfo);
                            long rowIDWeather = sqdb.insert(DBHelper.TABLE_NAME_WEATHER, null, weatherInfo);
                            long rowIDWind = sqdb.insert(DBHelper.TABLE_NAME_WIND, null, windInfo);

                            dateInfo.put("main_id", rowIDMain);
                            dateInfo.put("weather_id", rowIDWeather);
                            dateInfo.put("wind_id", rowIDWind);

                            long rowIDDate = sqdb.insert(DBHelper.TABLE_NAME_DATE, null, dateInfo);

                        }
                        getWeatherFromDB();
                    }

                } catch (Exception e) {
                    android.util.Log.e("Weather", "ALERT! ALERT! Exception!", e);
                } finally {

                }
                  }
        }.execute();

    }

    private void getWeatherFromDB() {
        days.clear();
        Day d;
        ArrayList<Integer> daysCount = dbh.getDaysList();
        if (daysCount == null) {
            return;
        }
        for (int i = 0; i < daysCount.size(); ++i) {
            d = new Day();

            d.setDate(dbh.getDate(daysCount.get(i)));
            d.setDayNumber(daysCount.get(i));
            d.setTempMin(dbh.getTempMin(daysCount.get(i)));
            d.setTempMax(dbh.getTempMax(daysCount.get(i)));
            d.setWeatherType(dbh.getWeatherType(daysCount.get(i)));
            d.setWeather(dbh.getWeather(daysCount.get(i)));
            d.setPressure(dbh.getPressure(daysCount.get(i)));
            d.setHumidity(dbh.getHumidity(daysCount.get(i)));
            d.setSpeed(dbh.getSpeed(daysCount.get(i)));
            days.add(d);

        }

        weatherListAdapter = new WeatherListAdapter(MainActivity.this, days);

        // настраиваем список
        ListView lvWeather = (ListView) findViewById(R.id.lvWeathers);
        lvWeather.setAdapter(weatherListAdapter);

        lvWeather.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                                                 Log.d("my_log", "" + position);
                                                 Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                                                 Day d = days.get(position);

                                                 intent.putExtra("date", d.getDate());
                                                 intent.putExtra("dateDay", d.getDayNumber());
                                                 intent.putExtra("tempMin", d.getTempMin());
                                                 intent.putExtra("tempMax", d.getTempMax());
                                                 intent.putExtra("image", d.getImage(d.getWeatherType()));
                                                 intent.putExtra("weatherType", d.getWeatherType());
                                                 intent.putExtra("weather", d.getWeather());
                                                 intent.putExtra("pressure", d.getPressure());
                                                 intent.putExtra("humidity", d.getHumidity());
                                                 intent.putExtra("speed", d.getSpeed());

                                                 startActivity(intent);
                                             }
                                         }
        );
        weatherListAdapter.notifyDataSetChanged();
    }

     protected void onResume() {
        String city = sp.getString(getString(R.string.pref_city), "");
         String time = sp.getString(getString(R.string.pref_time), "");
        if (city.contains("Москва")) {
            cityCode = "524901";
            getSupportActionBar().setTitle("Москва");
        } else
        if (city.contains("Санкт-Петербург")) {
            cityCode = "519690";
            getSupportActionBar().setTitle("Санкт-Петербург");
        } else
        if (city.contains("Ростов-на-Дону")) {
            cityCode = "501175";
            getSupportActionBar().setTitle("Ростов-на-Дону");
        } else
        if (city.contains("Нью-Йорк")) {
            cityCode = "5128581";
            getSupportActionBar().setTitle("Нью-Йорк");
        } else
        if (city.contains("Лондон")) {
            cityCode = "2643743";
            getSupportActionBar().setTitle("Лондон");
        }
         getWeather();
         super.onResume();
         //weatherListAdapter.notifyDataSetChanged();
    }

    private boolean isNetworkConnected(Context _context) {
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }


}
