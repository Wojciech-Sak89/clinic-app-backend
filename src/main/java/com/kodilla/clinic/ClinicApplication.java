package com.kodilla.clinic;

import com.kodilla.clinic.outerapi.medic.authorization.TokenRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClinicApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ClinicApplication.class, args);

        TokenRetriever tokenRetriever = new TokenRetriever();
        System.out.println("Access token for ApiMedic (for Postman testing purposes): " + tokenRetriever.retrieveToken("wojciech.r.sak@gmail.com", "f8SQg76JjLd4k9X3H", "https://sandbox-authservice.priaid.ch/login").getToken());
        System.out.println("Token is valid for " + tokenRetriever.retrieveToken("wojciech.r.sak@gmail.com", "f8SQg76JjLd4k9X3H", "https://sandbox-authservice.priaid.ch/login").getValidThrough()/60 + " minutes.");
    }

}
