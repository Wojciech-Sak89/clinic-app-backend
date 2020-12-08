package com.kodilla.clinic.controller.schedule;

import com.kodilla.clinic.dto.schedule.ClinicDoctorScheduleDto;
import com.kodilla.clinic.exception.schedule.ClinicDoctorScheduleNotFoundException;
import com.kodilla.clinic.mapper.schedule.ClinicDoctorScheduleMapper;
import com.kodilla.clinic.service.DbService;
import com.kodilla.clinic.service.schedule.ScheduleDbService;
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
    private ScheduleDbService service;

    @RequestMapping(method = RequestMethod.GET, value = "clinicDoctorsSchedules")
    public List<ClinicDoctorScheduleDto> getSchedules() {
        return mapper.mapToClinicDoctorScheduleDtos(service.getAllClinicDoctorSchedules());
    }

    @RequestMapping(method = RequestMethod.GET, value = "clinicDoctorsSchedules/{scheduleId}")
    public ClinicDoctorScheduleDto getSchedule(@PathVariable Integer scheduleId) throws ClinicDoctorScheduleNotFoundException {
        return mapper.mapToClinicDoctorScheduleDto(
                service.getClinicDoctorSchedule(scheduleId).orElseThrow(ClinicDoctorScheduleNotFoundException::new));
    }

    @RequestMapping(method = RequestMethod.POST, value = "clinicDoctorsSchedules")
    public ClinicDoctorScheduleDto addSchedule(@RequestBody ClinicDoctorScheduleDto clinicDoctorScheduleDto) {
        return mapper.mapToClinicDoctorScheduleDto(
                service.saveClinicDoctorSchedule(mapper.mapToClinicDoctorSchedule(clinicDoctorScheduleDto)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "clinicDoctorsSchedules")
    public ClinicDoctorScheduleDto updateSchedule(@RequestBody ClinicDoctorScheduleDto clinicDoctorScheduleDto) {
        return mapper.mapToClinicDoctorScheduleDto(
                service.saveClinicDoctorSchedule(mapper.mapToClinicDoctorSchedule(clinicDoctorScheduleDto)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "clinicDoctorsSchedules/{scheduleId}")
    public void deleteSchedule(@PathVariable Integer scheduleId) {
        service.deleteClinicDoctorSchedule(scheduleId);
    }
}
