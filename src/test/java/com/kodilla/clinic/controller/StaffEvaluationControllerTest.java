package com.kodilla.clinic.controller;

import com.google.gson.Gson;
import com.kodilla.clinic.domain.StaffEvaluation;
import com.kodilla.clinic.dto.StaffEvaluationDto;
import com.kodilla.clinic.enums.Stars;
import com.kodilla.clinic.mapper.StaffEvaluationMapper;
import com.kodilla.clinic.service.DbService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StaffEvaluationController.class)
public class StaffEvaluationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService service;

    @MockBean
    private StaffEvaluationMapper mapper;

    @Test
    public void shouldGetEmptyStaffEvaluationList() throws Exception {
        //Given
        List<StaffEvaluation> staffEvaluationList = new ArrayList<>();
        when(service.getAllStaffEvaluations()).thenReturn(staffEvaluationList);
        when(mapper.mapToStaffEvaluationDtoList(staffEvaluationList)).thenReturn(new ArrayList<>());

        //When & Then
        mockMvc.perform(get("/v1/staffEvaluations").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldGetStaffEvaluationList() throws Exception {
        //Given
        LocalDateTime localDateTime = LocalDateTime.now();

        List<StaffEvaluation> staffEvaluationList = new ArrayList<>();
        staffEvaluationList.add(new StaffEvaluation(15, Stars.FIVE, "Test opinion", localDateTime, null, null));

        List<StaffEvaluationDto> staffEvaluationDtoList = new ArrayList<>();
        staffEvaluationDtoList.add(new StaffEvaluationDto(15, Stars.FIVE, "Test opinion", localDateTime, null, null));

        when(service.getAllStaffEvaluations()).thenReturn(staffEvaluationList);
        when(mapper.mapToStaffEvaluationDtoList(staffEvaluationList)).thenReturn(staffEvaluationDtoList);

        //When & Then
        mockMvc.perform(get("/v1/staffEvaluations").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].evaluation_id", is(15)))
                .andExpect(jsonPath("$[0].stars", is("FIVE")))
                .andExpect(jsonPath("$[0].opinion", is("Test opinion")))
                .andExpect(jsonPath("$[0].entryDate", containsString(
                        localDateTime.toString().substring(0, localDateTime.toString().length()-1))))
                .andExpect(jsonPath("$[0].patient_Id", is(nullValue())))
                .andExpect(jsonPath("$[0].doctor_Id", is(nullValue())));
    }

    @Test
    public void shouldGetStaffEvaluation() throws Exception {
        //Given
        LocalDateTime localDateTime = LocalDateTime.now();

        StaffEvaluation staffEvaluation = new StaffEvaluation(
                15, Stars.FIVE, "Test opinion", localDateTime, null, null);
        StaffEvaluationDto staffEvaluationDto = new StaffEvaluationDto(
                15, Stars.FIVE, "Test opinion", localDateTime, null, null);

        when(service.getStaffEvaluation(15)).thenReturn(java.util.Optional.of(staffEvaluation));
        when(mapper.mapToStuffEvaluationDto(staffEvaluation)).thenReturn(staffEvaluationDto);

        //When & Then
        mockMvc.perform(get("/v1/staffEvaluations/{evaluationId}", 15).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.evaluation_id", is(15)))
                .andExpect(jsonPath("$.stars", is("FIVE")))
                .andExpect(jsonPath("$.opinion", is("Test opinion")))
                .andExpect(jsonPath("$.entryDate", containsString(
                        localDateTime.toString().substring(0, localDateTime.toString().length()-1))))
                .andExpect(jsonPath("$.patient_Id", is(nullValue())))
                .andExpect(jsonPath("$.doctor_Id", is(nullValue())));
    }


    @Test
    public void shouldCreateStaffEvaluation() throws Exception {
        //Given
        LocalDateTime localDateTime = LocalDateTime.now();

        StaffEvaluation staffEvaluation = new StaffEvaluation(
                15, Stars.FIVE, "Test opinion", localDateTime, null, null);
        StaffEvaluationDto staffEvaluationDto = new StaffEvaluationDto(
                15, Stars.FIVE, "Test opinion", null, null);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(staffEvaluationDto);

        when(mapper.mapToStuffEvaluation(staffEvaluationDto)).thenReturn(staffEvaluation);

        //When & Then
        mockMvc.perform(post("/v1/staffEvaluations").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateStaffEvaluation() throws Exception {
        //Given
        LocalDateTime localDateTime = LocalDateTime.now();

        StaffEvaluation staffEvaluation = new StaffEvaluation(
                15, Stars.FIVE, "Test opinion", localDateTime, null, null);
        StaffEvaluationDto staffEvaluationDto = new StaffEvaluationDto(
                15, Stars.FIVE, "Test opinion", null, null);

        when(mapper.mapToStuffEvaluationDto(any())).thenReturn(staffEvaluationDto);
        when(service.saveStaffEvaluation(staffEvaluation)).thenReturn(staffEvaluation);
        when(mapper.mapToStuffEvaluation(staffEvaluationDto)).thenReturn(staffEvaluation);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(staffEvaluationDto);

        //When & Then
        mockMvc.perform(put("/v1/staffEvaluations").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.evaluation_id", is(15)))
                .andExpect(jsonPath("$.stars", is("FIVE")))
                .andExpect(jsonPath("$.opinion", is("Test opinion")))
                .andExpect(jsonPath("$.patient_Id", is(nullValue())))
                .andExpect(jsonPath("$.doctor_Id", is(nullValue())));
    }

    @Test
    public void shouldDeleteStaffEvaluation() throws Exception {
        mockMvc.perform(delete("/v1/staffEvaluations/{evaluationId}", 11).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}