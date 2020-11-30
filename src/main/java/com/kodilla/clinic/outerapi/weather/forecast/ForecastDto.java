package com.kodilla.clinic.outerapi.weather.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastDto {
    @JsonProperty("cnt")
    private int cnt;

    @JsonProperty("city")
    private City city;

    @JsonProperty("list")
    private List<WeatherData> weatherDataList;

    @Override
    public String toString() {
        return "ForecastDto{" +
                "cnt=" + cnt +
                ", city=" + city +
                ", weatherDataList=" + weatherDataList +
                '}';
    }
}
