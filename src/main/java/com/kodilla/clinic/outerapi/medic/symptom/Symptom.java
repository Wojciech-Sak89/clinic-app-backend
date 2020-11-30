package com.kodilla.clinic.outerapi.medic.symptom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Symptom {
    @JsonProperty("ID")
    private int id;

    @JsonProperty("Name")
    private String name;

    @Override
    public String toString() {
        return "Symptom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}