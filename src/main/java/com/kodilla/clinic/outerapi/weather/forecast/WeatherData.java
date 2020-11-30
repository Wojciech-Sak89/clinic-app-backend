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
public class WeatherData {
    @JsonProperty("dt")
    private long timeStamp;

    @JsonProperty("main")
    private Temperature temperature;

    @JsonProperty("weather")
    private List<Weather> weather;

    @JsonProperty("clouds")
    private Cloudiness cloudiness;

    public WeatherData(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "\nWeatherData{" +
                "timeStamp=" + timeStamp +
                ", temperature=" + temperature +
                ", weather=" + weather +
                ", cloudiness=" + cloudiness +
                '}';
    }
}