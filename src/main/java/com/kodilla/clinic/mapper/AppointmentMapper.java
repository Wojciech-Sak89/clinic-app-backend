package com.kodilla.clinic.mapper;

import com.kodilla.clinic.dao.DoctorDao;
import com.kodilla.clinic.dao.PatientDao;
import com.kodilla.clinic.domain.Appointment;
import com.kodilla.clinic.domain.Doctor;
import com.kodilla.clinic.domain.Patient;
import com.kodilla.clinic.dto.AppointmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppointmentMapper {
    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private PatientDao patientDao;

    public Appointment mapToAppointment(AppointmentDto appointmentDto) {
        return new Appointment(
                appointmentDto.getAppointment_id(),
                appointmentDto.isForEmergency(),
                appointmentDto.getDateTime(),
                appointmentDto.getStatus(),
                doctorDao.findById(appointmentDto.getDoctorId()).orElse(null),
                patientDao.findById(appointmentDto.getPatientId()).orElse(null)
        );
    }

    public AppointmentDto mapToAppointmentDto(Appointment appointment) {
        return new AppointmentDto(
                appointment.getAppointment_id(),
                appointment.isForEmergency(),
                appointment.getDateTime(),
                appointment.getStatus(),
                Optional.ofNullable(appointment.getDoctor()).orElse(new Doctor()).getDoctor_id(),
                Optional.ofNullable(appointment.getPatient()).orElse(new Patient()).getPatient_id()
        );
    }
}

