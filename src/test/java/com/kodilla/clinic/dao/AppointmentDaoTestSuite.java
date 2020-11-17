package com.kodilla.clinic.dao;

import com.kodilla.clinic.domain.Appointment;
import com.kodilla.clinic.domain.Doctor;
import com.kodilla.clinic.domain.Patient;
import com.kodilla.clinic.enums.*;
import com.kodilla.clinic.schedule.ClinicDoctorSchedule;
import com.kodilla.clinic.schedule.EmergencyHour;
import com.kodilla.clinic.schedule.WorkingDay;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AppointmentDaoTestSuite {
    @Autowired
    private AppointmentDao appointmentDao;

//    @Autowired
//    private PatientDao patientDao;

    @Test
    public void testAppointmentDaoSave() {
        //Given
        LocalDateTime today = LocalDateTime.now();
        Appointment appointment = new Appointment(today);

        //When
        appointmentDao.save(appointment);

        //Then
        int id = appointment.getAppointment_id();

        Optional<Appointment> appointmentDbRetrieved = appointmentDao.findById(id);
        Assert.assertTrue(appointmentDbRetrieved.isPresent());
        Assert.assertEquals(today, appointmentDbRetrieved.get().getDateTime());
        Assert.assertEquals(Status.OPEN, appointmentDbRetrieved.get().getStatus());
    }

    @Test
    public void testAppointmentDaoSaveWith_Patient_And_Doctor() {
        //Given
        LocalDateTime today = LocalDateTime.now();
        Appointment appointment = new Appointment(today);

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

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        //When
        appointmentDao.save(appointment);

        //Then
        int id = appointment.getAppointment_id();

        Optional<Appointment> appointmentDbRetrieved = appointmentDao.findById(id);
        Assert.assertTrue(appointmentDbRetrieved.isPresent());
        Assert.assertEquals(today, appointmentDbRetrieved.get().getDateTime());
        Assert.assertEquals(Status.OPEN, appointmentDbRetrieved.get().getStatus());

        Assert.assertEquals(patient.getEmail(), appointmentDbRetrieved.get().getPatient().getEmail());
        Assert.assertEquals(patient.getBirthDate().getDayOfMonth(),
                appointmentDbRetrieved.get().getPatient().getBirthDate().getDayOfMonth());

        Assert.assertEquals(doctor.getEmail(), appointmentDbRetrieved.get().getDoctor().getEmail());
        Assert.assertEquals(doctor.getClinicDoctorSchedule(),
                appointmentDbRetrieved.get().getDoctor().getClinicDoctorSchedule());
        Assert.assertEquals(doctor.getDepartment(), appointmentDbRetrieved.get().getDoctor().getDepartment());
    }

//    @Test
//    public void testNamedQuery_retrievePatientsForthcomingAppointments() {
//        //Given
//        LocalDateTime tommorrow = LocalDateTime.now().plusDays(1);
//        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
//        LocalDateTime fewHoursLater = LocalDateTime.now().plusHours(5);
//        Appointment appointmentTommorrow = new Appointment(tommorrow);
//        Appointment appointmentYesterday = new Appointment(yesterday);
//        Appointment appointmentFewHoursLater = new Appointment(fewHoursLater);
//
//        Patient patient = new Patient(
//                "Peter", "Smith", "Chopin 30 Street",
//                LocalDate.of(1975, Month.AUGUST, 2),
//                998877123, 111222333, "smith.j@one.com");
//
//        appointmentTommorrow.setPatient(patient);
//        appointmentFewHoursLater.setPatient(patient);
//        appointmentYesterday.setPatient(patient);
//
//        patient.setAppointments(
//                Arrays.asList(appointmentFewHoursLater, appointmentTommorrow, appointmentYesterday));
//
//        //When
//        appointmentDao.save(appointmentFewHoursLater);
//        appointmentDao.save(appointmentTommorrow);
//        appointmentDao.save(appointmentYesterday);
//
//        //Then
//        Optional<List<Appointment>> forthcomingAppointmentsDbRetrieved = appointmentDao
//                .retrievePatientsForthcomingAppointments(patient.getPatient_id());
//
//        Assert.assertTrue(forthcomingAppointmentsDbRetrieved.isPresent());
//        Assert.assertEquals(2, forthcomingAppointmentsDbRetrieved.get().size());
//        Assert.assertEquals(fewHoursLater, forthcomingAppointmentsDbRetrieved.get().get(0).getDateTime());
//        Assert.assertEquals(tommorrow, forthcomingAppointmentsDbRetrieved.get().get(1).getDateTime());
//
//    }
}