package com.alex.weather10.model;

/**
 * Created by Admin on 05.12.2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.weather10.R;
import com.alex.weather10.launch.MainActivity;
import com.alex.weather10.utils.DateUtils;

/**
 * Created by DPosadsky on 07.11.2015.
 */
public class WeatherListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Day> objects;

    public WeatherListAdapter(Context context, ArrayList<Day> days) {
        ctx = context;
        objects = days;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.weather_lista_adapter, parent, false);
        }

        Day d = getDays(position);
        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.Day)).setText("" + getDayInString(DateUtils.getDayOfWeek(d.getDate())));
        ((ImageView) view.findViewById(R.id.Image)).setImageResource(d.getImage(d.getWeatherType()));
        ((TextView) view.findViewById(R.id.Temp)).setText((int)Math.round(Double.parseDouble(d.getTempMin())) + " .. " + (int)Math.round(Double.parseDouble(d.getTempMax())));
        ((TextView) view.findViewById(R.id.Date)).setText("" + d.getDayNumber());
        ((TextView) view.findViewById(R.id.Weather)).setText("" + d.getWeather());
        ((TextView) view.findViewById(R.id.Humidity)).setText(d.getHumidity() + "%");

        return view;

    }

    // товар по позиции
    Day getDays(int position) {
        return ((Day) getItem(position));
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