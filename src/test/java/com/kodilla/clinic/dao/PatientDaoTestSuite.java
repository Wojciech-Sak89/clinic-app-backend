package com.kodilla.clinic.dao;

import com.kodilla.clinic.domain.Appointment;
import com.kodilla.clinic.domain.Patient;
import com.kodilla.clinic.domain.StaffEvaluation;
import com.kodilla.clinic.enums.Stars;
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
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PatientDaoTestSuite {
    @Autowired
    private PatientDao patientDao;

    @Test
    public void testPatientDaoSave() {
        //Given
        Patient patient = new Patient(
                "Peter", "Smith", "Chopin 30 Street",
                LocalDate.of(1975, Month.AUGUST, 2),
                998877123, 111222333, "smith.j@one.com");

        //When
        patientDao.save(patient);

        //Then
        int id = patient.getPatient_id();

        Optional<Patient> patientDbRetrieved = patientDao.findById(id);
        Assert.assertTrue(patientDbRetrieved.isPresent());

        Assert.assertEquals(1975, patientDbRetrieved.get().getBirthDate().getYear());
        Assert.assertEquals(998877123, patientDbRetrieved.get().getPesel());
    }

    @Test
    public void testPatientDaoSaveWith_Appointments_And_StaffEvaluations() {
        //Given
        Patient patient = new Patient(
                "Peter", "Smith", "Chopin 30 Street",
                LocalDate.of(1975, Month.AUGUST, 2),
                998877123, 111222333, "smith.j@one.com");

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        Appointment appointment1 = new Appointment(today);
        Appointment appointment2 = new Appointment(tomorrow);
        StaffEvaluation staffEvaluation1 = new StaffEvaluation(Stars.FIVE, "Great doctor", LocalDateTime.now());
        StaffEvaluation staffEvaluation2 = new StaffEvaluation(Stars.THREE, "Was ok", LocalDateTime.now());

        patient.setAppointments(
                Arrays.asList(appointment1, appointment2));
        patient.setEvaluations(
                Arrays.asList(staffEvaluation1, staffEvaluation2));

        //When
        patientDao.save(patient);

        //Then
        int id = patient.getPatient_id();

        Optional<Patient> patientDbRetrieved = patientDao.findById(id);
        Assert.assertTrue(patientDbRetrieved.isPresent());

        Assert.assertEquals(1975, patientDbRetrieved.get().getBirthDate().getYear());
        Assert.assertEquals(today, patientDbRetrieved.get().getAppointments().get(0).getDateTime());
        Assert.assertEquals(tomorrow, patientDbRetrieved.get().getAppointments().get(1).getDateTime());
        Assert.assertEquals(Stars.FIVE, patientDbRetrieved.get().getEvaluations().get(0).getStars());
        Assert.assertEquals("Was ok", patientDbRetrieved.get().getEvaluations().get(1).getOpinion());

    }
}