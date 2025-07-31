package com.zai.weather_app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zai.weather_app.model.WeatherResponse;
import com.zai.weather_app.service.WeatherService;

@RestController
@RequestMapping("/v1/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public ResponseEntity<String> getWeather(@RequestParam(defaultValue = "melbourne") String city) {
        
        try {
            WeatherResponse response = weatherService.getWeather("melbourne");
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(response), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
