package com.zai.weather_app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.zai.weather_app.client.WeatherProviderClient;
import com.zai.weather_app.model.WeatherResponse;
import com.zai.weather_app.service.WeatherService;
import com.zai.weather_app.utils.WeatherCache;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherServiceTest {

    @InjectMocks
    private WeatherService weatherService;

    @Mock
    private WeatherProviderClient providerClient;

    @Mock
    private WeatherCache cache;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testWeatherFromPrimaryProvider() {
        WeatherResponse response = new WeatherResponse(15, 10);
        when(cache.get("elbourne")).thenReturn(null);
        when(providerClient.getFromWeatherStack("melbourne")).thenReturn(response);

        WeatherResponse result = weatherService.getWeather("melbourne");
        assertEquals(15, result.getTemperature_degrees());
        assertEquals(10, result.getWind_speed());
    }

    @Test
    void testFailoverToSecondaryProvider() {
        WeatherResponse response = new WeatherResponse(12, 5);
        when(cache.get("melbourne")).thenReturn(null);
        when(providerClient.getFromWeatherStack("melbourne")).thenThrow(new RuntimeException("Down"));
        when(providerClient.getFromOpenWeatherMap("melbourne")).thenReturn(response);

        WeatherResponse result = weatherService.getWeather("melbourne");
        assertEquals(12, result.getTemperature_degrees());
        assertEquals(5, result.getWind_speed());
    }

    @Test
    void testFallbackToStaleCache() {
        WeatherResponse stale = new WeatherResponse(9, 3);
        when(cache.get("melbourne")).thenReturn(stale);
        when(providerClient.getFromWeatherStack("melbourne")).thenThrow(new RuntimeException());
        when(providerClient.getFromOpenWeatherMap("melbourne")).thenThrow(new RuntimeException());

        WeatherResponse result = weatherService.getWeather("melbourne");
        assertEquals(9, result.getTemperature_degrees());
        assertEquals(3, result.getWind_speed());
    }

    @Test
    void testAllFailWithNoCache() {
        when(cache.get("melbourne")).thenReturn(null);
        when(providerClient.getFromWeatherStack("melbourne")).thenThrow(new RuntimeException());
        when(providerClient.getFromOpenWeatherMap("melbourne")).thenThrow(new RuntimeException());

        WeatherResponse result = weatherService.getWeather("melbourne");
        assertEquals(0, result.getTemperature_degrees());
        assertEquals(0, result.getWind_speed());
    }
}
