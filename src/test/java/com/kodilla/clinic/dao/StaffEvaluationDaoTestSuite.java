package com.kodilla.clinic.dao;

import com.kodilla.clinic.domain.Doctor;
import com.kodilla.clinic.domain.Patient;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class StaffEvaluationDaoTestSuite {
    @Autowired
    private StaffEvaluationDao staffEvaluationDao;

    @Test
    public void testStaffEvaluationDaoSave() {
        //Given
        LocalDateTime today = LocalDateTime.now();
        StaffEvaluation staffEvaluation = new StaffEvaluation(Stars.FIVE, "Great doctor", today);

        //When
        staffEvaluationDao.save(staffEvaluation);

        //Then
        int id = staffEvaluation.getEvaluation_id();

        Optional<StaffEvaluation> staffEvaluationDbRetrieved = staffEvaluationDao.findById(id);
        Assert.assertTrue(staffEvaluationDbRetrieved.isPresent());

        Assert.assertEquals(Stars.FIVE, staffEvaluationDbRetrieved.get().getStars());
        Assert.assertEquals("Great doctor", staffEvaluationDbRetrieved.get().getOpinion());
        Assert.assertEquals(today, staffEvaluationDbRetrieved.get().getEntryDate());
    }

    @Test
    public void testStaffEvaluationDaoSaveWith_Doctor_And_Patient() {
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

        Patient patient = new Patient(
                "Peter", "Smith", "Chopin 30 Street",
                LocalDate.of(1975, Month.AUGUST, 2),
                998877123, 111222333, "smith.j@one.com");

        LocalDateTime today = LocalDateTime.now();

        StaffEvaluation staffEvaluation = new StaffEvaluation(Stars.FIVE, "Great doctor", today);
        staffEvaluation.setDoctor(doctor);
        staffEvaluation.setPatient(patient);

        //When
        staffEvaluationDao.save(staffEvaluation);

        //Then
        int id = staffEvaluation.getEvaluation_id();

        Optional<StaffEvaluation> staffEvaluationDbRetrieved = staffEvaluationDao.findById(id);
        Assert.assertTrue(staffEvaluationDbRetrieved.isPresent());

        Assert.assertEquals(Stars.FIVE, staffEvaluationDbRetrieved.get().getStars());
        Assert.assertEquals("Great doctor", staffEvaluationDbRetrieved.get().getOpinion());
        Assert.assertEquals(today, staffEvaluationDbRetrieved.get().getEntryDate());

        Assert.assertEquals(doctor.getEmail(), staffEvaluationDbRetrieved.get().getDoctor().getEmail());
        Assert.assertEquals(doctor.getClinicDoctorSchedule(),
                staffEvaluationDbRetrieved.get().getDoctor().getClinicDoctorSchedule());
        Assert.assertEquals(doctor.getDepartment(), staffEvaluationDbRetrieved.get().getDoctor().getDepartment());
        Assert.assertEquals(doctor.getBio(), staffEvaluationDbRetrieved.get().getDoctor().getBio());

        Assert.assertEquals(1975, staffEvaluationDbRetrieved.get().getPatient().getBirthDate().getYear());
        Assert.assertEquals(998877123, staffEvaluationDbRetrieved.get().getPatient().getPesel());
    }
}