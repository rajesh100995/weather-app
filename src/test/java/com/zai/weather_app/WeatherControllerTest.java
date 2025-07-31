package com.zai.weather_app;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.zai.weather_app.controller.WeatherController;
import com.zai.weather_app.model.WeatherResponse;
import com.zai.weather_app.service.WeatherService;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    void testGetWeather() throws Exception {
        WeatherResponse mockResponse = new WeatherResponse(20, 7);
        Mockito.when(weatherService.getWeather("melbourne")).thenReturn(mockResponse);

        mockMvc.perform(get("/v1/weather?city=melbourne"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wind_speed", is(20)))
                .andExpect(jsonPath("$.temperature_degrees", is(7)));
    }
}
