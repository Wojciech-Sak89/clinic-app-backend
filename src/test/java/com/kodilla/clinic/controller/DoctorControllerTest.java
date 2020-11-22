package com.kodilla.clinic.controller;

import com.google.gson.Gson;
import com.kodilla.clinic.domain.Doctor;
import com.kodilla.clinic.dto.DoctorDto;
import com.kodilla.clinic.enums.Department;
import com.kodilla.clinic.enums.Specialization;
import com.kodilla.clinic.mapper.DoctorMapper;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService service;

    @MockBean
    private DoctorMapper mapper;

    @Test
    public void shouldGetEmptyDoctorList() throws Exception {
        //Given
        List<Doctor> doctorList = new ArrayList<>();

        when(service.getAllDoctors()).thenReturn(doctorList);
        when(mapper.mapToDoctorDtoList(doctorList)).thenReturn(new ArrayList<>());

        //When & Then
        mockMvc.perform(get("/v1/doctors").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldGetDoctorList() throws Exception {
        //Given
        List<Doctor> doctorList = new ArrayList<>();
        doctorList.add(new Doctor(15, "Name", "Surname", Specialization.DERMATOLOGY,
                Department.DERMATOLOGY, "mail@mail.com", null, "Bio", new ArrayList<>(), new ArrayList<>()));

        List<DoctorDto> doctorDtoList = new ArrayList<>();
        doctorDtoList.add(new DoctorDto(15, "Name", "Surname", Specialization.DERMATOLOGY,
                Department.DERMATOLOGY, "mail@mail.com", null, "Bio", new ArrayList<>(), new ArrayList<>()));

        when(service.getAllDoctors()).thenReturn(doctorList);
        when(mapper.mapToDoctorDtoList(doctorList)).thenReturn(doctorDtoList);

        //When & Then
        mockMvc.perform(get("/v1/doctors").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].doctor_id", is(15)))
                .andExpect(jsonPath("$[0].name", is("Name")))
                .andExpect(jsonPath("$[0].surname", is("Surname")))
                .andExpect(jsonPath("$[0].specialization", is("DERMATOLOGY")))
                .andExpect(jsonPath("$[0].department", is("DERMATOLOGY")))
                .andExpect(jsonPath("$[0].email", is("mail@mail.com")))
                .andExpect(jsonPath("$[0].clinicDoctorScheduleDto", is(nullValue())))
                .andExpect(jsonPath("$[0].bio", is("Bio")))
                .andExpect(jsonPath("$[0].appointmentsIds", hasSize(0)))
                .andExpect(jsonPath("$[0].evaluationsIds", hasSize(0)));
    }

    @Test
    public void shouldGetDoctor() throws Exception {
        //Given
        Doctor doctor = new Doctor(15, "Name", "Surname", Specialization.DERMATOLOGY,
                Department.DERMATOLOGY, "mail@mail.com", null, "Bio", new ArrayList<>(), new ArrayList<>());

        DoctorDto doctorDto = new DoctorDto(15, "Name", "Surname", Specialization.DERMATOLOGY,
                Department.DERMATOLOGY, "mail@mail.com", null, "Bio", new ArrayList<>(), new ArrayList<>());

        when(service.getDoctor(15)).thenReturn(java.util.Optional.of(doctor));
        when(mapper.mapToDoctorDto(doctor)).thenReturn(doctorDto);

        //When & Then
        mockMvc.perform(get("/v1/doctors/{doctorId}", 15).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctor_id", is(15)))
                .andExpect(jsonPath("$.name", is("Name")))
                .andExpect(jsonPath("$.surname", is("Surname")))
                .andExpect(jsonPath("$.specialization", is("DERMATOLOGY")))
                .andExpect(jsonPath("$.department", is("DERMATOLOGY")))
                .andExpect(jsonPath("$.email", is("mail@mail.com")))
                .andExpect(jsonPath("$.clinicDoctorScheduleDto", is(nullValue())))
                .andExpect(jsonPath("$.bio", is("Bio")))
                .andExpect(jsonPath("$.appointmentsIds", hasSize(0)))
                .andExpect(jsonPath("$.evaluationsIds", hasSize(0)));
    }


    @Test
    public void shouldCreateDoctor() throws Exception {
        //Given
        Doctor doctor = new Doctor(15, "Name", "Surname", Specialization.DERMATOLOGY,
                Department.DERMATOLOGY, "mail@mail.com", null, "Bio", new ArrayList<>(), new ArrayList<>());

        DoctorDto doctorDto = new DoctorDto(15, "Name", "Surname", Specialization.DERMATOLOGY,
                Department.DERMATOLOGY, "mail@mail.com", null, "Bio", new ArrayList<>(), new ArrayList<>());

        Gson gson = new Gson();
        String jsonContent = gson.toJson(doctorDto);

        when(mapper.mapToDoctor(doctorDto)).thenReturn(doctor);

        //When & Then
        mockMvc.perform(post("/v1/doctors").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateDoctor() throws Exception {
        //Given
        Doctor doctor = new Doctor(15, "Name", "Surname", Specialization.DERMATOLOGY,
                Department.DERMATOLOGY, "mail@mail.com", null, "Bio", new ArrayList<>(), new ArrayList<>());

        DoctorDto doctorDto = new DoctorDto(15, "Name", "Surname", Specialization.DERMATOLOGY,
                Department.DERMATOLOGY, "mail@mail.com", null, "Bio", new ArrayList<>(), new ArrayList<>());

        when(mapper.mapToDoctorDto(any())).thenReturn(doctorDto);
        when(service.saveDoctor(doctor)).thenReturn(doctor);
        when(mapper.mapToDoctor(doctorDto)).thenReturn(doctor);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(doctorDto);

        //When & Then
        mockMvc.perform(put("/v1/doctors").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctor_id", is(15)))
                .andExpect(jsonPath("$.name", is("Name")))
                .andExpect(jsonPath("$.surname", is("Surname")))
                .andExpect(jsonPath("$.specialization", is("DERMATOLOGY")))
                .andExpect(jsonPath("$.department", is("DERMATOLOGY")))
                .andExpect(jsonPath("$.email", is("mail@mail.com")))
                .andExpect(jsonPath("$.clinicDoctorScheduleDto", is(nullValue())))
                .andExpect(jsonPath("$.bio", is("Bio")))
                .andExpect(jsonPath("$.appointmentsIds", hasSize(0)))
                .andExpect(jsonPath("$.evaluationsIds", hasSize(0)));
    }

    @Test
    public void shouldDeleteDoctor() throws Exception {
        mockMvc.perform(delete("/v1/doctors/{doctorId}", 11).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}