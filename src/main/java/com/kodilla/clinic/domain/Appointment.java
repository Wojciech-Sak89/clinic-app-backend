package com.kodilla.clinic.domain;

import com.kodilla.clinic.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "APPOINTMENTS")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "APPOINTMENT_ID", unique = true)
    private Integer appointment_id;

    @Column(name = "FOR_EMERGENCY")
    private boolean forEmergency;

    @Column(name = "DATE")
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "DOCTOR_ID")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "PATIENT_ID")
    private Patient patient;

    public Appointment(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        this.status = Status.OPEN;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setForEmergency(boolean forEmergency) {
        this.forEmergency = forEmergency;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
