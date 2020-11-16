package com.kodilla.clinic.domain;

import com.kodilla.clinic.enums.Department;
import com.kodilla.clinic.enums.Specialization;
import com.kodilla.clinic.schedule.ClinicDoctorSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "DOCTORS")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DOCTOR_ID", unique = true)
    private Integer doctor_id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "SPECIALIZATION")
    private Specialization specialization;

    @Column(name = "DEPARTMENT")
    private Department department;

    @Column(name = "EMAIL")
    private String email;

    @Embedded
    private ClinicDoctorSchedule clinicDoctorSchedule;

    @Column(name = "BIO_NOTE")
    private String bio;

    @OneToMany(
            targetEntity = Appointment.class,
            mappedBy = "patient",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(
            targetEntity = StaffEvaluation.class,
            mappedBy = "patient",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    private List<StaffEvaluation> evaluations = new ArrayList<>();

    public Doctor(String name, String surname, Specialization specialization, Department department,
                  String email, ClinicDoctorSchedule clinicDoctorSchedule, String bio) {
        this.name = name;
        this.surname = surname;
        this.specialization = specialization;
        this.department = department;
        this.email = email;
        this.clinicDoctorSchedule = clinicDoctorSchedule;
        this.bio = bio;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void setEvaluations(List<StaffEvaluation> evaluations) {
        this.evaluations = evaluations;
    }
}
