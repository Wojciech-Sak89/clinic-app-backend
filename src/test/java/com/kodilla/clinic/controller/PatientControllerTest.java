package com.kodilla.clinic.controller;

import com.google.gson.Gson;
import com.kodilla.clinic.domain.Appointment;
import com.kodilla.clinic.domain.Patient;
import com.kodilla.clinic.domain.StaffEvaluation;
import com.kodilla.clinic.dto.PatientDto;
import com.kodilla.clinic.dto.StaffEvaluationDto;
import com.kodilla.clinic.enums.Stars;
import com.kodilla.clinic.mapper.PatientMapper;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PatientController.class)
public class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService service;

    @MockBean
    private PatientMapper mapper;

    @Test
    public void shouldGetEmptyPatientList() throws Exception {
        //Given
        List<Patient> patientList = new ArrayList<>();

        when(service.getAllPatients()).thenReturn(patientList);
        when(mapper.mapToPatientDtoList(patientList)).thenReturn(new ArrayList<>());

        //When & Then
        mockMvc.perform(get("/v1/patients").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldGetPatientList() throws Exception {
        //Given
        LocalDate localDate = LocalDate.now();
        List<Patient> patientList = new ArrayList<>();
        patientList.add(new Patient(15, "Name", "Surname", "address",
                localDate, new BigDecimal(1000), new BigDecimal(2222), "mail@mail.com", true, new ArrayList<>(), new ArrayList<>()));

        List<PatientDto> patientDtoList = new ArrayList<>();
        patientDtoList.add(new PatientDto(15, "Name", "Surname", "address",
                localDate, new BigDecimal(1000), new BigDecimal(2222), "mail@mail.com", true, new ArrayList<>(), new ArrayList<>()));

        when(service.getAllPatients()).thenReturn(patientList);
        when(mapper.mapToPatientDtoList(patientList)).thenReturn(patientDtoList);

        //When & Then
        mockMvc.perform(get("/v1/patients").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].patient_id", is(15)))
                .andExpect(jsonPath("$[0].name", is("Name")))
                .andExpect(jsonPath("$[0].surname", is("Surname")))
                .andExpect(jsonPath("$[0].address", is("address")))
                .andExpect(jsonPath("$[0].birthDate", containsString(localDate.toString())))
                .andExpect(jsonPath("$[0].pesel", is(1000)))
                .andExpect(jsonPath("$[0].telNum", is(2222)))
                .andExpect(jsonPath("$[0].email", is("mail@mail.com")))
                .andExpect(jsonPath("$[0].inUrgency", is(true)))
                .andExpect(jsonPath("$[0].appointmentsIds", hasSize(0)))
                .andExpect(jsonPath("$[0].evaluationsIds", hasSize(0)));
    }

    @Test
    public void shouldGetPatient() throws Exception {
        //Given
        LocalDate localDate = LocalDate.now();
        Patient patient = new Patient(15, "Name", "Surname", "address",
                localDate, new BigDecimal(1000), new BigDecimal(2222), "mail@mail.com", true, new ArrayList<>(), new ArrayList<>());

        PatientDto patientDto = new PatientDto(15, "Name", "Surname", "address",
                localDate, new BigDecimal(1000), new BigDecimal(2222), "mail@mail.com", true, new ArrayList<>(), new ArrayList<>());

        when(service.getPatient(15)).thenReturn(java.util.Optional.of(patient));
        when(mapper.mapToPatientDto(patient)).thenReturn(patientDto);

        //When & Then
        mockMvc.perform(get("/v1/patients/{patientId}", 15).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patient_id", is(15)))
                .andExpect(jsonPath("$.name", is("Name")))
                .andExpect(jsonPath("$.surname", is("Surname")))
                .andExpect(jsonPath("$.address", is("address")))
                .andExpect(jsonPath("$.birthDate", containsString(localDate.toString())))
                .andExpect(jsonPath("$.pesel", is(1000)))
                .andExpect(jsonPath("$.telNum", is(2222)))
                .andExpect(jsonPath("$.email", is("mail@mail.com")))
                .andExpect(jsonPath("$.inUrgency", is(true)))
                .andExpect(jsonPath("$.appointmentsIds", hasSize(0)))
                .andExpect(jsonPath("$.evaluationsIds", hasSize(0)));
    }


    @Test
    public void shouldCreatePatient() throws Exception {
        //Given
        LocalDate localDate = LocalDate.now();
        Patient patient = new Patient(15, "Name", "Surname", "address",
                localDate, new BigDecimal(1000), new BigDecimal(2222), "mail@mail.com", true, new ArrayList<>(), new ArrayList<>());

        PatientDto patientDto = new PatientDto(15, "Name", "Surname", "address",
                new BigDecimal(1000), new BigDecimal(2222), "mail@mail.com", true, new ArrayList<>(), new ArrayList<>());

        Gson gson = new Gson();
        String jsonContent = gson.toJson(patientDto);

        when(mapper.mapToPatient(patientDto)).thenReturn(patient);

        //When & Then
        mockMvc.perform(post("/v1/patients").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdatePatient() throws Exception {
        //Given
        LocalDate localDate = LocalDate.now();
        Patient patient = new Patient(15, "Name", "Surname", "address",
                localDate, new BigDecimal(1000), new BigDecimal(2222), "mail@mail.com", true, new ArrayList<>(), new ArrayList<>());

        PatientDto patientDto = new PatientDto(15, "Name", "Surname", "address",
                new BigDecimal(1000), new BigDecimal(2222), "mail@mail.com", true, new ArrayList<>(), new ArrayList<>());

        when(mapper.mapToPatientDto(any())).thenReturn(patientDto);
        when(service.savePatient(patient)).thenReturn(patient);
        when(mapper.mapToPatient(patientDto)).thenReturn(patient);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(patientDto);

        //When & Then
        mockMvc.perform(put("/v1/patients").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patient_id", is(15)))
                .andExpect(jsonPath("$.name", is("Name")))
                .andExpect(jsonPath("$.surname", is("Surname")))
                .andExpect(jsonPath("$.address", is("address")))
                .andExpect(jsonPath("$.pesel", is(1000)))
                .andExpect(jsonPath("$.telNum", is(2222)))
                .andExpect(jsonPath("$.email", is("mail@mail.com")))
                .andExpect(jsonPath("$.inUrgency", is(true)))
                .andExpect(jsonPath("$.appointmentsIds", hasSize(0)))
                .andExpect(jsonPath("$.evaluationsIds", hasSize(0)));
    }

    @Test
    public void shouldDeletePatient() throws Exception {
        mockMvc.perform(delete("/v1/patients/{patientId}", 11).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}