package com.kodilla.clinic.dao;

import com.kodilla.clinic.domain.Appointment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface AppointmentDao extends CrudRepository<Appointment, Integer> {
    @Query(nativeQuery = true)
    List<Appointment> retrievePatientsForthcomingAppointments(@Param("PATIENT_ID") Integer patientId);

    @Query
    List<Appointment> retrieveForthcomingAppointments();

    @Override
    List<Appointment> findAll();
}
