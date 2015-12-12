package com.alex.weather10.model;
import com.alex.weather10.R;
/**
 * Created by Admin on 05.12.2015.
 */
public class Day {

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

    public Day() {}

    public Day(long _date) {
        date = _date;
    }

    public void setDate(long dt) {
        date = dt;
    }

    public void setDayNumber(int dayN) {
        dayNumber = dayN;
    }

    public void setWeather(String wth) {
        weather = wth;
    }

    public void setWeatherType(String wthTp) {
        weatherType = wthTp;
    }

    public void setPressure(int pr) {
        pressure = pr;
    }

    public void setHumidity(int hm) {
        humidity = hm;
    }

    public void setSpeed(int sp) {
        speed = sp;
    }

    public void setTempMin(String tm) {
        tempMin = tm;
    }

    public void setTempMax(String tm) {
        tempMax = tm;
    }

    public long getDate() {
        return date;
    }

    public long getDayNumber() {
        return dayNumber;
    }

    public String getWeather() {
        return weather;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getSpeed() {
        return speed;
    }

    public String getTempMin() {
        return tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public int getImage(String wt) {
        switch (wt)
        {
            case "Rain": return(R.drawable.rain);
            case "Clear": return(R.drawable.clear);
            case "Clouds": return(R.drawable.clouds);
            case "Snow": return(R.drawable.snow);
            default: return(R.drawable.na);
        }
    }
}

