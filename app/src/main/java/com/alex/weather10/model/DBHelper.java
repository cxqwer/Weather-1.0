package com.alex.weather10.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Admin on 05.12.2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    private final Context fContext;

    private static final String DATABASE_NAME = "weather.db";
    private  static final int DB_VERSION = 1; // версия БД
    public static final String TABLE_NAME_CITY = "city";
    public static final String TABLE_NAME_DATE = "date";
    public static final String TABLE_NAME_MAIN = "main";
    public static final String TABLE_NAME_WEATHER = "weather";
    public static final String TABLE_NAME_WIND = "wind";

    public String createTableCityString = "CREATE TABLE " + TABLE_NAME_CITY + " (" +
            "id integer PRIMARY KEY AUTOINCREMENT, " +
            "name text NOT NULL, " +
            "coord_lon real NOT NULL, " +
            "coord_lat real NOT NULL, " +
            "country text NOT NULL " +
            ");";

    public String createTableDateString = "CREATE TABLE " + TABLE_NAME_DATE + " (\n" +
            "id integer PRIMARY KEY AUTOINCREMENT, " +
            "date text NOT NULL, " +
            "date_day integer NOT NULL, " +
            "main_id integer NOT NULL, " +
            "weather_id integer NOT NULL, " +
            "wind_id integer NOT NULL, " +
            "date_text text NOT NULL" +
            ");";

    public String createTableMainString = "CREATE TABLE " + TABLE_NAME_MAIN + " (" +
            "id integer PRIMARY KEY AUTOINCREMENT, " +
            "temp real NOT NULL, " +
            "pressure real NOT NULL, " +
            "humidity integer NOT NULL" +
            ");";

    public String createTableWeatherString = "CREATE TABLE " + TABLE_NAME_WEATHER + " (" +
            "id integer PRIMARY KEY AUTOINCREMENT, " +
            "main text NOT NULL, " +
            "description text NOT NULL " +
            ");";

    public String createTableWindString = "CREATE TABLE " + TABLE_NAME_WIND + " (" +
            "id integer PRIMARY KEY AUTOINCREMENT, " +
            "speed real NOT NULL" +
            ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        fContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(createTableCityString);
        db.execSQL(createTableDateString);
        db.execSQL(createTableMainString);
        db.execSQL(createTableWeatherString);
        db.execSQL(createTableWindString);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        if (oldVersion == 1 && newVersion == 2) {

            Log.w("TestBase", "Upgrading database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CITY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DATE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MAIN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WEATHER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WIND);
            onCreate(db);
        }
    }

    public void deleteAllData(SQLiteDatabase db) {

        db.execSQL("DELETE FROM " + TABLE_NAME_CITY);
        db.execSQL("DELETE FROM " + TABLE_NAME_DATE);
        db.execSQL("DELETE FROM " + TABLE_NAME_MAIN);
        db.execSQL("DELETE FROM " + TABLE_NAME_WEATHER);
        db.execSQL("DELETE FROM " + TABLE_NAME_WIND);

    }

    public ArrayList<Integer> getDaysList() {

        Cursor c = getReadableDatabase().rawQuery("select distinct date_day from " + TABLE_NAME_DATE, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        ArrayList<Integer> arr = new ArrayList<Integer>();
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int dtDay = c.getColumnIndex("date_day");

            do {
                // получаем значения по номерам столбцов
                arr.add(c.getInt(dtDay));
            } while (c.moveToNext());
        }
        if (arr == null)
            return null;
        return arr;
    }

    public int getDate(int day) {

        Cursor c = getReadableDatabase().rawQuery("select date from date where date_day = " + day, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        int ar = 0;
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int dtDay = c.getColumnIndex("date");

            do {
                // получаем значения по номерам столбцов
                ar = c.getInt(dtDay);
                return ar;
            } while (c.moveToNext());
        }

        return ar;
    }

    public String getTempMin(int day) {

        Cursor c = getReadableDatabase().rawQuery("select min(temp) from date d join main m on d.main_id = m.id where d.date_day =  " + day + "", null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        String minTemp = "";
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int minTmp = c.getColumnIndex("min(temp)");

            do {
                // получаем значения по номерам столбцов
                minTemp = c.getString(minTmp);
                //Log.d("my_log", minTemp);
            } while (c.moveToNext());
        }

        return minTemp;

    }

    public String getTempMax(int day) {

        Cursor c = getReadableDatabase().rawQuery("select max(temp) from date d join main m on d.main_id = m.id where d.date_day =  " + day + "", null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        String maxTemp = "";
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int maxTmp = c.getColumnIndex("max(temp)");

            do {
                // получаем значения по номерам столбцов
                maxTemp = c.getString(maxTmp);
                //Log.d("my_log", maxTemp);
            } while (c.moveToNext());
        }

        return maxTemp;

    }

    public String getWeatherType(int day) {

        Cursor c = getReadableDatabase().rawQuery("select main from date d join weather w on d.weather_id = w.id where d.date_day =  " + day + "", null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        ArrayList<String> main = new ArrayList<String>();
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int maxTmp = c.getColumnIndex("main");

            do {
                // получаем значения по номерам столбцов
                main.add(c.getString(maxTmp));
                //Log.d("my_log", maxTemp);
            } while (c.moveToNext());

        }

        if (main.size() > 3)
            return main.get(main.size() - 4);
        else
        if (main.size() != 0)
            return main.get(0);
        else return null;

    }

    public String getWeather(int day) {

        Cursor c = getReadableDatabase().rawQuery("select description from date d join weather w on d.weather_id = w.id where d.date_day =  " + day + "", null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        ArrayList<String> main = new ArrayList<String>();
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int maxTmp = c.getColumnIndex("description");

            do {
                // получаем значения по номерам столбцов
                main.add(c.getString(maxTmp));
                //Log.d("my_log", maxTemp);
            } while (c.moveToNext());

        }

        if (main.size() > 3)
            return main.get(main.size() - 4);
        else
        if (main.size() != 0)
            return main.get(0);
        else return null;

    }

    public int getPressure(int day) {

        Cursor c = getReadableDatabase().rawQuery("select avg(pressure) from date d join main m on d.main_id = m.id where d.date_day =  " + day + "", null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        int pr = 0;
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int prId = c.getColumnIndex("avg(pressure)");

            do {
                // получаем значения по номерам столбцов
                pr = (int)Math.round(c.getDouble(prId));
                //Log.d("my_log", maxTemp);
            } while (c.moveToNext());

        }

        return pr;

    }

    public int getHumidity(int day) {

        Cursor c = getReadableDatabase().rawQuery("select avg(humidity) from date d join main m on d.main_id = m.id where d.date_day =  " + day + "", null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        int h = 0;
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int maxTmp = c.getColumnIndex("avg(humidity)");

            do {
                // получаем значения по номерам столбцов
                h = (int)Math.round(c.getDouble(maxTmp));
                //Log.d("my_log", maxTemp);
            } while (c.moveToNext());

        }

        return h;

    }

    public int getSpeed(int day) {

        Cursor c = getReadableDatabase().rawQuery("select avg(speed) from date d join wind w on d.wind_id = w.id where d.date_day =  " + day + "", null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        int sp = 0;
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int spId = c.getColumnIndex("avg(speed)");

            do {
                // получаем значения по номерам столбцов
                sp = (int)Math.round(c.getDouble(spId));
                //Log.d("my_log", maxTemp);
            } while (c.moveToNext());

        }

        return sp;

    }
    public boolean isNotNull()
    {
        if (getDaysList()==null) return false;
        return true;
    }


}
