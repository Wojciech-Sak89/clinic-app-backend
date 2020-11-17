package com.kodilla.clinic.mapper;

import com.kodilla.clinic.dao.AppointmentDao;
import com.kodilla.clinic.dao.StaffEvaluationDao;
import com.kodilla.clinic.domain.Appointment;
import com.kodilla.clinic.domain.Doctor;
import com.kodilla.clinic.domain.Patient;
import com.kodilla.clinic.domain.StaffEvaluation;
import com.kodilla.clinic.dto.DoctorDto;
import com.kodilla.clinic.dto.PatientDto;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DoctorMapperTest {
    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private StaffEvaluationDao staffEvaluationDao;

    @Autowired
    private DoctorMapper doctorMapper;

    @Test
    public void testMapToDoctor_AppointmentsAndEvaluations_Not_Assigned() {
        //Given
        ClinicDoctorSchedule clinicDoctorSchedule = new ClinicDoctorSchedule.Builder()
                .workingDay(new WorkingDay(Day.MONDAY, Hour.EIGHT_AM, Hour.FIVE_PM))
                .workingDay(new WorkingDay(Day.WEDNESDAY, Hour.NINE_THIRTY_AM, Hour.SIX_THIRTY_PM))
                .workingDay(new WorkingDay(Day.FRIDAY, Hour.TWELVE_PM, Hour.SEVEN_THIRTY_PM))
                .emergencyHour(new EmergencyHour(Day.FRIDAY, Hour.FOUR_PM))
                .emergencyHour(new EmergencyHour(Day.FRIDAY, Hour.SEVEN_THIRTY_PM))
                .build();
        DoctorDto doctorDto = new DoctorDto(77, "Richard", "Davis",
                Specialization.CHILD_PSYCHIATRY, Department.PSYCHIATRY,
                "rich.dav.md@clinic.com", clinicDoctorSchedule, "Richards Biogram",
                new ArrayList<>(), new ArrayList<>());

        //When
        Doctor doctor = doctorMapper.mapToDoctor(doctorDto);

        //Then
        assertEquals(Integer.valueOf(77), doctor.getDoctor_id());
        assertEquals("Richard", doctor.getName());
        assertEquals("Davis", doctor.getSurname());
        assertEquals( Specialization.CHILD_PSYCHIATRY, doctor.getSpecialization());
        assertEquals(Department.PSYCHIATRY, doctor.getDepartment());
        assertEquals("rich.dav.md@clinic.com", doctor.getEmail());
        assertEquals(clinicDoctorSchedule, doctor.getClinicDoctorSchedule());
        assertEquals("Richards Biogram", doctor.getBio());
        assertEquals(0, doctor.getAppointments().size());
        assertEquals(0, doctor.getEvaluations().size());
    }

    @Test
    public void testMapToDoctor_AppointmentsAndEvaluations_Assigned() {
        //Given
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        Appointment appointment1 = new Appointment(today);
        Appointment appointment2 = new Appointment(tomorrow);
        StaffEvaluation staffEvaluation1 = new StaffEvaluation(Stars.FIVE, "Great doctor", LocalDateTime.now());
        StaffEvaluation staffEvaluation2 = new StaffEvaluation(Stars.THREE, "Was ok", LocalDateTime.now());

        appointmentDao.save(appointment1);
        appointmentDao.save(appointment2);
        staffEvaluationDao.save(staffEvaluation1);
        staffEvaluationDao.save(staffEvaluation2);

        int appointment1Id = appointment1.getAppointment_id();
        int appointment2Id = appointment2.getAppointment_id();
        int staffEvaluation1Id = staffEvaluation1.getEvaluation_id();
        int staffEvaluation2Id = staffEvaluation2.getEvaluation_id();

        List<Integer> appointmentsIds = new ArrayList<>(Arrays.asList(appointment1Id, appointment2Id));
        List<Integer> evaluationsIds = new ArrayList<>(Arrays.asList(staffEvaluation1Id, staffEvaluation2Id));

        ClinicDoctorSchedule clinicDoctorSchedule = new ClinicDoctorSchedule.Builder()
                .workingDay(new WorkingDay(Day.MONDAY, Hour.EIGHT_AM, Hour.FIVE_PM))
                .workingDay(new WorkingDay(Day.WEDNESDAY, Hour.NINE_THIRTY_AM, Hour.SIX_THIRTY_PM))
                .workingDay(new WorkingDay(Day.FRIDAY, Hour.TWELVE_PM, Hour.SEVEN_THIRTY_PM))
                .emergencyHour(new EmergencyHour(Day.FRIDAY, Hour.FOUR_PM))
                .emergencyHour(new EmergencyHour(Day.FRIDAY, Hour.SEVEN_THIRTY_PM))
                .build();
        DoctorDto doctorDto = new DoctorDto(77, "Richard", "Davis",
                Specialization.CHILD_PSYCHIATRY, Department.PSYCHIATRY,
                "rich.dav.md@clinic.com", clinicDoctorSchedule, "Richards Biogram",
                appointmentsIds, evaluationsIds);

        //When
        Doctor doctor = doctorMapper.mapToDoctor(doctorDto);

        //Then
        assertEquals(Integer.valueOf(77), doctor.getDoctor_id());
        assertEquals("Richard", doctor.getName());
        assertEquals("Davis", doctor.getSurname());
        assertEquals( Specialization.CHILD_PSYCHIATRY, doctor.getSpecialization());
        assertEquals(Department.PSYCHIATRY, doctor.getDepartment());
        assertEquals("rich.dav.md@clinic.com", doctor.getEmail());
        assertEquals(clinicDoctorSchedule, doctor.getClinicDoctorSchedule());
        assertEquals("Richards Biogram", doctor.getBio());

        assertEquals(Integer.valueOf(appointment1Id), doctor.getAppointments().get(0).getAppointment_id());
        assertEquals(Status.OPEN, doctor.getAppointments().get(0).getStatus());
        assertEquals(Integer.valueOf(appointment2Id), doctor.getAppointments().get(1).getAppointment_id());
        assertEquals(tomorrow, doctor.getAppointments().get(1).getDateTime());

        assertEquals(Integer.valueOf(staffEvaluation1Id), doctor.getEvaluations().get(0).getEvaluation_id());
        assertEquals(Stars.FIVE, doctor.getEvaluations().get(0).getStars());
        assertEquals(Integer.valueOf(staffEvaluation2Id), doctor.getEvaluations().get(1).getEvaluation_id());
        assertEquals("Was ok", doctor.getEvaluations().get(1).getOpinion());
    }

    @Test
    public void testMapToDoctorDto_AppointmentsAndEvaluations_Not_Assigned() {
        //Given
        ClinicDoctorSchedule clinicDoctorSchedule = new ClinicDoctorSchedule.Builder()
                .workingDay(new WorkingDay(Day.MONDAY, Hour.EIGHT_AM, Hour.FIVE_PM))
                .workingDay(new WorkingDay(Day.WEDNESDAY, Hour.NINE_THIRTY_AM, Hour.SIX_THIRTY_PM))
                .workingDay(new WorkingDay(Day.FRIDAY, Hour.TWELVE_PM, Hour.SEVEN_THIRTY_PM))
                .emergencyHour(new EmergencyHour(Day.FRIDAY, Hour.FOUR_PM))
                .emergencyHour(new EmergencyHour(Day.FRIDAY, Hour.SEVEN_THIRTY_PM))
                .build();
        Doctor doctor = new Doctor(77, "Richard", "Davis",
                Specialization.CHILD_PSYCHIATRY, Department.PSYCHIATRY,
                "rich.dav.md@clinic.com", clinicDoctorSchedule, "Richards Biogram",
                new ArrayList<>(), new ArrayList<>());

        //When
        DoctorDto doctorDto = doctorMapper.mapToDoctorDto(doctor);

        //Then
        assertEquals(Integer.valueOf(77), doctorDto.getDoctor_id());
        assertEquals("Richard", doctorDto.getName());
        assertEquals("Davis", doctorDto.getSurname());
        assertEquals( Specialization.CHILD_PSYCHIATRY, doctorDto.getSpecialization());
        assertEquals(Department.PSYCHIATRY, doctorDto.getDepartment());
        assertEquals("rich.dav.md@clinic.com", doctorDto.getEmail());
        assertEquals(clinicDoctorSchedule, doctorDto.getClinicDoctorSchedule());
        assertEquals("Richards Biogram", doctorDto.getBio());
        assertEquals(0, doctorDto.getAppointmentsIds().size());
        assertEquals(0, doctorDto.getEvaluationsIds().size());
    }

    @Test
    public void testMapToDoctorDto_AppointmentsAndEvaluations_Assigned() {
        //Given
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        Appointment appointment1 = new Appointment(today);
        Appointment appointment2 = new Appointment(tomorrow);
        StaffEvaluation staffEvaluation1 = new StaffEvaluation(Stars.FIVE, "Great doctor", LocalDateTime.now());
        StaffEvaluation staffEvaluation2 = new StaffEvaluation(Stars.THREE, "Was ok", LocalDateTime.now());

        List<Appointment> appointments = new ArrayList<>(Arrays.asList(appointment1, appointment2));
        List<StaffEvaluation> evaluations = new ArrayList<>(Arrays.asList(staffEvaluation1, staffEvaluation2));

        ClinicDoctorSchedule clinicDoctorSchedule = new ClinicDoctorSchedule.Builder()
                .workingDay(new WorkingDay(Day.MONDAY, Hour.EIGHT_AM, Hour.FIVE_PM))
                .workingDay(new WorkingDay(Day.WEDNESDAY, Hour.NINE_THIRTY_AM, Hour.SIX_THIRTY_PM))
                .workingDay(new WorkingDay(Day.FRIDAY, Hour.TWELVE_PM, Hour.SEVEN_THIRTY_PM))
                .emergencyHour(new EmergencyHour(Day.FRIDAY, Hour.FOUR_PM))
                .emergencyHour(new EmergencyHour(Day.FRIDAY, Hour.SEVEN_THIRTY_PM))
                .build();
        Doctor doctor = new Doctor(77, "Richard", "Davis",
                Specialization.CHILD_PSYCHIATRY, Department.PSYCHIATRY,
                "rich.dav.md@clinic.com", clinicDoctorSchedule, "Richards Biogram",
                appointments, evaluations);

        //When
        DoctorDto doctorDto = doctorMapper.mapToDoctorDto(doctor);

        //Then
        assertEquals(Integer.valueOf(77), doctorDto.getDoctor_id());
        assertEquals("Richard", doctorDto.getName());
        assertEquals("Davis", doctorDto.getSurname());
        assertEquals( Specialization.CHILD_PSYCHIATRY, doctorDto.getSpecialization());
        assertEquals(Department.PSYCHIATRY, doctorDto.getDepartment());
        assertEquals("rich.dav.md@clinic.com", doctorDto.getEmail());
        assertEquals(clinicDoctorSchedule, doctorDto.getClinicDoctorSchedule());
        assertEquals("Richards Biogram", doctorDto.getBio());
        assertEquals(2, doctorDto.getAppointmentsIds().size());
        assertEquals(2, doctorDto.getEvaluationsIds().size());
    }

    @Test
    public void testMapToDoctorDtoList_EmptyList() {
        //Given
        List<Doctor> doctorList = new ArrayList<>();

        //When
        List<DoctorDto> doctorDtoList = doctorMapper.mapToDoctorDtoList(doctorList);

        //Then
        assertEquals(0, doctorDtoList.size());
    }

    @Test
    public void testMapToDoctorDtoList_WithPatients() {
        //Given
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

        List<Doctor> doctorList = new ArrayList<>(Arrays.asList(doctor1, doctor2));

        //When
        List<DoctorDto> doctorDtoList = doctorMapper.mapToDoctorDtoList(doctorList);

        //Then
        assertEquals(2, doctorDtoList.size());
        assertEquals("rich.dav.md@clinic.com", doctorDtoList.get(0).getEmail());
        assertEquals(Hour.SEVEN_THIRTY_PM, doctorDtoList.get(1)
                .getClinicDoctorSchedule().getWorkingDays().get(0).getEndHour());
    }
}