package com.zai.weather_app;

import org.junit.jupiter.api.Test;

import com.zai.weather_app.model.WeatherResponse;
import com.zai.weather_app.utils.WeatherCache;

import static org.junit.jupiter.api.Assertions.*;

class WeatherCacheTest {

    @Test
    void testCacheStoresAndRetrievesFreshData() throws InterruptedException {
        WeatherCache cache = new WeatherCache();
        WeatherResponse response = new WeatherResponse(15, 5);

        cache.put("melbourne", response);

        assertNotNull(cache.get("melbourne"));
        assertTrue(cache.isFresh("melbourne"));
    }

    @Test
    void testCacheBecomesStaleAfter3Seconds() throws InterruptedException {
        WeatherCache cache = new WeatherCache();
        WeatherResponse response = new WeatherResponse(22, 8);

        cache.put("melbourne", response);
        Thread.sleep(3100); 

        assertNotNull(cache.get("melbourne"));  
        assertFalse(cache.isFresh("melbourne"));
    }

}