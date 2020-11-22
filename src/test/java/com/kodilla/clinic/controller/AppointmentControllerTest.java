package com.kodilla.clinic.controller;

import com.google.gson.Gson;
import com.kodilla.clinic.dao.DoctorDao;
import com.kodilla.clinic.dao.PatientDao;
import com.kodilla.clinic.domain.Appointment;
import com.kodilla.clinic.domain.Doctor;
import com.kodilla.clinic.domain.Patient;
import com.kodilla.clinic.dto.AppointmentDto;
import com.kodilla.clinic.enums.Status;
import com.kodilla.clinic.mapper.AppointmentMapper;
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
@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService service;

    @MockBean
    private AppointmentMapper mapper;

    @MockBean
    private DoctorDao doctorDao;

    @MockBean
    private PatientDao patientDao;

    @Test
    public void shouldGetEmptyAppointmentList() throws Exception {
        //Given
        List<Appointment> appointments = new ArrayList<>();
        when(service.getAllAppointments()).thenReturn(appointments);
        when(mapper.mapToAppointmentDtoList(appointments)).thenReturn(new ArrayList<>());

        //When & Then
        mockMvc.perform(get("/v1/appointments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldGetAppointmentList() throws Exception {
        //Given
        LocalDateTime localDateTime = LocalDateTime.now();

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment(15,  true, localDateTime, Status.RESERVED, null, null));

        List<AppointmentDto> appointmentDtoList = new ArrayList<>();
        appointmentDtoList.add(new AppointmentDto(15,  true, localDateTime, Status.RESERVED, null, null));

        when(service.getAllAppointments()).thenReturn(appointments);
        when(mapper.mapToAppointmentDtoList(appointments)).thenReturn(appointmentDtoList);

        //When & Then
        mockMvc.perform(get("/v1/appointments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].appointment_id", is(15)))
                .andExpect(jsonPath("$[0].forEmergency", is(true)))
                .andExpect(jsonPath("$[0].dateTime", containsString(localDateTime.toString().substring(0, localDateTime.toString().length()-1))))
                .andExpect(jsonPath("$[0].doctorId", is(nullValue())))
                .andExpect(jsonPath("$[0].patientId", is(nullValue())));
    }

    @Test
    public void shouldGetAppointment() throws Exception {
        //Given
        LocalDateTime localDateTime = LocalDateTime.now();

        Appointment appointment =  new Appointment(15,  true, localDateTime, Status.RESERVED, null, null);

        AppointmentDto appointmentDto =  new AppointmentDto(15,  true, localDateTime, Status.RESERVED, null, null);

        when(service.getAppointment(15)).thenReturn(java.util.Optional.of(appointment));
        when(mapper.mapToAppointmentDto(appointment)).thenReturn(appointmentDto);

        //When & Then
        mockMvc.perform(get("/v1/appointments/{appointmentId}", 15).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointment_id", is(15)))
                .andExpect(jsonPath("$.forEmergency", is(true)))
                .andExpect(jsonPath("$.dateTime", containsString(localDateTime.toString())))
                .andExpect(jsonPath("$.doctorId", is(nullValue())))
                .andExpect(jsonPath("$.patientId", is(nullValue())));
    }

    @Test
    public void shouldCreateAppointment() throws Exception {
        //Given
        LocalDateTime localDateTime = LocalDateTime.now();
        Doctor doctor = new Doctor();
        Patient patient = new Patient();

        Appointment appointment =  new Appointment(15,  true, localDateTime, Status.RESERVED, doctor, patient);
        AppointmentDto appointmentDto =  new AppointmentDto(15,  true, Status.RESERVED, 1, 1);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(appointmentDto);

        when(doctorDao.findById(any())).thenReturn(java.util.Optional.of(doctor));
        when(patientDao.findById(any())).thenReturn(java.util.Optional.of(patient));
        when(mapper.mapToAppointment(any())).thenReturn(appointment);

        //When & Then
        mockMvc.perform(post("/v1/appointments").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateAppointment() throws Exception {
        //Given
        LocalDateTime localDateTime = LocalDateTime.now();

        Appointment appointment =  new Appointment(15,  true, localDateTime, Status.RESERVED, null, null);
        AppointmentDto appointmentDto =  new AppointmentDto(15,  true, Status.RESERVED, null, null);


        when(mapper.mapToAppointmentDto(any())).thenReturn(appointmentDto);
        when(service.saveAppointment(appointment)).thenReturn(appointment);
        when(mapper.mapToAppointment(appointmentDto)).thenReturn(appointment);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(appointmentDto);

        //When & Then
        mockMvc.perform(put("/v1/appointments").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointment_id", is(15)))
                .andExpect(jsonPath("$.forEmergency", is(true)))
                .andExpect(jsonPath("$.doctorId", is(nullValue())))
                .andExpect(jsonPath("$.patientId", is(nullValue())));
    }

    @Test
    public void shouldDeleteAppointment() throws Exception {
        mockMvc.perform(delete("/v1/appointments/{appointmentId}", 11).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}