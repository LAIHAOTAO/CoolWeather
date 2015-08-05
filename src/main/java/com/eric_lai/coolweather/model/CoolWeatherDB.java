package com.eric_lai.coolweather.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eric_lai.coolweather.db.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laihaotao on 2015/8/4.
 */
public class CoolWeatherDB {

    //数据库名称
    private static final String DB_NAME = "cool_weather";
    //数据库版本
    private static final int VERSION = 1;

    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    private CoolWeatherDB(Context context){
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
    }


    public synchronized static CoolWeatherDB getInstance(Context context){
        if(coolWeatherDB == null){
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    //将province保存到数据库
    public void saveProvince(Province province){
        if(province != null){
            ContentValues Values = new ContentValues();
            Values.put("province_name", province.getProvinceName());
            Values.put("province_code", province.getProvinceCode());
            db.insert("Province", null, Values);
        }
    }
    //从数据库读出国内所有的所有city
    public List<Province> loadProvinces(){
        List<Province> provincesList = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("provinceName")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("provinceCode")));
                provincesList.add(province);
            }while(cursor.moveToNext());
        }
        if(cursor != null){
            cursor.close();
        }
        return provincesList;
    }

    //将city保存到数据库
    public void saveCity(City city){
        if(city != null){
            ContentValues Values = new ContentValues();
            Values.put("city_name", city.getcityeName());
            Values.put("city_code", city.getcityCode());
            Values.put("province_id", city.getProvinceId());
            db.insert("City", null, Values);
        }
    }
    //从数据库读出某province下的所有city
    public List<City> loadCities(int provinceId){
        List<City> citiesList= new ArrayList<City>();
        Cursor cursor = db.query("City", null, "province_id=?",new String[]{String.valueOf(provinceId)}, null, null, null);
        do{
            City city = new City();
            city.setId(cursor.getInt(cursor.getColumnIndex("id")));
            city.setCityeName(cursor.getString(cursor.getColumnIndex("cityName")));
            city.setcityCode(cursor.getString(cursor.getColumnIndex("cityCode")));
            city.setProvinceId(cursor.getInt(cursor.getColumnIndex("provinceId")));
            citiesList.add(city);
        }while(cursor.moveToNext());
        if( cursor != null){
            cursor.close();
        }
        return citiesList;
    }

    //将country保存到数据库
    public void saveCountry(Country country){
        if(country != null){
            ContentValues Values = new ContentValues();
            Values.put("city_name", country.getCountryName());
            Values.put("city_code", country.getCountryCode());
            Values.put("province_id", country.getCityId());
            db.insert("Country", null, Values);
        }
    }
    //从数据库读出某city下的所有country
    public List<Country> loadCountries(int cityId){
        List<Country> countriesList= new ArrayList<Country>();
        Cursor cursor = db.query("Country", null, "city_id=?",new String[]{String.valueOf(cityId)}, null, null, null);
        do{
            Country country = new Country();
            country.setId(cursor.getInt(cursor.getColumnIndex("id")));
            country.setCountryName(cursor.getString(cursor.getColumnIndex("countryName")));
            country.setCountryCode(cursor.getString(cursor.getColumnIndex("countryCode")));
            country.setCityId(cursor.getInt(cursor.getColumnIndex("cityId")));
            countriesList.add(country);
        }while(cursor.moveToNext());
        if( cursor != null){
            cursor.close();
        }
        return countriesList;
    }
}
