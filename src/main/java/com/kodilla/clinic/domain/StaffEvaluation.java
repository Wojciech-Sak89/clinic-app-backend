package com.kodilla.clinic.domain;

import com.kodilla.clinic.enums.Stars;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "STAFF_EVALUATIONS")
public class StaffEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EVALUATION_ID", unique = true)
    private Integer evaluation_id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "EVALUATION")
    private Stars stars;

    @Column(name = "OPINION")
    private String opinion;

    @NotNull
    @Column(name = "DATE")
    private LocalDateTime entryDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PATIENT_ID")
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DOCTOR_ID")
    private Doctor doctor;

    public StaffEvaluation(Stars stars, String opinion, LocalDateTime entryDate) {
        this.stars = stars;
        this.opinion = opinion;
        this.entryDate = entryDate;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
