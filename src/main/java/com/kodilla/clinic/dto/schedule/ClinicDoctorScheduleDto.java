package com.kodilla.clinic.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ClinicDoctorScheduleDto {
    private Integer schedule_id;
    private List<WorkingDayDto> workingDaysDtos;
    private List<EmergencyHourDto> emergencyHoursDtos;
}
