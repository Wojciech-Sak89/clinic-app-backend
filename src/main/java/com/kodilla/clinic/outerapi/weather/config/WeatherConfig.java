package com.kodilla.clinic.outerapi.weather.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class WeatherConfig {
    @Value("${openweather.api.endpoint.prod}")
    private String openWeatherApiEndpoint;

    @Value("${openweather.app.key}")
    private String openWeatherAppKey;

    @Value("${openweather.app.forecast}")
    private String forecast;

    @Value("${openweather.app.units}")
    private String units;

    @Value("${openweather.app.forecast.long-term.extent.every-next-three-hours-iteration_up-to-five-days}")
    private String forecastExtent;
}
