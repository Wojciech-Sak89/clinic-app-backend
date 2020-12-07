package com.kodilla.clinic.domain;

import com.kodilla.clinic.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;

@NamedQueries({
        @NamedQuery(
                name = "Appointment.retrieveForthcomingAppointments",
                query = "FROM Appointment WHERE dateTime > NOW()"
        )
})
@NamedNativeQuery(
        name = "Appointment.retrievePatientsForthcomingAppointments",
        query = "SELECT * FROM APPOINTMENTS" +
                " WHERE PATIENT_ID = :PATIENT_ID AND DATE > NOW()",
        resultClass = Appointment.class
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "APPOINTMENTS")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DOCTOR_ID")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PATIENT_ID")
    private Patient patient;

    public Appointment(boolean forEmergency, LocalDateTime dateTime, Status status) {
        this.forEmergency = forEmergency;
        this.dateTime = dateTime;
        this.status = status;
    }

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
