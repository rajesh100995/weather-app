package com.zai.weather_app.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zai.weather_app.client.WeatherProviderClient;
import com.zai.weather_app.model.WeatherResponse;
import com.zai.weather_app.utils.WeatherCache;

@Service
public class WeatherService {

    @Autowired
    private WeatherProviderClient providerClient;

    @Autowired
    private WeatherCache weatherCache;

    public WeatherResponse getWeather(String city) {
        WeatherResponse cached = weatherCache.get(city);
        if (cached != null && weatherCache.isFresh(city)) {
            return cached;
        }

        try {
            WeatherResponse response = providerClient.getFromWeatherStack(city);
            weatherCache.put(city, response);
            return response;
        } catch (Exception e) {
            try {
                WeatherResponse response = providerClient.getFromOpenWeatherMap(city);
                weatherCache.put(city, response);
                return response;
            } catch (Exception ex) {
                if (cached != null) {
                    return cached;
                }
                return new WeatherResponse(0, 0);
            }
        }
    }
}
