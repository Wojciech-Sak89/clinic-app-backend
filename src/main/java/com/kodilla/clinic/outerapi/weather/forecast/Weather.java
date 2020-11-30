package com.kodilla.clinic.outerapi.weather.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
    @JsonProperty("main")
    private String mainAspect;

    @JsonProperty("description")
    private String description;

    @Override
    public String toString() {
        return "\nWeather{" +
                "mainAspect='" + mainAspect + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}