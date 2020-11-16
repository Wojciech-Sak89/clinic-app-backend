package com.kodilla.clinic.dto;

import com.kodilla.clinic.enums.Stars;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StaffEvaluationDto {
    private Integer evaluation_id;
    private Stars stars;
    private String opinion;
    private LocalDateTime entryDate;
    private Integer patient_Id;
    private Integer doctor_Id;
}
