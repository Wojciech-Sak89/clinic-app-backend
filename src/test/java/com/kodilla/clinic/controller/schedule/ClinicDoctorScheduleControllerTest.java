package com.kodilla.clinic.controller.schedule;

import com.google.gson.Gson;
import com.kodilla.clinic.domain.schedule.ClinicDoctorSchedule;
import com.kodilla.clinic.dto.schedule.ClinicDoctorScheduleDto;
import com.kodilla.clinic.mapper.schedule.ClinicDoctorScheduleMapper;
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
@WebMvcTest(ClinicDoctorScheduleController.class)
public class ClinicDoctorScheduleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService service;

    @MockBean
    private ClinicDoctorScheduleMapper mapper;

    @Test
    public void shouldGetEmptyClinicDoctorScheduleList() throws Exception {
        //Given
        List<ClinicDoctorSchedule> clinicDoctorSchedules = new ArrayList<>();
        when(service.getAllClinicDoctorSchedules()).thenReturn(clinicDoctorSchedules);
        when(mapper.mapToClinicDoctorScheduleDtos(clinicDoctorSchedules)).thenReturn(new ArrayList<>());

        //When & Then
        mockMvc.perform(get("/v1/clinicDoctorsSchedules").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldGetClinicDoctorScheduleList() throws Exception {
        //Given
        List<ClinicDoctorSchedule> clinicDoctorSchedules = new ArrayList<>();
        clinicDoctorSchedules.add(new ClinicDoctorSchedule(15,  new ArrayList<>(), new ArrayList<>()));

        List<ClinicDoctorScheduleDto> clinicDoctorScheduleDtos = new ArrayList<>();
        clinicDoctorScheduleDtos.add(new ClinicDoctorScheduleDto(15,  new ArrayList<>(), new ArrayList<>()));

        when(service.getAllClinicDoctorSchedules()).thenReturn(clinicDoctorSchedules);
        when(mapper.mapToClinicDoctorScheduleDtos(clinicDoctorSchedules)).thenReturn(clinicDoctorScheduleDtos);

        //When & Then
        mockMvc.perform(get("/v1/clinicDoctorsSchedules").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].schedule_id", is(15)))
                .andExpect(jsonPath("$[0].workingDaysDtos", hasSize(0)))
                .andExpect(jsonPath("$[0].emergencyHoursDtos", hasSize(0)));
    }

    @Test
    public void shouldGetClinicDoctorSchedule() throws Exception {
        //Given
        ClinicDoctorSchedule clinicDoctorSchedule =
                new ClinicDoctorSchedule(15,  new ArrayList<>(), new ArrayList<>());
        ClinicDoctorScheduleDto clinicDoctorScheduleDto =
                new ClinicDoctorScheduleDto(15,  new ArrayList<>(), new ArrayList<>());

        when(service.getClinicDoctorSchedule(15)).thenReturn(java.util.Optional.of(clinicDoctorSchedule));
        when(mapper.mapToClinicDoctorScheduleDto(clinicDoctorSchedule)).thenReturn(clinicDoctorScheduleDto);

        //When & Then
        mockMvc.perform(get("/v1/clinicDoctorsSchedules/{scheduleId}", 15).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schedule_id", is(15)))
                .andExpect(jsonPath("$.workingDaysDtos", hasSize(0)))
                .andExpect(jsonPath("$.emergencyHoursDtos", hasSize(0)));
    }

    @Test
    public void shouldCreateClinicDoctorSchedule() throws Exception {
        //Given
        ClinicDoctorSchedule clinicDoctorSchedule =
                new ClinicDoctorSchedule(15,  new ArrayList<>(), new ArrayList<>());
        ClinicDoctorScheduleDto clinicDoctorScheduleDto =
                new ClinicDoctorScheduleDto(15,  new ArrayList<>(), new ArrayList<>());

        Gson gson = new Gson();
        String jsonContent = gson.toJson(clinicDoctorScheduleDto);

        when(mapper.mapToClinicDoctorSchedule(clinicDoctorScheduleDto)).thenReturn(clinicDoctorSchedule);

        //When & Then
        mockMvc.perform(post("/v1/clinicDoctorsSchedules").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateClinicDoctorSchedule() throws Exception {
        //Given
        ClinicDoctorSchedule clinicDoctorSchedule =
                new ClinicDoctorSchedule(15,  new ArrayList<>(), new ArrayList<>());
        ClinicDoctorScheduleDto clinicDoctorScheduleDto =
                new ClinicDoctorScheduleDto(15,  new ArrayList<>(), new ArrayList<>());


        when(mapper.mapToClinicDoctorScheduleDto(any())).thenReturn(clinicDoctorScheduleDto);
        when(service.saveClinicDoctorSchedule(clinicDoctorSchedule)).thenReturn(clinicDoctorSchedule);
        when(mapper.mapToClinicDoctorSchedule(clinicDoctorScheduleDto)).thenReturn(clinicDoctorSchedule);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(clinicDoctorScheduleDto);

        //When & Then
        mockMvc.perform(put("/v1/clinicDoctorsSchedules").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schedule_id", is(15)))
                .andExpect(jsonPath("$.workingDaysDtos", hasSize(0)))
                .andExpect(jsonPath("$.emergencyHoursDtos", hasSize(0)));
    }

    @Test
    public void shouldDeleteClinicDoctorSchedule() throws Exception {
        mockMvc.perform(delete("/v1/clinicDoctorsSchedules/{scheduleId}", 11).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}