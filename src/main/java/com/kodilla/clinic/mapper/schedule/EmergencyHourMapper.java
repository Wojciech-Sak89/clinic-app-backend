package com.kodilla.clinic.mapper.schedule;

import com.kodilla.clinic.dao.schedule.ClinicDoctorScheduleDao;
import com.kodilla.clinic.dto.schedule.EmergencyHourDto;
import com.kodilla.clinic.domain.schedule.ClinicDoctorSchedule;
import com.kodilla.clinic.domain.schedule.EmergencyHour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EmergencyHourMapper {
    @Autowired
    ClinicDoctorScheduleDao clinicDoctorScheduleDao;

    public EmergencyHour mapToEmergencyHour(EmergencyHourDto emergencyHourDto) {
        return new EmergencyHour(
                emergencyHourDto.getEmergencyHour_id(),
                emergencyHourDto.getDay(),
                emergencyHourDto.getHour(),
                mapToSchedules(emergencyHourDto.getSchedulesIds()));
    }

    public EmergencyHourDto mapToEmergencyHourDto(EmergencyHour emergencyHour) {
        return new EmergencyHourDto(
                emergencyHour.getEmergencyHour_id(),
                emergencyHour.getDay(),
                emergencyHour.getHour(),
                mapToSchedulesIds(emergencyHour.getSchedules()));
    }

    public List<ClinicDoctorSchedule> mapToSchedules(List<Integer> schedulesIds) {
        return schedulesIds.stream()
                .map(scheduleId -> clinicDoctorScheduleDao.findById(scheduleId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<Integer> mapToSchedulesIds(List<ClinicDoctorSchedule> schedules) {
        return schedules.stream()
                .map(ClinicDoctorSchedule::getSchedule_id)
                .collect(Collectors.toList());
    }

    public List<EmergencyHourDto> mapToEmergencyHourDtos(List<EmergencyHour> hours) {
        return hours.stream()
                .map(this::mapToEmergencyHourDto)
                .collect(Collectors.toList());
    }
}