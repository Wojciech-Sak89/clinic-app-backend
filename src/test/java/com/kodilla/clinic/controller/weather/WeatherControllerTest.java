package com.kodilla.clinic.controller.weather;

import com.kodilla.clinic.outerapi.weather.forecast.*;
import com.kodilla.clinic.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    public void shouldGetForecast() throws Exception {
        //Given
        Weather weather = new Weather("Clear", "clear sky");
        WeatherData weatherData = new WeatherData(
                1607169600, new Temperature(20), Collections.singletonList(weather), new Cloudiness(80));
        ForecastDto forecastDto = new ForecastDto(1, new City("Warsaw", "PL"), Collections.singletonList(weatherData));

        when(weatherService.getCurrentForecast()).thenReturn(forecastDto);

        //When & Then
        mockMvc.perform(get("/v1/weather/forecast").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cnt", is(1)))
                .andExpect(jsonPath("$.city.name", is("Warsaw")))
                .andExpect(jsonPath("$.city.country", is("PL")))
                .andExpect(jsonPath("$.list[0].dt", is(1607169600)))
                .andExpect(jsonPath("$.list[0].main.temp", is(20)))
                .andExpect(jsonPath("$.list[0].clouds.all", is(80)))
                .andExpect(jsonPath("$.list[0].weather[0].main", is("Clear")))
                .andExpect(jsonPath("$.list[0].weather[0].description", is("clear sky")));
    }
}