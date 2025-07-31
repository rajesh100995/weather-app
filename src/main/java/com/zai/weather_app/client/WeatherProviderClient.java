package com.zai.weather_app.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zai.weather_app.model.WeatherResponse;

@Component
public class WeatherProviderClient {

    private static final String WEATHERSTACK_URL_TEMPLATE = "http://api.weatherstack.com/current?access_key=%s&query=%s";

    private static final String OPENWEATHERMAP_URL_TEMPLATE = "http://api.openweathermap.org/data/2.5/weather?q=%s,AU&appid=%s&units=metric";

    private final RestTemplate restTemplate = new RestTemplate();
    
    @Autowired
    ObjectMapper objectMapper;

    @Value("${weatherstack.api.key}")
    private String weatherstackKey;

    @Value("${openweathermap.api.key}")
    private String openWeatherMapKey;

    public WeatherResponse getFromWeatherStack(String city) {
        String url = String.format(WEATHERSTACK_URL_TEMPLATE, weatherstackKey, city);
        String response = restTemplate.getForObject(url, String.class);

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode current = root.path("current");
            int temperature = current.path("temperature").asInt();
            int windSpeed = current.path("wind_speed").asInt();

            return new WeatherResponse(temperature, windSpeed);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse WeatherStack response", e);
        }
    }

    public WeatherResponse getFromOpenWeatherMap(String city) {
        String url = String.format(OPENWEATHERMAP_URL_TEMPLATE, city, openWeatherMapKey);
        String response = restTemplate.getForObject(url, String.class);

        try {
            JsonNode root = objectMapper.readTree(response);
            double tempKelvin = root.path("main").path("temp").asDouble();
            int tempCelsius = (int) Math.round(tempKelvin - 273.15); // Convert to Celsius

            double windSpeed = root.path("wind").path("speed").asDouble();

            return new WeatherResponse(tempCelsius, (int) Math.round(windSpeed));

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse OpenWeatherMap response", e);
        }
    }
}
