package com.kodilla.clinic.outerapi.weather.forecast.personalized;

import com.kodilla.clinic.outerapi.weather.forecast.WeatherData;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonalizedForecastTest {
    private final PersonalizedForecast personalizedForecast = PersonalizedForecast.getInstance();

    @Test
    public void getNearestForecast() {
        //Given
        LocalDateTime visitTime = LocalDateTime.of(2012, Month.DECEMBER, 18, 14, 30);

        LocalDateTime weatherForecastTime1 =
                LocalDateTime.of(2012, Month.DECEMBER, 18, 9, 30);
        LocalDateTime weatherForecastTime2 =
                LocalDateTime.of(2012, Month.DECEMBER, 18, 12, 30);
        LocalDateTime weatherForecastTime3 =
                LocalDateTime.of(2012, Month.DECEMBER, 18, 14, 0);
        LocalDateTime nearestForecastTime =
                LocalDateTime.of(2012, Month.DECEMBER, 18, 15, 0);
        LocalDateTime weatherForecastTime5 =
                LocalDateTime.of(2012, Month.DECEMBER, 18, 19, 30);

        List<WeatherData> weatherDataList = new ArrayList<>();
        Collections.addAll(weatherDataList,
                new WeatherData(weatherForecastTime1.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000),
                new WeatherData(weatherForecastTime2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000),
                new WeatherData(weatherForecastTime3.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000),
                new WeatherData(nearestForecastTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000),
                new WeatherData(weatherForecastTime5.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000));

        //When
        WeatherData personalizedData = personalizedForecast.findNearest(visitTime, weatherDataList);

        //Then
        assertEquals(nearestForecastTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000,
                personalizedData.getTimeStamp());
    }
}