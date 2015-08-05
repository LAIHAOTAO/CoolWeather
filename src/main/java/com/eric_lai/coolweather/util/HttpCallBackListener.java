package com.eric_lai.coolweather.util;

/**
 * Created by laihaotao on 2015/8/5.
 */
public interface HttpCallBackListener {
    void onFinish(String response);
    void onError(Exception e);
}
