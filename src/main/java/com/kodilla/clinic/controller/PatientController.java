package com.kodilla.clinic.controller;

import com.kodilla.clinic.dto.PatientDto;
import com.kodilla.clinic.exception.PatientNotFoundException;
import com.kodilla.clinic.mapper.PatientMapper;
import com.kodilla.clinic.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1")
public class PatientController {
    @Autowired
    private PatientMapper mapper;

    @Autowired
    private DbService service;

    @RequestMapping(method = RequestMethod.GET, value = "patients")
    public List<PatientDto> getPatients() {
        return mapper.mapToPatientDtoList(service.getAllPatients());
    }

    @RequestMapping(method = RequestMethod.GET, value = "patients/{patientId}")
    public PatientDto getPatient(@PathVariable Integer patientId) throws PatientNotFoundException {
        return mapper.mapToPatientDto(service.getPatient(patientId).orElseThrow(PatientNotFoundException::new));
    }

    @RequestMapping(method = RequestMethod.POST, value = "patients")
    public PatientDto addPatient(@RequestBody PatientDto patientDto) {
        return mapper.mapToPatientDto(service.savePatient(mapper.mapToPatient(patientDto)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "patients")
    public PatientDto updatePatient(@RequestBody PatientDto patientDto) {
        return mapper.mapToPatientDto(service.savePatient(mapper.mapToPatient(patientDto)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "patients/{patientId}")
    public void deletePatient(@PathVariable Integer patientId) {
        service.deletePatient(patientId);
    }
}