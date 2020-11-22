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

    public StaffEvaluationDto(Integer evaluation_id, Stars stars, String opinion, Integer patient_Id, Integer doctor_Id) {
        this.evaluation_id = evaluation_id;
        this.stars = stars;
        this.opinion = opinion;
        this.patient_Id = patient_Id;
        this.doctor_Id = doctor_Id;
    }
}
