package com.kodilla.clinic.mapper;

import com.kodilla.clinic.dao.DoctorDao;
import com.kodilla.clinic.dao.PatientDao;
import com.kodilla.clinic.domain.Doctor;
import com.kodilla.clinic.domain.Patient;
import com.kodilla.clinic.domain.StaffEvaluation;
import com.kodilla.clinic.dto.StaffEvaluationDto;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class StaffEvaluationMapperTest {
    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private StaffEvaluationMapper staffEvaluationMapper;

    @Test
    public void testMapToStaffEvaluation_DoctorAndPatient_Not_Assigned() {
        //Given
        LocalDateTime today = LocalDateTime.now();
        StaffEvaluationDto staffEvaluationDto = new StaffEvaluationDto(
                23, Stars.FIVE, "Great doctor", today, -1, -1);

        //When
        StaffEvaluation staffEvaluation = staffEvaluationMapper.mapToStuffEvaluation(staffEvaluationDto);

        //Then
        assertEquals(Integer.valueOf(23), staffEvaluation.getEvaluation_id());
        assertEquals(Stars.FIVE, staffEvaluation.getStars());
        assertEquals("Great doctor", staffEvaluation.getOpinion());
        assertEquals(today, staffEvaluation.getEntryDate());
        assertNull(staffEvaluation.getPatient());
        assertNull(staffEvaluation.getDoctor());
    }

    @Test
    public void testMapToStaffEvaluation_DoctorAndPatient_Assigned() {
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
        StaffEvaluationDto staffEvaluationDto = new StaffEvaluationDto(
                23, Stars.FIVE, "Great doctor", today, patientId, doctorId);

        //When
        StaffEvaluation staffEvaluation = staffEvaluationMapper.mapToStuffEvaluation(staffEvaluationDto);

        //Then
        assertEquals(Integer.valueOf(23), staffEvaluation.getEvaluation_id());
        assertEquals(Stars.FIVE, staffEvaluation.getStars());
        assertEquals("Great doctor", staffEvaluation.getOpinion());
        assertEquals(today, staffEvaluation.getEntryDate());

        assertEquals(doctor.getDoctor_id(), staffEvaluation.getDoctor().getDoctor_id());
        assertEquals(doctor.getDepartment(), staffEvaluation.getDoctor().getDepartment());
        assertEquals(doctor.getClinicDoctorSchedule(), staffEvaluation.getDoctor().getClinicDoctorSchedule());

        assertEquals(Day.MONDAY,
                staffEvaluation.getDoctor().getClinicDoctorSchedule().getWorkingDays().get(0).getDay());
        assertEquals(Hour.NINE_THIRTY_AM,
                staffEvaluation.getDoctor().getClinicDoctorSchedule().getWorkingDays().get(1).getStartHour());
        assertEquals(Hour.SEVEN_THIRTY_PM,
                staffEvaluation.getDoctor().getClinicDoctorSchedule().getWorkingDays().get(2).getEndHour());

        assertEquals(Day.FRIDAY,
                staffEvaluation.getDoctor().getClinicDoctorSchedule().getEmergencyHours().get(0).getDay());
        assertEquals(Hour.SEVEN_THIRTY_PM,
                staffEvaluation.getDoctor().getClinicDoctorSchedule().getEmergencyHours().get(1).getHour());

        assertEquals(patient.getPatient_id(), staffEvaluation.getPatient().getPatient_id());
        assertEquals(patient.getPesel(), staffEvaluation.getPatient().getPesel());
        assertEquals(patient.getAddress(), staffEvaluation.getPatient().getAddress());
    }

    @Test
    public void testMapToStaffEvaluationDto_DoctorAndPatient_Not_Assigned() {
        //Given
        LocalDateTime today = LocalDateTime.now();
        StaffEvaluation staffEvaluation = new StaffEvaluation(
                23, Stars.FIVE, "Great doctor", today, null, null);

        //When
        StaffEvaluationDto staffEvaluationDto = staffEvaluationMapper.mapToStuffEvaluationDto(staffEvaluation);

        //Then
        assertEquals(Integer.valueOf(23), staffEvaluationDto.getEvaluation_id());
        assertEquals(Stars.FIVE, staffEvaluationDto.getStars());
        assertEquals("Great doctor", staffEvaluationDto.getOpinion());
        assertEquals(today, staffEvaluationDto.getEntryDate());
        assertNull(staffEvaluationDto.getPatient_Id());
        assertNull(staffEvaluationDto.getDoctor_Id());
    }

    @Test
    public void testMapToStaffEvaluationDto_DoctorAndPatient_Assigned() {
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

        doctorDao.save(doctor);
        patientDao.save(patient);

        LocalDateTime today = LocalDateTime.now();
        StaffEvaluation staffEvaluation = new StaffEvaluation(
                23, Stars.FIVE, "Great doctor", today, patient, doctor);

        //When
        StaffEvaluationDto staffEvaluationDto = staffEvaluationMapper.mapToStuffEvaluationDto(staffEvaluation);

        //Then
        assertEquals(Integer.valueOf(23), staffEvaluationDto.getEvaluation_id());
        assertEquals(Stars.FIVE, staffEvaluationDto.getStars());
        assertEquals("Great doctor", staffEvaluationDto.getOpinion());
        assertEquals(today, staffEvaluationDto.getEntryDate());
        assertEquals(doctor.getDoctor_id(), staffEvaluationDto.getDoctor_Id());
        assertEquals(patient.getPatient_id(), staffEvaluationDto.getPatient_Id());
    }
}