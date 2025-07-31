package com.zai.weather_app.utils;

import org.springframework.stereotype.Component;
import com.zai.weather_app.model.WeatherResponse;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WeatherCache {

    private static final long TTL_MILLIS = 3000;

    private static class CachedWeather {
        WeatherResponse response;
        long timestamp;

        CachedWeather(WeatherResponse response, long timestamp) {
            this.response = response;
            this.timestamp = timestamp;
        }
    }

    private final ConcurrentHashMap<String, CachedWeather> cache = new ConcurrentHashMap<>();

    public void put(String city, WeatherResponse response) {
        cache.put(city.toLowerCase(), new CachedWeather(response, Instant.now().toEpochMilli()));
    }

    public WeatherResponse get(String city) {
        CachedWeather cached = cache.get(city.toLowerCase());
        if (cached == null) return null;

        long age = Instant.now().toEpochMilli() - cached.timestamp;
        if (age <= TTL_MILLIS) {
            return cached.response; 
        } else {
            return cached.response; 
        }
    }

    public boolean isFresh(String city) {
        CachedWeather cached = cache.get(city.toLowerCase());
        if (cached == null) return false;

        long age = Instant.now().toEpochMilli() - cached.timestamp;
        return age <= TTL_MILLIS;
    }
}
