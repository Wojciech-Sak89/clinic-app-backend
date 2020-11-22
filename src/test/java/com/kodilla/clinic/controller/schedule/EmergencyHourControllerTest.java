package com.kodilla.clinic.controller.schedule;

import com.google.gson.Gson;
import com.kodilla.clinic.domain.schedule.EmergencyHour;
import com.kodilla.clinic.dto.schedule.EmergencyHourDto;
import com.kodilla.clinic.enums.Day;
import com.kodilla.clinic.enums.Hour;
import com.kodilla.clinic.mapper.schedule.EmergencyHourMapper;
import com.kodilla.clinic.service.DbService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EmergencyHourController.class)
public class EmergencyHourControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService service;

    @MockBean
    private EmergencyHourMapper mapper;

    @Test
    public void shouldGetEmptyEmergencyHoursList() throws Exception {
        //Given
        List<EmergencyHour> emergencyHours = new ArrayList<>();
        when(service.getAllEmergencyHours()).thenReturn(emergencyHours);
        when(mapper.mapToEmergencyHourDtos(emergencyHours)).thenReturn(new ArrayList<>());

        //When & Then
        mockMvc.perform(get("/v1/emergencyHours").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldGetEmergencyHoursList() throws Exception {
        //Given
        List<EmergencyHour> emergencyHours = new ArrayList<>();
        emergencyHours.add(new EmergencyHour(15, Day.FRIDAY, Hour.EIGHT_AM, new ArrayList<>()));

        List<EmergencyHourDto> emergencyHourDtos = new ArrayList<>();
        emergencyHourDtos.add(new EmergencyHourDto(15, Day.FRIDAY, Hour.EIGHT_AM, new ArrayList<>()));

        when(service.getAllEmergencyHours()).thenReturn(emergencyHours);
        when(mapper.mapToEmergencyHourDtos(emergencyHours)).thenReturn(emergencyHourDtos);

        //When & Then
        mockMvc.perform(get("/v1/emergencyHours").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].emergencyHour_id", is(15)))
                .andExpect(jsonPath("$[0].day", is("FRIDAY")))
                .andExpect(jsonPath("$[0].hour", is("EIGHT_AM")))
                .andExpect(jsonPath("$[0].schedulesIds", hasSize(0)));
    }

    @Test
    public void shouldGetEmergencyHour() throws Exception {
        //Given
        EmergencyHour emergencyHour =
                new EmergencyHour(15, Day.FRIDAY, Hour.EIGHT_AM, new ArrayList<>());
        EmergencyHourDto emergencyHourDto =
                new EmergencyHourDto(15, Day.FRIDAY, Hour.EIGHT_AM, new ArrayList<>());

        when(service.getEmergencyHour(15)).thenReturn(java.util.Optional.of(emergencyHour));
        when(mapper.mapToEmergencyHourDto(emergencyHour)).thenReturn(emergencyHourDto);

        //When & Then
        mockMvc.perform(get("/v1/emergencyHours/{hourId}", 15).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emergencyHour_id", is(15)))
                .andExpect(jsonPath("$.day", is("FRIDAY")))
                .andExpect(jsonPath("$.hour", is("EIGHT_AM")))
                .andExpect(jsonPath("$.schedulesIds", hasSize(0)));
    }


    @Test
    public void shouldCreateEmergencyHour() throws Exception {
        //Given
        EmergencyHour emergencyHour =
                new EmergencyHour(15, Day.FRIDAY, Hour.EIGHT_AM, new ArrayList<>());
        EmergencyHourDto emergencyHourDto =
                new EmergencyHourDto(15, Day.FRIDAY, Hour.EIGHT_AM, new ArrayList<>());

        Gson gson = new Gson();
        String jsonContent = gson.toJson(emergencyHourDto);

        when(mapper.mapToEmergencyHour(emergencyHourDto)).thenReturn(emergencyHour);

        //When & Then
        mockMvc.perform(post("/v1/emergencyHours").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateEmergencyHour() throws Exception {
        //Given
        EmergencyHour emergencyHour =
                new EmergencyHour(15, Day.FRIDAY, Hour.EIGHT_AM, new ArrayList<>());
        EmergencyHourDto emergencyHourDto =
                new EmergencyHourDto(15, Day.FRIDAY, Hour.EIGHT_AM, new ArrayList<>());

        when(mapper.mapToEmergencyHourDto(any())).thenReturn(emergencyHourDto);
        when(service.saveEmergencyHour(emergencyHour)).thenReturn(emergencyHour);
        when(mapper.mapToEmergencyHour(emergencyHourDto)).thenReturn(emergencyHour);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(emergencyHourDto);

        //When & Then
        mockMvc.perform(put("/v1/emergencyHours").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emergencyHour_id", is(15)))
                .andExpect(jsonPath("$.day", is("FRIDAY")))
                .andExpect(jsonPath("$.hour", is("EIGHT_AM")))
                .andExpect(jsonPath("$.schedulesIds", hasSize(0)));
    }

    @Test
    public void shouldDeleteEmergencyHour() throws Exception {
        mockMvc.perform(delete("/v1/emergencyHours/{hourId}", 11).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}