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

    @Autowired
    private PersonalizedForecast personalizedForecast;

    public ForecastDto getCurrentForecast() {
        System.out.println("WeatherService.getCurrentForecast()  Current forecast:" + weatherClient.getForecast());
        return weatherClient.getForecast();
    }

    public WeatherData getPersonalizedForecast(LocalDateTime visitTime) {
        ForecastDto forecastDto = getCurrentForecast();
        return personalizedForecast.findNearest(visitTime, forecastDto.getWeatherDataList());
    }
}
