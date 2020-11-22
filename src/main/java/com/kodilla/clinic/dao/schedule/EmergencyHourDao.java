package com.kodilla.clinic.dao.schedule;

import com.kodilla.clinic.domain.schedule.EmergencyHour;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface EmergencyHourDao extends CrudRepository<EmergencyHour, Integer> {
    @Override
    List<EmergencyHour> findAll();
}