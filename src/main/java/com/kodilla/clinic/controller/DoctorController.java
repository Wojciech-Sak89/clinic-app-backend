package com.kodilla.clinic.controller;

import com.kodilla.clinic.dto.DoctorDto;
import com.kodilla.clinic.exception.DoctorNotFoundException;
import com.kodilla.clinic.mapper.DoctorMapper;
import com.kodilla.clinic.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1")
public class DoctorController {
    @Autowired
    private DoctorMapper mapper;

    @Autowired
    private DbService service;

    @RequestMapping(method = RequestMethod.GET, value = "doctors")
    public List<DoctorDto> getDoctors() {
        return mapper.mapToDoctorDtoList(service.getAllDoctors());
    }

    @RequestMapping(method = RequestMethod.GET, value = "doctors/{doctorId}")
    public DoctorDto getDoctor(@PathVariable Integer doctorId) throws DoctorNotFoundException {
        return mapper.mapToDoctorDto(service.getDoctor(doctorId).orElseThrow(DoctorNotFoundException::new));
    }

    @RequestMapping(method = RequestMethod.POST, value = "doctors")
    public DoctorDto addDoctor(@RequestBody DoctorDto doctorDto) {
        return mapper.mapToDoctorDto(service.saveDoctor(mapper.mapToDoctor(doctorDto)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "doctors")
    public DoctorDto updateDoctor(@RequestBody DoctorDto doctorDto) {
        return mapper.mapToDoctorDto(service.saveDoctor(mapper.mapToDoctor(doctorDto)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "doctors/{doctorId}")
    public void deleteDoctor(@PathVariable Integer doctorId) {
        service.deleteDoctor(doctorId);
    }
}

/*
6.	Zwolnij lekarza ;) PUT
7.	*Pobierz schedule lekarza GET
8.	*Zmień Schedule lekarza -> workingDays PUT
9.	*Zmień Schedule lekarza -> emergencyHours PUT
*/