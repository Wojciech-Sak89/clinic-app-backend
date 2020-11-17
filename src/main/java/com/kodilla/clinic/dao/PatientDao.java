package com.kodilla.clinic.dao;

import com.kodilla.clinic.domain.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface PatientDao extends CrudRepository<Patient, Integer> {
    @Override
    List<Patient> findAll();
}
