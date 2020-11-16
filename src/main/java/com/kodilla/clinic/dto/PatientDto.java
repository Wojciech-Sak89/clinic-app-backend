package com.kodilla.clinic.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PatientDto {
    private Integer patient_id;
    private String name;
    private String surname;
    private String address;
    private LocalDate birthDate;
    private int pesel;
    private int telNum;
    private String email;
    private boolean inUrgency;
    private List<Integer> appointmentsIds;
    private List<Integer> evaluationsIds;
}
