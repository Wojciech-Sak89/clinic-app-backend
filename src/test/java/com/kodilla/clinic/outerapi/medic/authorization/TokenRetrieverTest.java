package com.kodilla.clinic.outerapi.medic.authorization;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenRetrieverTest {

    @Test
    public void retrieveToken() throws Exception {
        //Given
        String username = "wojciech.r.sak@gmail.com";
        String password = "f8SQg76JjLd4k9X3H";
        String urlToken = "https://sandbox-authservice.priaid.ch/login";

        TokenRetriever tokenRetriever = new TokenRetriever();

        //When
        AccessToken accessToken = tokenRetriever.retrieveToken(username, password, urlToken);

        //Then
        assertEquals(7200, accessToken.getValidThrough());
        assertNotNull(accessToken.getToken());
        assertNotEquals("", accessToken.getToken());

        System.out.println(accessToken.getToken());
    }
}