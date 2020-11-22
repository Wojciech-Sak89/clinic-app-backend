package com.kodilla.clinic.mapper.schedule;

import com.kodilla.clinic.dao.schedule.ClinicDoctorScheduleDao;
import com.kodilla.clinic.dto.schedule.ClinicDoctorScheduleDto;
import com.kodilla.clinic.dto.schedule.EmergencyHourDto;
import com.kodilla.clinic.dto.schedule.WorkingDayDto;
import com.kodilla.clinic.domain.schedule.ClinicDoctorSchedule;
import com.kodilla.clinic.domain.schedule.EmergencyHour;
import com.kodilla.clinic.domain.schedule.WorkingDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClinicDoctorScheduleMapper {
    @Autowired
    ClinicDoctorScheduleDao clinicDoctorScheduleDao;

    @Autowired
    WorkingDayMapper workingDayMapper;

    @Autowired
    EmergencyHourMapper emergencyHourMapper;

    public ClinicDoctorSchedule mapToClinicDoctorSchedule(ClinicDoctorScheduleDto clinicDoctorScheduleDto) {
        return new ClinicDoctorSchedule(
                clinicDoctorScheduleDto.getSchedule_id(),
                mapToWorkingDays(clinicDoctorScheduleDto.getWorkingDaysDtos()),
                mapToEmergencyHours(clinicDoctorScheduleDto.getEmergencyHoursDtos()));
    }

    public ClinicDoctorScheduleDto mapToClinicDoctorScheduleDto(ClinicDoctorSchedule clinicDoctorSchedule) {
        return new ClinicDoctorScheduleDto(
                clinicDoctorSchedule.getSchedule_id(),
                mapToWorkingDaysDtos(clinicDoctorSchedule.getWorkingDays()),
                mapToEmergencyHoursDtos(clinicDoctorSchedule.getEmergencyHours()));
    }

    public List<WorkingDay> mapToWorkingDays(List<WorkingDayDto> workingDayDtos) {
        return workingDayDtos.stream()
                .map(workingDayDto -> workingDayMapper.mapToWorkingDay(workingDayDto))
                .collect(Collectors.toList());
    }

    public List<WorkingDayDto> mapToWorkingDaysDtos(List<WorkingDay> workingDays) {
        return workingDays.stream()
                .map(workingDay -> workingDayMapper.mapToWorkingDayDto(workingDay))
                .collect(Collectors.toList());
    }

    private List<EmergencyHour> mapToEmergencyHours(List<EmergencyHourDto> emergencyHoursDtos) {
        return emergencyHoursDtos.stream()
                .map(emergencyHourDto -> emergencyHourMapper.mapToEmergencyHour(emergencyHourDto))
                .collect(Collectors.toList());
    }

    private List<EmergencyHourDto> mapToEmergencyHoursDtos(List<EmergencyHour> emergencyHours) {
        return emergencyHours.stream()
                .map(emergencyHour -> emergencyHourMapper.mapToEmergencyHourDto(emergencyHour))
                .collect(Collectors.toList());
    }

    public List<ClinicDoctorScheduleDto> mapToClinicDoctorScheduleDtos(List<ClinicDoctorSchedule> schedules) {
        return schedules.stream()
                .map(this::mapToClinicDoctorScheduleDto)
                .collect(Collectors.toList());
    }
}