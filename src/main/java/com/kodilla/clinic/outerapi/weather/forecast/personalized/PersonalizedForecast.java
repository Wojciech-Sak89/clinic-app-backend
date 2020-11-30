package com.kodilla.clinic.outerapi.weather.forecast.personalized;

import com.kodilla.clinic.outerapi.weather.forecast.WeatherData;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class PersonalizedForecast {

    public WeatherData findNearest(LocalDateTime visitTime, List<WeatherData> forecasts) {
        long visitTimeStamp = visitTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
        System.out.println("!!!!!!!!!!!!  visitTimeStamp" + visitTimeStamp);
        return forecasts.stream()
                .filter(forecast -> visitTimeStamp <= forecast.getTimeStamp())
                .findFirst()
                .orElse(null);
    }
}
