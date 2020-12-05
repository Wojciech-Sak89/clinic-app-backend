package com.kodilla.clinic.mapper;

import com.kodilla.clinic.dao.AppointmentDao;
import com.kodilla.clinic.dao.StaffEvaluationDao;
import com.kodilla.clinic.dao.schedule.ClinicDoctorScheduleDao;
import com.kodilla.clinic.dao.schedule.EmergencyHourDao;
import com.kodilla.clinic.dao.schedule.WorkingDayDao;
import com.kodilla.clinic.domain.Appointment;
import com.kodilla.clinic.domain.Doctor;
import com.kodilla.clinic.domain.StaffEvaluation;
import com.kodilla.clinic.dto.DoctorDto;
import com.kodilla.clinic.dto.schedule.ClinicDoctorScheduleDto;
import com.kodilla.clinic.enums.*;
import com.kodilla.clinic.mapper.schedule.ClinicDoctorScheduleMapper;
import com.kodilla.clinic.domain.schedule.ClinicDoctorSchedule;
import com.kodilla.clinic.domain.schedule.EmergencyHour;
import com.kodilla.clinic.domain.schedule.WorkingDay;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DoctorMapperTest {
    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private ClinicDoctorScheduleDao clinicDoctorScheduleDao;

    @Autowired
    private WorkingDayDao workingDayDao;

    @Autowired
    private EmergencyHourDao emergencyHourDao;

    @Autowired
    private StaffEvaluationDao staffEvaluationDao;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private ClinicDoctorScheduleMapper scheduleMapper;

    @Test
    public void testMapToDoctor_AppointmentsAndEvaluations_Not_Assigned() {
        //Given
        WorkingDay workingDay1 = new WorkingDay(Day.MONDAY, Hour.EIGHT_AM, Hour.FIVE_PM);
        WorkingDay workingDay2 = new WorkingDay(Day.WEDNESDAY, Hour.NINE_THIRTY_AM, Hour.SIX_THIRTY_PM);
        WorkingDay workingDay3 = new WorkingDay(Day.FRIDAY, Hour.TWELVE_PM, Hour.SEVEN_THIRTY_PM);
        workingDayDao.save(workingDay1);
        workingDayDao.save(workingDay2);
        workingDayDao.save(workingDay3);

        EmergencyHour emergencyHour1 = new EmergencyHour(Day.FRIDAY, Hour.FOUR_PM);
        EmergencyHour emergencyHour2 = new EmergencyHour(Day.FRIDAY, Hour.SEVEN_THIRTY_PM);
        emergencyHourDao.save(emergencyHour1);
        emergencyHourDao.save(emergencyHour2);

        ClinicDoctorSchedule clinicDoctorSchedule = new ClinicDoctorSchedule.ScheduleBuilder()
                .workingDay(workingDay1)
                .workingDay(workingDay2)
                .workingDay(workingDay3)
                .emergencyHour(emergencyHour1)
                .emergencyHour(emergencyHour2)
                .build();
        workingDay1.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        workingDay2.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        workingDay3.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        emergencyHour1.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        emergencyHour2.setSchedules(Collections.singletonList(clinicDoctorSchedule));

        clinicDoctorScheduleDao.save(clinicDoctorSchedule);
        ClinicDoctorScheduleDto clinicDoctorScheduleDto =
                scheduleMapper.mapToClinicDoctorScheduleDto(clinicDoctorSchedule);

        DoctorDto doctorDto = new DoctorDto(77, "Richard", "Davis",
                Specialization.CHILD_PSYCHIATRY, Department.PSYCHIATRY,
                "rich.dav.md@clinic.com", clinicDoctorScheduleDto, "Richards Biogram",
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
        assertEquals(clinicDoctorSchedule.getSchedule_id(), doctor.getClinicDoctorSchedule().getSchedule_id());
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

        WorkingDay workingDay1 = new WorkingDay(Day.MONDAY, Hour.EIGHT_AM, Hour.FIVE_PM);
        WorkingDay workingDay2 = new WorkingDay(Day.WEDNESDAY, Hour.NINE_THIRTY_AM, Hour.SIX_THIRTY_PM);
        WorkingDay workingDay3 = new WorkingDay(Day.FRIDAY, Hour.TWELVE_PM, Hour.SEVEN_THIRTY_PM);
        workingDayDao.save(workingDay1);
        workingDayDao.save(workingDay2);
        workingDayDao.save(workingDay3);

        EmergencyHour emergencyHour1 = new EmergencyHour(Day.FRIDAY, Hour.FOUR_PM);
        EmergencyHour emergencyHour2 = new EmergencyHour(Day.FRIDAY, Hour.SEVEN_THIRTY_PM);
        emergencyHourDao.save(emergencyHour1);
        emergencyHourDao.save(emergencyHour2);

        ClinicDoctorSchedule clinicDoctorSchedule = new ClinicDoctorSchedule.ScheduleBuilder()
                .workingDay(workingDay1)
                .workingDay(workingDay2)
                .workingDay(workingDay3)
                .emergencyHour(emergencyHour1)
                .emergencyHour(emergencyHour2)
                .build();
        workingDay1.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        workingDay2.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        workingDay3.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        emergencyHour1.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        emergencyHour2.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        clinicDoctorScheduleDao.save(clinicDoctorSchedule);
        ClinicDoctorScheduleDto clinicDoctorScheduleDto =
                scheduleMapper.mapToClinicDoctorScheduleDto(clinicDoctorSchedule);

        DoctorDto doctorDto = new DoctorDto(77, "Richard", "Davis",
                Specialization.CHILD_PSYCHIATRY, Department.PSYCHIATRY,
                "rich.dav.md@clinic.com", clinicDoctorScheduleDto, "Richards Biogram",
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
        assertEquals(clinicDoctorSchedule.getSchedule_id(), doctor.getClinicDoctorSchedule().getSchedule_id());
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
        WorkingDay workingDay1 = new WorkingDay(Day.MONDAY, Hour.EIGHT_AM, Hour.FIVE_PM);
        WorkingDay workingDay2 = new WorkingDay(Day.WEDNESDAY, Hour.NINE_THIRTY_AM, Hour.SIX_THIRTY_PM);
        WorkingDay workingDay3 = new WorkingDay(Day.FRIDAY, Hour.TWELVE_PM, Hour.SEVEN_THIRTY_PM);
        workingDayDao.save(workingDay1);
        workingDayDao.save(workingDay2);
        workingDayDao.save(workingDay3);

        EmergencyHour emergencyHour1 = new EmergencyHour(Day.FRIDAY, Hour.FOUR_PM);
        EmergencyHour emergencyHour2 = new EmergencyHour(Day.FRIDAY, Hour.SEVEN_THIRTY_PM);
        emergencyHourDao.save(emergencyHour1);
        emergencyHourDao.save(emergencyHour2);

        ClinicDoctorSchedule clinicDoctorSchedule = new ClinicDoctorSchedule.ScheduleBuilder()
                .workingDay(workingDay1)
                .workingDay(workingDay2)
                .workingDay(workingDay3)
                .emergencyHour(emergencyHour1)
                .emergencyHour(emergencyHour2)
                .build();
        workingDay1.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        workingDay2.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        workingDay3.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        emergencyHour1.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        emergencyHour2.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        clinicDoctorScheduleDao.save(clinicDoctorSchedule);
        ClinicDoctorScheduleDto clinicDoctorScheduleDto =
                scheduleMapper.mapToClinicDoctorScheduleDto(clinicDoctorSchedule);

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
        assertEquals(Specialization.CHILD_PSYCHIATRY, doctorDto.getSpecialization());
        assertEquals(Department.PSYCHIATRY, doctorDto.getDepartment());
        assertEquals("rich.dav.md@clinic.com", doctorDto.getEmail());
        assertEquals(clinicDoctorScheduleDto.getSchedule_id(), doctorDto.getClinicDoctorScheduleDto().getSchedule_id());
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

        appointmentDao.save(appointment1);
        appointmentDao.save(appointment2);
        staffEvaluationDao.save(staffEvaluation1);
        staffEvaluationDao.save(staffEvaluation2);

        List<Appointment> appointments = new ArrayList<>(Arrays.asList(appointment1, appointment2));
        List<StaffEvaluation> evaluations = new ArrayList<>(Arrays.asList(staffEvaluation1, staffEvaluation2));

        WorkingDay workingDay1 = new WorkingDay(Day.MONDAY, Hour.EIGHT_AM, Hour.FIVE_PM);
        WorkingDay workingDay2 = new WorkingDay(Day.WEDNESDAY, Hour.NINE_THIRTY_AM, Hour.SIX_THIRTY_PM);
        WorkingDay workingDay3 = new WorkingDay(Day.FRIDAY, Hour.TWELVE_PM, Hour.SEVEN_THIRTY_PM);
        workingDayDao.save(workingDay1);
        workingDayDao.save(workingDay2);
        workingDayDao.save(workingDay3);

        EmergencyHour emergencyHour1 = new EmergencyHour(Day.FRIDAY, Hour.FOUR_PM);
        EmergencyHour emergencyHour2 = new EmergencyHour(Day.FRIDAY, Hour.SEVEN_THIRTY_PM);
        emergencyHourDao.save(emergencyHour1);
        emergencyHourDao.save(emergencyHour2);

        ClinicDoctorSchedule clinicDoctorSchedule = new ClinicDoctorSchedule.ScheduleBuilder()
                .workingDay(workingDay1)
                .workingDay(workingDay2)
                .workingDay(workingDay3)
                .emergencyHour(emergencyHour1)
                .emergencyHour(emergencyHour2)
                .build();
        workingDay1.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        workingDay2.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        workingDay3.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        emergencyHour1.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        emergencyHour2.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        clinicDoctorScheduleDao.save(clinicDoctorSchedule);
        ClinicDoctorScheduleDto clinicDoctorScheduleDto =
                scheduleMapper.mapToClinicDoctorScheduleDto(clinicDoctorSchedule);

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
        assertEquals(clinicDoctorScheduleDto.getSchedule_id(), doctorDto.getClinicDoctorScheduleDto().getSchedule_id());
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
        WorkingDay workingDay1 = new WorkingDay(Day.MONDAY, Hour.EIGHT_AM, Hour.FIVE_PM);
        WorkingDay workingDay2 = new WorkingDay(Day.WEDNESDAY, Hour.NINE_THIRTY_AM, Hour.SIX_THIRTY_PM);
        WorkingDay workingDay3 = new WorkingDay(Day.FRIDAY, Hour.TWELVE_PM, Hour.SEVEN_THIRTY_PM);
        workingDayDao.save(workingDay1);
        workingDayDao.save(workingDay2);
        workingDayDao.save(workingDay3);

        EmergencyHour emergencyHour1 = new EmergencyHour(Day.FRIDAY, Hour.FOUR_PM);
        EmergencyHour emergencyHour2 = new EmergencyHour(Day.MONDAY, Hour.SEVEN_THIRTY_PM);
        emergencyHourDao.save(emergencyHour1);
        emergencyHourDao.save(emergencyHour2);

        ClinicDoctorSchedule clinicDoctorSchedule1 = new ClinicDoctorSchedule.ScheduleBuilder()
                .workingDay(workingDay1)
                .emergencyHour(emergencyHour1)
                .build();

        ClinicDoctorSchedule clinicDoctorSchedule2 = new ClinicDoctorSchedule.ScheduleBuilder()
                .workingDay(workingDay3)
                .emergencyHour(emergencyHour2)
                .build();

        workingDay1.setSchedules(Collections.singletonList(clinicDoctorSchedule1));
        workingDay3.setSchedules(Collections.singletonList(clinicDoctorSchedule2));
        emergencyHour1.setSchedules(Collections.singletonList(clinicDoctorSchedule1));
        emergencyHour2.setSchedules(Collections.singletonList(clinicDoctorSchedule2));
        clinicDoctorScheduleDao.save(clinicDoctorSchedule1);
        clinicDoctorScheduleDao.save(clinicDoctorSchedule2);


        Doctor doctor1 = new Doctor(77, "Richard", "Davis",
                Specialization.CHILD_PSYCHIATRY, Department.PSYCHIATRY,
                "rich.dav.md@clinic.com", clinicDoctorSchedule1, "Richards Biogram",
                new ArrayList<>(), new ArrayList<>());

        Doctor doctor2 = new Doctor(33, "Dan", "Simmons",
                Specialization.CARDIOLOGY, Department.CARDIOLOGY,
                "dan.s.md@clinic.com", clinicDoctorSchedule2, "Dans Biogram",
                new ArrayList<>(), new ArrayList<>());

        List<Doctor> doctorList = new ArrayList<>(Arrays.asList(doctor1, doctor2));

        //When
        List<DoctorDto> doctorDtoList = doctorMapper.mapToDoctorDtoList(doctorList);

        //Then
        assertEquals(2, doctorDtoList.size());
        assertEquals("rich.dav.md@clinic.com", doctorDtoList.get(0).getEmail());
        assertEquals(Hour.SEVEN_THIRTY_PM, doctorDtoList.get(1)
                .getClinicDoctorScheduleDto().getWorkingDaysDtos().get(0).getEndHour());
    }
}