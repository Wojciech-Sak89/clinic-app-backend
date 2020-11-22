package com.kodilla.clinic.mapper.schedule;

import com.kodilla.clinic.dao.schedule.ClinicDoctorScheduleDao;
import com.kodilla.clinic.dto.schedule.WorkingDayDto;
import com.kodilla.clinic.domain.schedule.ClinicDoctorSchedule;
import com.kodilla.clinic.domain.schedule.WorkingDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class WorkingDayMapper {
    @Autowired
    ClinicDoctorScheduleDao clinicDoctorScheduleDao;

    public WorkingDay mapToWorkingDay(WorkingDayDto workingDayDto) {
        return new WorkingDay(
                workingDayDto.getWorkingDay_id(),
                workingDayDto.getDay(),
                workingDayDto.getStartHour(),
                workingDayDto.getEndHour(),
                mapToSchedules(workingDayDto.getSchedulesIds()));
    }

    public WorkingDayDto mapToWorkingDayDto(WorkingDay workingDay) {
        return new WorkingDayDto(
                workingDay.getWorkingDay_id(),
                workingDay.getDay(),
                workingDay.getStartHour(),
                workingDay.getEndHour(),
                mapToSchedulesIds(workingDay.getSchedules()));
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

    public List<WorkingDayDto> mapToWorkingDaysDtos(List<WorkingDay> workingDays) {
        return workingDays.stream()
                .map(this::mapToWorkingDayDto)
                .collect(Collectors.toList());
    }
}
