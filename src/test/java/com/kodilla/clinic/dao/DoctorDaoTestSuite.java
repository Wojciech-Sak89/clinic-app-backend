package com.kodilla.clinic.dao;

import com.kodilla.clinic.domain.Appointment;
import com.kodilla.clinic.domain.Doctor;
import com.kodilla.clinic.domain.StaffEvaluation;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DoctorDaoTestSuite {
    @Autowired
    private DoctorDao doctorDao;

    @Test
    public void testDoctorDaoSave() {
        //Given
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

        //When
        doctorDao.save(doctor);

        //Then
        int id = doctor.getDoctor_id();

        Optional<Doctor> doctorDbRetrieved = doctorDao.findById(id);
        Assert.assertTrue(doctorDbRetrieved.isPresent());

        Assert.assertEquals(doctor.getEmail(), doctorDbRetrieved.get().getEmail());
        Assert.assertEquals(doctor.getClinicDoctorSchedule(),
                doctorDbRetrieved.get().getClinicDoctorSchedule());
        Assert.assertEquals(doctor.getDepartment(), doctorDbRetrieved.get().getDepartment());
        Assert.assertEquals(doctor.getBio(), doctorDbRetrieved.get().getBio());
    }

    @Test
    public void testDoctorDaoSaveWith_Appointments_And_StaffEvaluations() {
        //Given
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

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        Appointment appointment1 = new Appointment(today);
        Appointment appointment2 = new Appointment(tomorrow);
        StaffEvaluation staffEvaluation1 = new StaffEvaluation(Stars.FIVE, "Great doctor", LocalDateTime.now());
        StaffEvaluation staffEvaluation2 = new StaffEvaluation(Stars.THREE, "Was ok", LocalDateTime.now());

        doctor.setAppointments(Arrays.asList(appointment1, appointment2));
        doctor.setEvaluations(Arrays.asList(staffEvaluation1, staffEvaluation2));

        //When
        doctorDao.save(doctor);

        //Then
        int id = doctor.getDoctor_id();

        Optional<Doctor> doctorDbRetrieved = doctorDao.findById(id);
        Assert.assertTrue(doctorDbRetrieved.isPresent());

        Assert.assertEquals(doctor.getEmail(), doctorDbRetrieved.get().getEmail());
        Assert.assertEquals(doctor.getClinicDoctorSchedule(),
                doctorDbRetrieved.get().getClinicDoctorSchedule());
        Assert.assertEquals(doctor.getDepartment(), doctorDbRetrieved.get().getDepartment());
        Assert.assertEquals(doctor.getBio(), doctorDbRetrieved.get().getBio());

        Assert.assertEquals(today, doctorDbRetrieved.get().getAppointments().get(0).getDateTime());
        Assert.assertEquals(tomorrow, doctorDbRetrieved.get().getAppointments().get(1).getDateTime());

        Assert.assertEquals(Stars.FIVE, doctorDbRetrieved.get().getEvaluations().get(0).getStars());
        Assert.assertEquals("Was ok", doctorDbRetrieved.get().getEvaluations().get(1).getOpinion());
    }
}