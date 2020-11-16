package com.kodilla.clinic.mapper;

import com.kodilla.clinic.dao.DoctorDao;
import com.kodilla.clinic.dao.PatientDao;
import com.kodilla.clinic.domain.Doctor;
import com.kodilla.clinic.domain.Patient;
import com.kodilla.clinic.domain.StaffEvaluation;
import com.kodilla.clinic.dto.StaffEvaluationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StaffEvaluationMapper {
    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private PatientDao patientDao;

    public StaffEvaluation mapToStuffEvaluation(StaffEvaluationDto staffEvaluationDto) {
        return new StaffEvaluation(
                staffEvaluationDto.getEvaluation_id(),
                staffEvaluationDto.getStars(),
                staffEvaluationDto.getOpinion(),
                staffEvaluationDto.getEntryDate(),
                patientDao.findById(staffEvaluationDto.getPatient_Id()).orElse(null),
                doctorDao.findById(staffEvaluationDto.getDoctor_Id()).orElse(null)
        );
    }

    public StaffEvaluationDto mapToStuffEvaluationDto(StaffEvaluation staffEvaluation) {
        return new StaffEvaluationDto(
                staffEvaluation.getEvaluation_id(),
                staffEvaluation.getStars(),
                staffEvaluation.getOpinion(),
                staffEvaluation.getEntryDate(),
                Optional.ofNullable(staffEvaluation.getPatient()).orElse(new Patient()).getPatient_id(),
                Optional.ofNullable(staffEvaluation.getDoctor()).orElse(new Doctor()).getDoctor_id()
        );
    }
}