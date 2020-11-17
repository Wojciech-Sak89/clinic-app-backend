package com.kodilla.clinic.mapper;

import com.kodilla.clinic.dao.AppointmentDao;
import com.kodilla.clinic.dao.StaffEvaluationDao;
import com.kodilla.clinic.domain.Appointment;
import com.kodilla.clinic.domain.Patient;
import com.kodilla.clinic.domain.StaffEvaluation;
import com.kodilla.clinic.dto.PatientDto;
import com.kodilla.clinic.enums.Stars;
import com.kodilla.clinic.enums.Status;
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
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PatientMapperTest {
    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private StaffEvaluationDao staffEvaluationDao;

    @Autowired
    private PatientMapper patientMapper;

    @Test
    public void testMapToPatient_AppointmentsAndEvaluations_Not_Assigned() {
        //Given
        PatientDto patientDto = new PatientDto(5,
                "Peter", "Smith", "Chopin 30 Street",
                LocalDate.of(1975, Month.AUGUST, 2),
                998877123, 111222333, "smith.j@one.com",
                false, new ArrayList<>(), new ArrayList<>());

        //When
        Patient patient = patientMapper.mapToPatient(patientDto);

        //Then
        assertEquals(Integer.valueOf(5), patient.getPatient_id());
        assertEquals("Peter", patient.getName());
        assertEquals("Smith", patient.getSurname());
        assertEquals("Chopin 30 Street", patient.getAddress());
        assertEquals(LocalDate.of(1975, Month.AUGUST, 2), patient.getBirthDate());
        assertEquals(998877123, patient.getPesel());
        assertEquals(111222333, patient.getTelNum());
        assertEquals("smith.j@one.com", patient.getEmail());
        assertFalse(patient.isInUrgency());
        assertEquals(0, patient.getAppointments().size());
        assertEquals(0, patient.getEvaluations().size());
    }

    @Test
    public void testMapToPatient_AppointmentsAndEvaluations_Assigned() {
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

        PatientDto patientDto = new PatientDto(5,
                "Peter", "Smith", "Chopin 30 Street",
                LocalDate.of(1975, Month.AUGUST, 2),
                998877123, 111222333, "smith.j@one.com",
                false, appointmentsIds, evaluationsIds);

        //When
        Patient patient = patientMapper.mapToPatient(patientDto);

        //Then
        assertEquals(Integer.valueOf(5), patient.getPatient_id());

        assertEquals(Integer.valueOf(appointment1Id), patient.getAppointments().get(0).getAppointment_id());
        assertEquals(Status.OPEN, patient.getAppointments().get(0).getStatus());
        assertEquals(Integer.valueOf(appointment2Id), patient.getAppointments().get(1).getAppointment_id());
        assertEquals(tomorrow, patient.getAppointments().get(1).getDateTime());

        assertEquals(Integer.valueOf(staffEvaluation1Id), patient.getEvaluations().get(0).getEvaluation_id());
        assertEquals(Stars.FIVE, patient.getEvaluations().get(0).getStars());
        assertEquals(Integer.valueOf(staffEvaluation2Id), patient.getEvaluations().get(1).getEvaluation_id());
        assertEquals("Was ok", patient.getEvaluations().get(1).getOpinion());
    }

    @Test
    public void testMapToPatientDto_AppointmentsAndEvaluations_Not_Assigned() {
        //Given
        Patient patient = new Patient(5,
                "Peter", "Smith", "Chopin 30 Street",
                LocalDate.of(1975, Month.AUGUST, 2),
                998877123, 111222333, "smith.j@one.com",
                false, new ArrayList<>(), new ArrayList<>());

        //When
        PatientDto patientDto = patientMapper.mapToPatientDto(patient);

        //Then
        assertEquals(Integer.valueOf(5), patientDto.getPatient_id());
        assertEquals("Peter", patientDto.getName());
        assertEquals("Smith", patientDto.getSurname());
        assertEquals("Chopin 30 Street", patientDto.getAddress());
        assertEquals(LocalDate.of(1975, Month.AUGUST, 2), patientDto.getBirthDate());
        assertEquals(998877123, patientDto.getPesel());
        assertEquals(111222333, patientDto.getTelNum());
        assertEquals("smith.j@one.com", patientDto.getEmail());
        assertFalse(patientDto.isInUrgency());
        assertEquals(0, patientDto.getAppointmentsIds().size());
        assertEquals(0, patientDto.getEvaluationsIds().size());
    }

    @Test
    public void testMapToPatientDto_AppointmentsAndEvaluations_Assigned() {
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

        Patient patient = new Patient(5,
                "Peter", "Smith", "Chopin 30 Street",
                LocalDate.of(1975, Month.AUGUST, 2),
                998877123, 111222333, "smith.j@one.com",
                false, appointments, evaluations);

        //When
        PatientDto patientDto = patientMapper.mapToPatientDto(patient);

        //Then
        assertEquals(Integer.valueOf(5), patientDto.getPatient_id());
        assertFalse(patientDto.isInUrgency());
        assertEquals(2, patientDto.getAppointmentsIds().size());
        assertEquals(2, patientDto.getEvaluationsIds().size());
    }

    @Test
    public void testMapToPatientDtoList_EmptyList() {
        //Given
        List<Patient> patientList = new ArrayList<>();

        //When
        List<PatientDto> patientDtoList = patientMapper.mapToPatientDtoList(patientList);

        //Then
        assertEquals(0, patientDtoList.size());
    }

    @Test
    public void testMapToPatientDtoList_WithPatients() {
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

        List<Patient> patientList = new ArrayList<>(Arrays.asList(patient1, patient2));

        //When
        List<PatientDto> patientDtoList = patientMapper.mapToPatientDtoList(patientList);

        //Then
        assertEquals(2, patientDtoList.size());
        assertEquals("smith.j@one.com", patientDtoList.get(0).getEmail());
        assertEquals(1111119999, patientDtoList.get(1).getPesel());
    }
}