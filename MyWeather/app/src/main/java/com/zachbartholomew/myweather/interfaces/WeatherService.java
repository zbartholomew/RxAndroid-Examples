package com.zachbartholomew.myweather.interfaces;

import com.zachbartholomew.myweather.model.WeatherData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zach on 7/25/2017.
 */

public interface WeatherService {

    String API_KEY = "859cfe62ba9fe1bd1d15b1d5a52e448e";

    @GET("weather")
    Observable<WeatherData> getWeatherData(@Query("q") String city, @Query("APPID") String key);

}

