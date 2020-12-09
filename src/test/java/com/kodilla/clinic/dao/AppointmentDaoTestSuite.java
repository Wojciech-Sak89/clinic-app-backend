package com.kodilla.clinic.dao;

import com.kodilla.clinic.dao.schedule.ClinicDoctorScheduleDao;
import com.kodilla.clinic.dao.schedule.EmergencyHourDao;
import com.kodilla.clinic.dao.schedule.WorkingDayDao;
import com.kodilla.clinic.domain.Appointment;
import com.kodilla.clinic.domain.Doctor;
import com.kodilla.clinic.domain.Patient;
import com.kodilla.clinic.enums.*;
import com.kodilla.clinic.domain.schedule.ClinicDoctorSchedule;
import com.kodilla.clinic.domain.schedule.EmergencyHour;
import com.kodilla.clinic.domain.schedule.WorkingDay;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AppointmentDaoTestSuite {
    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private StaffEvaluationDao staffEvaluationDao;

    @Autowired
    private WorkingDayDao workingDayDao;

    @Autowired
    private EmergencyHourDao emergencyHourDao;

    @Autowired
    private ClinicDoctorScheduleDao clinicDoctorScheduleDao;

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
        assertEquals(today, appointmentDbRetrieved.get().getDateTime());
        assertEquals(Status.OPEN, appointmentDbRetrieved.get().getStatus());
    }

    @Test
    public void testAppointmentDaoSaveWith_Patient_And_Doctor() {
        //Given
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        Appointment appointment = new Appointment(tomorrow);

        Patient patient = new Patient(
                "Peter", "Smith", "Chopin 30 Street",
                LocalDate.of(1975, Month.AUGUST, 2),
                new BigDecimal(998877123), new BigDecimal(111222333), "wojciech.r.sak@gmail.com");


        ClinicDoctorSchedule clinicDoctorSchedule = new ClinicDoctorSchedule.ScheduleBuilder()
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
        assertEquals(tomorrow, appointmentDbRetrieved.get().getDateTime());
        assertEquals(Status.OPEN, appointmentDbRetrieved.get().getStatus());

        assertEquals(patient.getEmail(), appointmentDbRetrieved.get().getPatient().getEmail());
        assertEquals(patient.getBirthDate().getDayOfMonth(),
                appointmentDbRetrieved.get().getPatient().getBirthDate().getDayOfMonth());

        assertEquals(doctor.getEmail(), appointmentDbRetrieved.get().getDoctor().getEmail());
        assertEquals(doctor.getClinicDoctorSchedule(),
                appointmentDbRetrieved.get().getDoctor().getClinicDoctorSchedule());
        assertEquals(doctor.getDepartment(), appointmentDbRetrieved.get().getDoctor().getDepartment());
    }

    @Test
    public void testNamedQuery_retrievePatientsForthcomingAppointments() {
        //Given
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        LocalDateTime fewHoursLater = LocalDateTime.now().plusHours(5);
        Appointment appointmentTomorrow = new Appointment(tomorrow);
        Appointment appointmentYesterday = new Appointment(yesterday);
        Appointment appointmentFewHoursLater = new Appointment(fewHoursLater);

        Patient patient = new Patient(
                "Peter", "Smith", "Chopin 30 Street",
                LocalDate.of(1975, Month.AUGUST, 2),
                new BigDecimal(998877123), new BigDecimal(111222333), "smith.j@one.com");

        appointmentTomorrow.setPatient(patient);
        appointmentFewHoursLater.setPatient(patient);
        appointmentYesterday.setPatient(patient);

        patient.setAppointments(
                Arrays.asList(appointmentFewHoursLater, appointmentTomorrow, appointmentYesterday));

        //When
        patientDao.save(patient);
        appointmentDao.save(appointmentFewHoursLater);
        appointmentDao.save(appointmentTomorrow);
        appointmentDao.save(appointmentYesterday);

        //Then
        List<Appointment> forthcomingAppointmentsDbRetrieved = appointmentDao
                .retrievePatientsForthcomingAppointments(patient.getPatient_id());

        assertEquals(2, forthcomingAppointmentsDbRetrieved.size());
        assertEquals(tomorrow, forthcomingAppointmentsDbRetrieved.get(1).getDateTime());
        assertEquals(fewHoursLater, forthcomingAppointmentsDbRetrieved.get(0).getDateTime());
    }
}