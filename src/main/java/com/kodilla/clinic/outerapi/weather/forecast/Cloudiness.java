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
public class Cloudiness {
    @JsonProperty("all")
    private int percentage;

    @Override
    public String toString() {
        return "Cloudiness{" +
                "percentage=" + percentage +
                '}';
    }
}