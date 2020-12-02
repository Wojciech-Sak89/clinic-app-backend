package com.kodilla.clinic.outerapi.medic.client;

import com.kodilla.clinic.enums.Gender;
import com.kodilla.clinic.outerapi.medic.config.MedicConfig;
import com.kodilla.clinic.outerapi.medic.recommendation.SpecialisationDto;
import com.kodilla.clinic.outerapi.medic.symptom.SymptomDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Component
public class MedicClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MedicClient.class);

    @Autowired
    private MedicConfig medicConfig;

    @Autowired
    private RestTemplate restTemplate;

    public List<SymptomDto> getSymptoms() throws Exception {
        URI url = getSymptomsUri();
        System.out.println(url);

        try {
            Optional<SymptomDto[]> symptomsResponse = Optional.ofNullable(restTemplate.getForObject(url, SymptomDto[].class));
            System.out.println(url);
            return Arrays.asList(symptomsResponse.orElse(new SymptomDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<SpecialisationDto> getSpecialisations(int birthYear, Gender gender, List<Integer> symptomsIds) throws Exception {
        URI url = getSpecialisationsUri(birthYear, gender, symptomsIds);
        System.out.println("#####################\n URL in MedicClient.getSpecialisations() before try" + url
                + "#####################\n)");

        try {
            Optional<SpecialisationDto[]> specialisationsResponse = Optional.ofNullable(restTemplate.getForObject(url, SpecialisationDto[].class));
            System.out.println("#####################\nURL MedicClient.getSpecialisations() after try{  " +
                    "Optional<SpecialisationDto[]> specialisationsResponse = Optional.ofNullable(restTemplate.getForObject(url, SpecialisationDto[].class)); }: " + url
                    + "#####################\n");
            return Arrays.asList(specialisationsResponse.orElse(new SpecialisationDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private URI getSymptomsUri() throws Exception {
        return UriComponentsBuilder.fromHttpUrl(medicConfig.getMedicApiEndpoint() + medicConfig.getSymptoms())
                .queryParam("token", medicConfig.getToken())
                .queryParam("format", medicConfig.getFormat())
                .queryParam("language", medicConfig.getLanguage())
                .build().encode().toUri();
    }

    private URI getSpecialisationsUri(int birthYear, Gender gender, List<Integer> symptomsIds) throws Exception {
        return UriComponentsBuilder.fromHttpUrl(medicConfig.getMedicApiEndpoint() + medicConfig.getSpecialisations())
                .queryParam("token", medicConfig.getToken())
                .queryParam("format", medicConfig.getFormat())
                .queryParam("language", medicConfig.getLanguage())
                .queryParam("year_of_birth", birthYear)
                .queryParam("gender", gender)
                .query("symptoms=" + symptomsIds)
                .build().encode().toUri();
    }

//    @GetMapping("/get")
//    public Resource get(){
//        String url = "http://localhost:8085/post";
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//                .queryParam("path", "home");
//
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//
//        HttpEntity<Map<String, String>> request = new HttpEntity<>(Collections.singletonMap("key", "valid"), httpHeaders);
//
//        Resource resource = restTemplate.postForObject(builder.toUriString(), request, Resource.class);
//
//        return resource;
//    }

    //header

}
