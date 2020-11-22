package com.kodilla.clinic.dao.schedule;

import com.kodilla.clinic.domain.schedule.WorkingDay;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface WorkingDayDao extends CrudRepository<WorkingDay, Integer> {
    @Override
    List<WorkingDay> findAll();
}