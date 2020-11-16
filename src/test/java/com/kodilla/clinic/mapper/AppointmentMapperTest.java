package com.kodilla.clinic.mapper;

import com.kodilla.clinic.dao.DoctorDao;
import com.kodilla.clinic.dao.PatientDao;
import com.kodilla.clinic.domain.Appointment;
import com.kodilla.clinic.domain.Doctor;
import com.kodilla.clinic.domain.Patient;
import com.kodilla.clinic.dto.AppointmentDto;
import com.kodilla.clinic.enums.*;
import com.kodilla.clinic.schedule.ClinicDoctorSchedule;
import com.kodilla.clinic.schedule.EmergencyHour;
import com.kodilla.clinic.schedule.WorkingDay;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AppointmentMapperTest {
    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Test
    public void testMapToAppointment_DoctorAndPatient_Not_Assigned() {
        //Given
        LocalDateTime today = LocalDateTime.now();
        AppointmentDto appointmentDto = new AppointmentDto(
                19, true, today, Status.RESERVED, -1, -1);

        //When
        Appointment appointment = appointmentMapper.mapToAppointment(appointmentDto);

        //Then
        assertEquals(Integer.valueOf(19), appointment.getAppointment_id());
        assertTrue(appointment.isForEmergency());
        assertEquals(today, appointment.getDateTime());
        assertEquals(Status.RESERVED, appointment.getStatus());
        assertNull(appointment.getDoctor());
        assertNull(appointment.getPatient());
    }

    @Test
    public void testMapToAppointment_DoctorAndPatient_Assigned() {
        //Given
        Patient patient = new Patient(
                "Peter", "Smith", "Chopin 30 Street",
                LocalDate.of(1975, Month.AUGUST, 2),
                998877123, 111222333, "smith.j@one.com");


        ClinicDoctorSchedule clinicDoctorSchedule = new ClinicDoctorSchedule.Builder()
                .workingDay(new WorkingDay(Day.MONDAY, Hour.EIGHT_AM, Hour.FIVE_PM))
                .workingDay(new WorkingDay(Day.WEDNESDAY, Hour.NINE_THIRTY_AM, Hour.SIX_THIRTY_PM))
                .workingDay(new WorkingDay(Day.FRIDAY, Hour.TWELVE_PM, Hour.SEVEN_THIRTY_PM))
                .emergencyHour(new EmergencyHour(Day.FRIDAY, Hour.FOUR_PM))
                .emergencyHour(new EmergencyHour(Day.FRIDAY, Hour.SEVEN_THIRTY_PM))
                .build();
        Doctor doctor = new Doctor("Richard", "Davis",
                Specialization.CHILD_PSYCHIATRY, Department.PSYCHIATRY,
                "rich.dav.md@clinic.com", clinicDoctorSchedule, "Richards Biogram");

        patientDao.save(patient);
        doctorDao.save(doctor);
        int patientId = patient.getPatient_id();
        int doctorId = doctor.getDoctor_id();

        LocalDateTime today = LocalDateTime.now();
        AppointmentDto appointmentDto = new AppointmentDto(
                19, true, today, Status.RESERVED, doctorId, patientId);

        //When
        Appointment appointment = appointmentMapper.mapToAppointment(appointmentDto);

        //Then
        assertEquals(Integer.valueOf(19), appointment.getAppointment_id());
        assertTrue(appointment.isForEmergency());
        assertEquals(today, appointment.getDateTime());
        assertEquals(Status.RESERVED, appointment.getStatus());

        assertEquals(doctor.getDoctor_id(), appointment.getDoctor().getDoctor_id());
        assertEquals(doctor.getDepartment(), appointment.getDoctor().getDepartment());
        assertEquals(doctor.getClinicDoctorSchedule(), appointment.getDoctor().getClinicDoctorSchedule());

        assertEquals(Day.MONDAY,
                appointment.getDoctor().getClinicDoctorSchedule().getWorkingDays().get(0).getDay());
        assertEquals(Hour.NINE_THIRTY_AM,
                appointment.getDoctor().getClinicDoctorSchedule().getWorkingDays().get(1).getStartHour());
        assertEquals(Hour.SEVEN_THIRTY_PM,
                appointment.getDoctor().getClinicDoctorSchedule().getWorkingDays().get(2).getEndHour());

        assertEquals(Day.FRIDAY,
                appointment.getDoctor().getClinicDoctorSchedule().getEmergencyHours().get(0).getDay());
        assertEquals(Hour.SEVEN_THIRTY_PM,
                appointment.getDoctor().getClinicDoctorSchedule().getEmergencyHours().get(1).getHour());

        assertEquals(patient.getPatient_id(), appointment.getPatient().getPatient_id());
        assertEquals(patient.getPesel(), appointment.getPatient().getPesel());
        assertEquals(patient.getAddress(), appointment.getPatient().getAddress());
    }

    @Test
    public void testMapToAppointmentDto_DoctorAndPatient_Not_Assigned() {
        //Given
        LocalDateTime today = LocalDateTime.now();
        Appointment appointment = new Appointment(
                19, true, today, Status.RESERVED, null, null);

        //When
        AppointmentDto appointmentDto = appointmentMapper.mapToAppointmentDto(appointment);

        //Then
        assertEquals(Integer.valueOf(19), appointmentDto.getAppointment_id());
        assertTrue(appointmentDto.isForEmergency());
        assertEquals(today, appointmentDto.getDateTime());
        assertEquals(Status.RESERVED, appointmentDto.getStatus());
        assertNull(appointmentDto.getDoctorId());
        assertNull(appointmentDto.getPatientId());
    }

    @Test
    public void testMapToAppointmentDto_DoctorAndPatient_Assigned() {
        //Given
        Patient patient = new Patient(
                "Peter", "Smith", "Chopin 30 Street",
                LocalDate.of(1975, Month.AUGUST, 2),
                998877123, 111222333, "smith.j@one.com");


        ClinicDoctorSchedule clinicDoctorSchedule = new ClinicDoctorSchedule.Builder()
                .workingDay(new WorkingDay(Day.MONDAY, Hour.EIGHT_AM, Hour.FIVE_PM))
                .workingDay(new WorkingDay(Day.WEDNESDAY, Hour.NINE_THIRTY_AM, Hour.SIX_THIRTY_PM))
                .workingDay(new WorkingDay(Day.FRIDAY, Hour.TWELVE_PM, Hour.SEVEN_THIRTY_PM))
                .emergencyHour(new EmergencyHour(Day.FRIDAY, Hour.FOUR_PM))
                .emergencyHour(new EmergencyHour(Day.FRIDAY, Hour.SEVEN_THIRTY_PM))
                .build();
        Doctor doctor = new Doctor("Richard", "Davis",
                Specialization.CHILD_PSYCHIATRY, Department.PSYCHIATRY,
                "rich.dav.md@clinic.com", clinicDoctorSchedule, "Richards Biogram");

        patientDao.save(patient);
        doctorDao.save(doctor);

        LocalDateTime today = LocalDateTime.now();
        Appointment appointment = new Appointment(
                19, true, today, Status.RESERVED, doctor, patient);

        //When
        AppointmentDto appointmentDto = appointmentMapper.mapToAppointmentDto(appointment);

        //Then
        assertEquals(Integer.valueOf(19), appointmentDto.getAppointment_id());
        assertTrue(appointmentDto.isForEmergency());
        assertEquals(today, appointmentDto.getDateTime());
        assertEquals(Status.RESERVED, appointmentDto.getStatus());
        assertEquals(doctor.getDoctor_id(), appointmentDto.getDoctorId());
        assertEquals(patient.getPatient_id(), appointmentDto.getPatientId());
    }
}