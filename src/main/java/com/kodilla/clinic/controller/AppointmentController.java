package com.kodilla.clinic.controller;

import com.kodilla.clinic.dto.AppointmentDto;
import com.kodilla.clinic.exception.AppointmentNotFoundException;
import com.kodilla.clinic.mapper.AppointmentMapper;
import com.kodilla.clinic.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1")
public class AppointmentController {
    @Autowired
    private AppointmentMapper mapper;

    @Autowired
    private DbService service;

    @RequestMapping(method = RequestMethod.GET, value = "appointments")
    public List<AppointmentDto> getAppointments() {
        return mapper.mapToAppointmentDtoList(service.getAllAppointments());
    }

    @RequestMapping(method = RequestMethod.GET, value = "appointments/{appointmentId}")
    public AppointmentDto getAppointment(@PathVariable Integer appointmentId) throws AppointmentNotFoundException {
        return mapper.mapToAppointmentDto(service.getAppointment(appointmentId).orElseThrow(AppointmentNotFoundException::new));
    }

    @RequestMapping(method = RequestMethod.POST, value = "appointments")
    public AppointmentDto addAppointment(@RequestBody AppointmentDto appointmentDto) {
        return mapper.mapToAppointmentDto(service.saveAppointment(mapper.mapToAppointment(appointmentDto)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "appointments")
    public AppointmentDto updateAppointment(@RequestBody AppointmentDto appointmentDto) {
        return mapper.mapToAppointmentDto(service.saveAppointment(mapper.mapToAppointment(appointmentDto)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "doctors/{appointmentId}")
    public void deleteDoctor(@PathVariable Integer appointmentId) {
        service.deleteAppointment(appointmentId);
    }
}
