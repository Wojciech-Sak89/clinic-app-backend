package com.kodilla.clinic.outerapi.medic.config;


import com.kodilla.clinic.outerapi.medic.authorization.TokenRetriever;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MedicConfig {
    @Value("${medic.api.endpoint.prod}")
    private String medicApiEndpoint;

    @Value("${medic.app.symptoms}")
    private String symptoms;

    @Value("${medic.app.diagnosis.specialisations}")
    private String specialisations;

    @Value("json")
    private String format;

    @Value("en-gb")
    private String language;

    @Value("${medic.app.username}")
    private String userName;

    @Value("${medic.app.password}")
    private String password;

    @Value("${medic.app.authServiceUrl}")
    private String authServiceUrl;

    @Autowired
    private TokenRetriever tokenRetriever;

    public String getToken() throws Exception {
        return tokenRetriever.retrieveToken(userName, password, authServiceUrl).getToken();
    }
}