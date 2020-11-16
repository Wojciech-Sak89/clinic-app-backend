package com.kodilla.clinic.dto;

import com.kodilla.clinic.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AppointmentDto {
    private Integer appointment_id;
    private boolean forEmergency;
    private LocalDateTime dateTime;
    private Status status;
    private Integer doctorId;
    private Integer patientId;
}
