package com.kodilla.clinic.outerapi.medic.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class TokenRetriever {
    private AccessToken token;
    private CloseableHttpClient httpclient;

    public AccessToken retrieveToken(String username, String password, String url) throws Exception {

        httpclient = HttpClients.createDefault();

        SecretKeySpec keySpec = new SecretKeySpec(
                password.getBytes(),
                "HmacMD5");

        String computedHashString = "";
        try {
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(keySpec);
            byte[] result = mac.doFinal(url.getBytes());


            //            BASE64Encoder encoder = new BASE64Encoder();
            computedHashString = new String(Base64.getEncoder().encode(result));

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("Can not create token (NoSuchAlgorithmException)");
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("Can not create token (InvalidKeyException)");
        }

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", "Bearer " + username + ":" + computedHashString);

        try {
            CloseableHttpResponse response = httpclient.execute(httpPost);

            ObjectMapper objectMapper = new ObjectMapper();
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
                RetrieveException(response, objectMapper);
            }
            token = objectMapper.readValue(response.getEntity().getContent(), AccessToken.class);
        }
        catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("Can not create token (ClientProtocolException)");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("Can not create token (IOException)");
        }

        return token;
    }


    private void RetrieveException(CloseableHttpResponse response, ObjectMapper objectMapper) throws Exception {

        String errorMessage = objectMapper.readValue(response.getEntity().getContent(), String.class);
        System.out.println("Resposne with status code: " + response.getStatusLine().getStatusCode() + ", error message: " + errorMessage);
        throw new Exception(errorMessage);
    }
}