package com.kodilla.clinic.outerapi.weather.forecast.personalized;

import com.kodilla.clinic.outerapi.weather.forecast.WeatherData;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class PersonalizedForecast {

    private static PersonalizedForecast personalizedForecastInstance = null;

    private PersonalizedForecast() {
    }

    public static PersonalizedForecast getInstance() {
        if (personalizedForecastInstance == null) {
            synchronized (PersonalizedForecast.class) {
                if (personalizedForecastInstance == null) {
                    personalizedForecastInstance = new PersonalizedForecast();
                }
            }
        }
        return personalizedForecastInstance;
    }

    public WeatherData findNearest(LocalDateTime visitTime, List<WeatherData> forecasts) {
        long visitTimeStamp = visitTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
        return forecasts.stream()
                .filter(forecast -> visitTimeStamp <= forecast.getTimeStamp())
                .findFirst()
                .orElse(null);
    }
}
