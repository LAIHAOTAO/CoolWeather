package com.eric_lai.coolweather.model;

/**
 * Created by laihaotao on 2015/8/4.
 */
public class Province {

    private String provinceName;
    private String provinceCode;
    private int id;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setProvinceName(String provinceName){
        this.provinceName = provinceName;
    }
    public String getProvinceName(){
        return provinceName;
    }
    public void setProvinceCode(String provinceCode){
        this.provinceCode = provinceCode;
    }
    public String getProvinceCode(){
        return provinceCode;
    }
}
