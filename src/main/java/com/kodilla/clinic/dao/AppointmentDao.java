package com.kodilla.clinic.dao;

import com.kodilla.clinic.domain.Appointment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface AppointmentDao extends CrudRepository<Appointment, Integer> {
//    @Query
//    Optional<List<Appointment>> retrievePatientsForthcomingAppointments(@Param("PATIENT_ID") Integer patientId);

    @Override
    List<Appointment> findAll();
}
