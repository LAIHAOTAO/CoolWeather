package com.eric_lai.coolweather.model;

/**
 * Created by laihaotao on 2015/8/4.
 * 县（区）的实体类，辅助数据库操作
 */
public class Country {

    private String countryName;
    private String countryCode;
    private int cityId;
    private int id;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setCountryName(String countryName){
        this.countryName = countryName;
    }
    public String getCountryName(){
        return countryName;
    }
    public void setCountryCode(String countryCode){
        this.countryCode = countryCode;
    }
    public String getCountryCode(){
        return countryCode;
    }
    public void setCityId(int cityId){
        this.cityId = cityId;
    }
    public int getCityId(){
        return cityId;
    }
}
