package com.kodilla.clinic.mapper;

import com.kodilla.clinic.dao.AppointmentDao;
import com.kodilla.clinic.dao.StaffEvaluationDao;
import com.kodilla.clinic.domain.Appointment;
import com.kodilla.clinic.domain.Doctor;
import com.kodilla.clinic.domain.StaffEvaluation;
import com.kodilla.clinic.dto.DoctorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DoctorMapper {
    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private StaffEvaluationDao staffEvaluationDao;

    public Doctor mapToDoctor(DoctorDto doctorDto) {
        return new Doctor(
                doctorDto.getDoctor_id(),
                doctorDto.getName(),
                doctorDto.getSurname(),
                doctorDto.getSpecialization(),
                doctorDto.getDepartment(),
                doctorDto.getEmail(),
                doctorDto.getClinicDoctorSchedule(),
                doctorDto.getBio(),
                mapToAppointments(doctorDto.getAppointmentsIds()),
                mapToStaffEvaluations(doctorDto.getEvaluationsIds())
                );
    }

    public DoctorDto mapToDoctorDto(Doctor doctor) {
        return new DoctorDto(
                doctor.getDoctor_id(),
                doctor.getName(),
                doctor.getSurname(),
                doctor.getSpecialization(),
                doctor.getDepartment(),
                doctor.getEmail(),
                doctor.getClinicDoctorSchedule(),
                doctor.getBio(),
                mapToAppointmentsIds(doctor.getAppointments()),
                mapToStaffEvaluationsIds(doctor.getEvaluations())
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
