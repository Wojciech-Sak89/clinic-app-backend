package com.kodilla.clinic.controller.schedule;

import com.kodilla.clinic.dto.schedule.ClinicDoctorScheduleDto;
import com.kodilla.clinic.exception.schedule.ClinicDoctorScheduleNotFoundException;
import com.kodilla.clinic.mapper.schedule.ClinicDoctorScheduleMapper;
import com.kodilla.clinic.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1")
public class ClinicDoctorScheduleController {
    @Autowired
    private ClinicDoctorScheduleMapper mapper;

    @Autowired
    private DbService service;

    @RequestMapping(method = RequestMethod.GET, value = "clinicDoctorsSchedules")
    public List<ClinicDoctorScheduleDto> getEmergencyHours() {
        return mapper.mapToClinicDoctorScheduleDtos(service.getAllClinicDoctorSchedules());
    }

    @RequestMapping(method = RequestMethod.GET, value = "clinicDoctorsSchedules/{scheduleId}")
    public ClinicDoctorScheduleDto getEmergencyHour(@PathVariable Integer scheduleId) throws ClinicDoctorScheduleNotFoundException {
        return mapper.mapToClinicDoctorScheduleDto(
                service.getClinicDoctorSchedule(scheduleId).orElseThrow(ClinicDoctorScheduleNotFoundException::new));
    }

    @RequestMapping(method = RequestMethod.POST, value = "clinicDoctorsSchedules")
    public ClinicDoctorScheduleDto addEmergencyHour(@RequestBody ClinicDoctorScheduleDto clinicDoctorScheduleDto) {
        return mapper.mapToClinicDoctorScheduleDto(
                service.saveClinicDoctorSchedule(mapper.mapToClinicDoctorSchedule(clinicDoctorScheduleDto)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "clinicDoctorsSchedules")
    public ClinicDoctorScheduleDto updateEmergencyHour(@RequestBody ClinicDoctorScheduleDto clinicDoctorScheduleDto) {
        return mapper.mapToClinicDoctorScheduleDto(
                service.saveClinicDoctorSchedule(mapper.mapToClinicDoctorSchedule(clinicDoctorScheduleDto)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "clinicDoctorsSchedules/{scheduleId}")
    public void deleteEmergencyHour(@PathVariable Integer scheduleId) {
        service.deleteClinicDoctorSchedule(scheduleId);
    }
}
