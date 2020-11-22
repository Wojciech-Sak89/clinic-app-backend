package com.kodilla.clinic.mapper;

import com.kodilla.clinic.dao.DoctorDao;
import com.kodilla.clinic.dao.PatientDao;
import com.kodilla.clinic.domain.Doctor;
import com.kodilla.clinic.domain.Patient;
import com.kodilla.clinic.domain.StaffEvaluation;
import com.kodilla.clinic.dto.StaffEvaluationDto;
import com.kodilla.clinic.enums.*;
import com.kodilla.clinic.domain.schedule.ClinicDoctorSchedule;
import com.kodilla.clinic.domain.schedule.EmergencyHour;
import com.kodilla.clinic.domain.schedule.WorkingDay;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Test
    public void testMapToAppointmentDtoList_EmptyLists() {
        //Given
        List<StaffEvaluation> staffEvaluationList = new ArrayList<>();

        //When
        List<StaffEvaluationDto> staffEvaluationDtoList = staffEvaluationMapper
                .mapToStaffEvaluationDtoList(staffEvaluationList);

        //Then
        assertEquals(0, staffEvaluationDtoList.size());
    }

    @Test
    public void testMapToAppointmentDtoList_With_Doctor_And_Patient() {
        //Given
        Patient patient1 = new Patient(5,
                "Peter", "Smith", "Chopin 30 Street",
                LocalDate.of(1975, Month.AUGUST, 2),
                998877123, 111222333, "smith.j@one.com",
                false, new ArrayList<>(), new ArrayList<>());

        Patient patient2 = new Patient(10,
                "Leo", "Doanldson", "Arena 51 Street",
                LocalDate.of(1999, Month.JANUARY, 15),
                1111119999, 90909090, "leo.d@one.com",
                true, new ArrayList<>(), new ArrayList<>());

        ClinicDoctorSchedule clinicDoctorSchedule1 = new ClinicDoctorSchedule.Builder()
                .workingDay(new WorkingDay(Day.MONDAY, Hour.EIGHT_AM, Hour.FIVE_PM))
                .emergencyHour(new EmergencyHour(Day.FRIDAY, Hour.FOUR_PM))
                .build();
        Doctor doctor1 = new Doctor(77, "Richard", "Davis",
                Specialization.CHILD_PSYCHIATRY, Department.PSYCHIATRY,
                "rich.dav.md@clinic.com", clinicDoctorSchedule1, "Richards Biogram",
                new ArrayList<>(), new ArrayList<>());

        ClinicDoctorSchedule clinicDoctorSchedule = new ClinicDoctorSchedule.Builder()
                .workingDay(new WorkingDay(Day.FRIDAY, Hour.TWELVE_PM, Hour.SEVEN_THIRTY_PM))
                .emergencyHour(new EmergencyHour(Day.MONDAY, Hour.SEVEN_THIRTY_PM))
                .build();
        Doctor doctor2 = new Doctor(33, "Dan", "Simmons",
                Specialization.CARDIOLOGY, Department.CARDIOLOGY,
                "dan.s.md@clinic.com", clinicDoctorSchedule, "Dans Biogram",
                new ArrayList<>(), new ArrayList<>());

        LocalDateTime today = LocalDateTime.now();
        StaffEvaluation evaluation1 = new StaffEvaluation(
                100, Stars.FOUR, "Great doctor", today, patient1, doctor1);

        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        StaffEvaluation evaluation2 = new StaffEvaluation(
                300, Stars.ONE, "Horrible doctor", tomorrow, patient2, doctor2);

        List<StaffEvaluation> staffEvaluationList = new ArrayList<>(Arrays.asList(evaluation1, evaluation2));

        //When
        List<StaffEvaluationDto> staffEvaluationDtoList = staffEvaluationMapper
                .mapToStaffEvaluationDtoList(staffEvaluationList);

        //Then
        assertEquals(2, staffEvaluationDtoList.size());

        assertEquals(Stars.FOUR, staffEvaluationDtoList.get(0).getStars());
        assertEquals(today, staffEvaluationDtoList.get(0).getEntryDate());
        assertEquals(Integer.valueOf(100), staffEvaluationDtoList.get(0).getEvaluation_id());
        assertEquals(Integer.valueOf(5), staffEvaluationDtoList.get(0).getPatient_Id());
        assertEquals(Integer.valueOf(77), staffEvaluationDtoList.get(0).getDoctor_Id());

        assertEquals(Stars.ONE, staffEvaluationDtoList.get(1).getStars());
        assertEquals(tomorrow, staffEvaluationDtoList.get(1).getEntryDate());
        assertEquals(Integer.valueOf(300), staffEvaluationDtoList.get(1).getEvaluation_id());
        assertEquals(Integer.valueOf(10), staffEvaluationDtoList.get(1).getPatient_Id());
        assertEquals(Integer.valueOf(33), staffEvaluationDtoList.get(1).getDoctor_Id());
    }
}