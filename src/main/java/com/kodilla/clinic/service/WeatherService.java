package com.kodilla.clinic.service;

import com.kodilla.clinic.outerapi.weather.client.WeatherClient;
import com.kodilla.clinic.outerapi.weather.forecast.ForecastDto;
import com.kodilla.clinic.outerapi.weather.forecast.WeatherData;
import com.kodilla.clinic.outerapi.weather.forecast.personalized.PersonalizedForecast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WeatherService {
    @Autowired
    private WeatherClient weatherClient;

    private final PersonalizedForecast personalizedForecast = PersonalizedForecast.getInstance();

    public ForecastDto getCurrentForecast() {
        return weatherClient.getForecast();
    }

    public WeatherData getPersonalizedForecast(LocalDateTime visitTime) {
        ForecastDto forecastDto = getCurrentForecast();
        return personalizedForecast.findNearest(visitTime, forecastDto.getWeatherDataList());
    }
}
