package com.kodilla.clinic.outerapi.weather.client;

import com.kodilla.clinic.config.ClinicConfig;
import com.kodilla.clinic.outerapi.weather.config.WeatherConfig;
import com.kodilla.clinic.outerapi.weather.forecast.ForecastDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Component
public class WeatherClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherClient.class);

    @Autowired
    private ClinicConfig clinicConfig;

    @Autowired
    private WeatherConfig weatherConfig;

    @Autowired
    private RestTemplate restTemplate;

    public ForecastDto getForecast() {
        URI url = getUri();

        try {
            Optional<ForecastDto> weatherResponse = Optional.ofNullable(restTemplate.getForObject(url, ForecastDto.class));
            System.out.println(url);
            return weatherResponse.orElse(new ForecastDto());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ForecastDto();
        }
    }

    private URI getUri() {
        return UriComponentsBuilder.fromHttpUrl(weatherConfig.getOpenWeatherApiEndpoint() + "/" + weatherConfig.getForecast())
                .queryParam("APPID", weatherConfig.getOpenWeatherAppKey())
                .queryParam("units", weatherConfig.getUnits())
                .queryParam("cnt", weatherConfig.getForecastExtent())
                .queryParam("q", clinicConfig.getClinicLocation())
                .build().encode().toUri();
    }
}
