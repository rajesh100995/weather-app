package com.zai.weather_app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zai.weather_app.client.WeatherProviderClient;
import com.zai.weather_app.model.WeatherResponse;
import com.zai.weather_app.service.WeatherService;
import com.zai.weather_app.utils.WeatherCache;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
        when(cache.get("melbourne")).thenReturn(null);
        when(providerClient.getFromWeatherStack("melbourne")).thenReturn(response);

        WeatherResponse result = weatherService.getWeather("melbourne");
        assertEquals(15, result.getWind_speed());
        assertEquals(10, result.getTemperature_degrees());
    }

    @Test
    void testFailoverToSecondaryProvider() {
        WeatherResponse response = new WeatherResponse(12, 5);
        when(cache.get("melbourne")).thenReturn(null);
        when(providerClient.getFromWeatherStack("melbourne")).thenThrow(new RuntimeException("Down"));
        when(providerClient.getFromOpenWeatherMap("melbourne")).thenReturn(response);

        WeatherResponse result = weatherService.getWeather("melbourne");
        assertEquals(12, result.getWind_speed());
        assertEquals(5, result.getTemperature_degrees());
    }
}
