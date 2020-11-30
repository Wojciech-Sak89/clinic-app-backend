package com.kodilla.clinic.controller.weather;

import com.kodilla.clinic.service.WeatherService;
import com.kodilla.clinic.outerapi.weather.forecast.ForecastDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    @RequestMapping(method = RequestMethod.GET, value = "forecast")
    public ForecastDto getForecast() {
        return weatherService.getCurrentForecast();
    }
}
