package com.kodilla.clinic.dao;

import com.kodilla.clinic.domain.StaffEvaluation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface StaffEvaluationDao extends CrudRepository<StaffEvaluation, Integer> {
    @Override
    List<StaffEvaluation> findAll();
}
