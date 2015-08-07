package com.eric_lai.coolweather.util;

import android.content.Context;
import android.text.TextUtils;

import com.eric_lai.coolweather.model.City;
import com.eric_lai.coolweather.model.CoolWeatherDB;
import com.eric_lai.coolweather.model.Country;
import com.eric_lai.coolweather.model.Province;

import org.json.JSONObject;

/**
 * Created by laihaotao on 2015/8/5.
 * 解析服务器返回数据的工具类
 */
public class Utility {

    //解析省级数据
    public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB,
                                                              String response){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length > 0){
                for (String p:allProvinces) {
                    Province province = new Province();
                    String[] array = p.split("\\|");
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    //解析市级数据
    public static boolean handleCityResponse(CoolWeatherDB coolWeatherDB,
                                                              String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    //解析县级数据
    public static boolean handleCountryResponse(CoolWeatherDB coolWeatherDB,
                                                          String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCountries = response.split(",");
            if (allCountries != null && allCountries.length > 0) {
                for (String c : allCountries) {
                    Country country = new Country();
                    String[] array = c.split("\\|");
                    country.setCountryCode(array[0]);
                    country.setCountryName(array[1]);
                    country.setCityId(cityId);
                    coolWeatherDB.saveCountry(country);
                }
                return true;
            }
        }
        return false;
    }

    public static void handleWeatherResponse(Context context, String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publicshTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publicshTime);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void saveWeatherInfo(Context context, String cityName, String weatherCode,
                                        String temp1, String temp2, String weatherDesp,
                                        String publicshTime) {

    }
}
