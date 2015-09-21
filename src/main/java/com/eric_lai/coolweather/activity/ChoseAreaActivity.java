package com.eric_lai.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eric_lai.coolweather.R;
import com.eric_lai.coolweather.model.City;
import com.eric_lai.coolweather.model.CoolWeatherDB;
import com.eric_lai.coolweather.model.Country;
import com.eric_lai.coolweather.model.Province;
import com.eric_lai.coolweather.util.HttpCallBackListener;
import com.eric_lai.coolweather.util.HttpUtil;
import com.eric_lai.coolweather.util.Utility;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class ChoseAreaActivity extends Activity {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTRY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList = new ArrayList<String>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<Country> countryList;

    private Province selectedProvince;
    private City selectedCity;

    private int currentLevel;

    private boolean isFromeWeatherActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromeWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("city_selected", false) && !isFromeWeatherActivity){
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_chose_area);

        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }
                else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCountries();
                }else if(currentLevel == LEVEL_COUNTRY){
                    String countryCode = countryList.get(position).getCountryCode();
                    Intent intent = new Intent(ChoseAreaActivity.this, WeatherActivity.class);
                    intent.putExtra("country_code", countryCode);
                    startActivity(intent);
                    finish();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces() {
        //从数据库拿出所有的省份
        provinceList = coolWeatherDB.loadProvinces();
        //开始从本地查询
        if(provinceList.size() >0){
            //清空缓存
            dataList.clear();
            //遍历所有省份，并添加到缓存当中
            for(Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            //
            adapter.notifyDataSetChanged();
            //
            listView.setSelection(0);
            //设置头布局名称
            titleText.setText("中国");
            //将当前状态改为省份
            currentLevel=LEVEL_PROVINCE;
        }else{
            //本地数据库没有资料，向服务器查询
            queryFromService(null, "province");
        }
    }

    private void queryCities() {
        cityList = coolWeatherDB.loadCities(selectedProvince.getId());
        if(cityList.size() > 0){
            dataList.clear();
            for(City city:cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        }else{
            queryFromService(selectedProvince.getProvinceCode(), "city");
        }
    }

    private void queryCountries() {
        countryList = coolWeatherDB.loadCountries(selectedCity.getId());
        if(countryList.size() > 0){
            dataList.clear();
            for (Country country:countryList){
                dataList.add(country.getCountryName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTRY;
        }else{
            queryFromService(selectedCity.getCityCode(), "country");
        }
    }

    //从服务器查找数据
    private void queryFromService(final String code, final String type) {
        String address;
        if(!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        }else{
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener(){

            @Override
            public void onFinish(String response) {
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(coolWeatherDB, response);
                }else if("city".equals(type)){
                    result = Utility.handleCityResponse(coolWeatherDB, response, selectedProvince.getId());
                }else if("country".equals(type)){
                    result = Utility.handleCountryResponse(coolWeatherDB, response, selectedCity.getId());
                }
                if(result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("country".equals(type)){
                                queryCountries();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChoseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //关闭进度框
    private void closeProgressDialog() {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    //显示进度框
    private void showProgressDialog() {
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    //根据不同的层次来决定back键的作用
    @Override
    public void onBackPressed(){
        if(currentLevel == LEVEL_COUNTRY){
            queryCities();
        }else if (currentLevel == LEVEL_CITY){
            queryProvinces();
        }else{
            if (isFromeWeatherActivity){
                Intent intent = new Intent(this,WeatherActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }

}
