package com.alex.weather10.model;
import java.util.ArrayList;
/**
 * Created by Admin on 05.12.2015.
 */
public class CityList {
    private ArrayList<City> cities;

    private static CityList instance;

    private void generateCitiesList(){
        this.cities.add(new City("Москва","524901","ru"));
        this.cities.add(new City("Санкт-Петербург","519690","ru"));
        this.cities.add(new City("Ростов-на-Дону","501175","ru"));
            }

    private CityList(){
        this.cities = new ArrayList<>();
        generateCitiesList();
    }
    public static CityList getInstance(){
        if(instance == null){
            return new CityList();
        }
        else{
            return instance;
        }
    }

    public ArrayList<City> getCities(){
        return cities;
    }
    public int getCount(){
        return this.cities.size();
    }
}
