package com.kodilla.clinic.outerapi.medic.recommendation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpecialisationDto {
    @JsonProperty("Name")
    private String specialisation;

    @JsonProperty("Accuracy")
    private String matchAccuracy;
}