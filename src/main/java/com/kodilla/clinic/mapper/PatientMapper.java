package com.kodilla.clinic.mapper;

import com.kodilla.clinic.dao.AppointmentDao;
import com.kodilla.clinic.dao.StaffEvaluationDao;
import com.kodilla.clinic.domain.Appointment;
import com.kodilla.clinic.domain.Patient;
import com.kodilla.clinic.domain.StaffEvaluation;
import com.kodilla.clinic.dto.PatientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PatientMapper {
    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private StaffEvaluationDao staffEvaluationDao;

    public Patient mapToPatient(PatientDto patientDto) {
        return new Patient(
                patientDto.getPatient_id(),
                patientDto.getName(),
                patientDto.getSurname(),
                patientDto.getAddress(),
                patientDto.getBirthDate(),
                patientDto.getPesel(),
                patientDto.getTelNum(),
                patientDto.getEmail(),
                patientDto.isInUrgency(),
                mapToAppointments(patientDto.getAppointmentsIds()),
                mapToStaffEvaluations(patientDto.getEvaluationsIds())
        );
    }

    public PatientDto mapToPatientDto(Patient patient) {
        return new PatientDto(
                patient.getPatient_id(),
                patient.getName(),
                patient.getSurname(),
                patient.getAddress(),
                patient.getBirthDate(),
                patient.getPesel(),
                patient.getTelNum(),
                patient.getEmail(),
                patient.isInUrgency(),
                mapToAppointmentsIds(patient.getAppointments()),
                mapToStaffEvaluationsIds(patient.getEvaluations())
        );
    }

    public List<Appointment> mapToAppointments(List<Integer> appointmentsIds) {
        return appointmentsIds.stream()
                .map(appointmentId -> appointmentDao.findById(appointmentId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<Integer> mapToAppointmentsIds(List<Appointment> appointments) {
        return appointments.stream()
                .map(Appointment::getAppointment_id)
                .collect(Collectors.toList());
    }

    public List<StaffEvaluation> mapToStaffEvaluations(List<Integer> evaluationsIds) {
        return evaluationsIds.stream()
                .map(evaluationId -> staffEvaluationDao.findById(evaluationId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<Integer> mapToStaffEvaluationsIds(List<StaffEvaluation> evaluations) {
        return evaluations.stream()
                .map(StaffEvaluation::getEvaluation_id)
                .collect(Collectors.toList());
    }
}