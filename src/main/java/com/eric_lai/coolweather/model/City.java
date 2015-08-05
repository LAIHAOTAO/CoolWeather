package com.eric_lai.coolweather.model;

/**
 * Created by laihaotao on 2015/8/4.
 */
public class City {

    private String cityeName;
    private String cityCode;
    private int provinceId;
    private int id;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setCityeName(String cityeName){
        this.cityeName = cityeName;
    }
    public String getcityeName(){
        return cityeName;
    }
    public void setcityCode(String cityCode){
        this.cityCode = cityCode;
    }
    public String getcityCode(){
        return cityCode;
    }
    public void setProvinceId(int provinceId){
        this.provinceId = provinceId;
    }
    public int getProvinceId(){
        return provinceId;
    }
}
