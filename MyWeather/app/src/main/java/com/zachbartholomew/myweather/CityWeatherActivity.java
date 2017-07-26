package com.zachbartholomew.myweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.zachbartholomew.myweather.interfaces.WeatherService;
import com.zachbartholomew.myweather.model.WeatherData;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CityWeatherActivity extends AppCompatActivity {

    private static final String TAG = CityWeatherActivity.class.getSimpleName();

    @BindView(R.id.cityText)
    TextView cityTextView;
    @BindView(R.id.condIcon)
    ImageView condIconImageView;
    @BindView(R.id.condDescr)
    TextView condDescTextView;
    @BindView(R.id.temp)
    TextView tempTextView;
    @BindView(R.id.pressLab)
    TextView pressLabTextView;
    @BindView(R.id.press)
    TextView pressTextView;
    @BindView(R.id.humLab)
    TextView humLabTextView;
    @BindView(R.id.hum)
    TextView humTextView;
    @BindView(R.id.windLab)
    TextView windLabTextView;
    @BindView(R.id.windDeg)
    TextView windDegTextView;
    @BindView(R.id.windSpeed)
    TextView windSpeedTextView;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        final String city = extras.getString("city");

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .build();

        WeatherService weatherService = retrofit.create(WeatherService.class);
        Observable<WeatherData> cityWeather = weatherService.getWeatherData(city, WeatherService.API_KEY);
//        Observable<WeatherData> cityWeather = weatherService.getWeatherData("Seattle, WA", WeatherService.API_KEY);

        cityWeather.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherData>() {
                    @Override
                    public void accept(WeatherData weatherData) throws Exception {
                        Log.v(TAG, "Current Weather: " + weatherData.getWeather().get(0).getDescription()
                                + "\nName: " + weatherData.getName()
                                + "\nWind: " + weatherData.getWind().getSpeed()
                                + "\nTemp: " + weatherData.getMain().getTempMax());

                        try {
                            cityTextView.setText(weatherData.getName());
                            condDescTextView.setText(weatherData.getWeather().get(0).getDescription());
                            tempTextView.setText(Double.toString(weatherData.getMain().getTemp()));
                            pressTextView.setText(weatherData.getMain().getPressure().toString());
                            windSpeedTextView.setText(Double.toString(weatherData.getWind().getSpeed()));
                            windDegTextView.setText(weatherData.getWind().getDeg().toString());
                            humTextView.setText(weatherData.getMain().getHumidity().toString());
                        } catch (Exception e) {
                            Log.e(TAG, "accept: " + e.getMessage());
                        }
                    }
                });
    }
}
