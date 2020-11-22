package com.kodilla.clinic.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "PATIENTS")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PATIENT_ID", unique = true)
    private Integer patient_id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @Column(name = "PESEL")
    private int pesel;

    @Column(name = "TELEPHONE_NUM")
    private int telNum;

    @Column(name = "EMAIL")
    private String email;

    private boolean inUrgency;

    @OneToMany(
            targetEntity = Appointment.class,
            mappedBy = "patient",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(
            targetEntity = StaffEvaluation.class,
            mappedBy = "patient",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<StaffEvaluation> evaluations = new ArrayList<>();

    public Patient(String name, String surname, String address, LocalDate birthDate, int pesel, int telNum, String email) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.birthDate = birthDate;
        this.pesel = pesel;
        this.telNum = telNum;
        this.email = email;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void setEvaluations(List<StaffEvaluation> evaluations) {
        this.evaluations = evaluations;
    }

    public void setInUrgency(boolean inUrgency) {
        this.inUrgency = inUrgency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Patient patient = (Patient) o;

        if (pesel != patient.pesel) return false;
        if (telNum != patient.telNum) return false;
        if (inUrgency != patient.inUrgency) return false;
        if (!patient_id.equals(patient.patient_id)) return false;
        if (!name.equals(patient.name)) return false;
        if (!surname.equals(patient.surname)) return false;
        if (!address.equals(patient.address)) return false;
        if (!birthDate.equals(patient.birthDate)) return false;
        if (!Objects.equals(email, patient.email)) return false;
        if (!Objects.equals(appointments, patient.appointments))
            return false;
        return Objects.equals(evaluations, patient.evaluations);
    }
}

