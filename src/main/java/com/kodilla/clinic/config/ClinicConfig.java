package com.kodilla.clinic.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ClinicConfig {
    @Value("${info.clinic.location.city}")
    private String clinicLocation;
}
