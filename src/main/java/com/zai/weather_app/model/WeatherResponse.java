package com.zai.weather_app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class WeatherResponse {
	
    private int wind_speed;
	private int temperature_degrees;

}
