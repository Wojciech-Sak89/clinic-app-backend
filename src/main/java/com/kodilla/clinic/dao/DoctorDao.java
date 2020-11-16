package com.kodilla.clinic.dao;

import com.kodilla.clinic.domain.Doctor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface DoctorDao extends CrudRepository<Doctor, Integer> {
}
