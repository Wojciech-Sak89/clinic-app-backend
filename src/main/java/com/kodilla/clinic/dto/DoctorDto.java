package com.kodilla.clinic.dto;

import com.kodilla.clinic.dto.schedule.ClinicDoctorScheduleDto;
import com.kodilla.clinic.enums.Department;
import com.kodilla.clinic.enums.Specialization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DoctorDto {
    private Integer doctor_id;
    private String name;
    private String surname;
    private Specialization specialization;
    private Department department;
    private String email;
    private ClinicDoctorScheduleDto clinicDoctorScheduleDto;
    private String bio;
    private List<Integer> appointmentsIds;
    private List<Integer> evaluationsIds;
}
