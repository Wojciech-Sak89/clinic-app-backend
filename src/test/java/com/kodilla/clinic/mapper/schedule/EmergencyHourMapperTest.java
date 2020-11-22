package com.kodilla.clinic.mapper.schedule;

import com.kodilla.clinic.dao.schedule.ClinicDoctorScheduleDao;
import com.kodilla.clinic.dto.schedule.EmergencyHourDto;
import com.kodilla.clinic.enums.Day;
import com.kodilla.clinic.enums.Hour;
import com.kodilla.clinic.domain.schedule.ClinicDoctorSchedule;
import com.kodilla.clinic.domain.schedule.EmergencyHour;
import com.kodilla.clinic.domain.schedule.factory.ScheduleFactory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EmergencyHourMapperTest {
    @Autowired
    private EmergencyHourMapper emergencyHourMapper;

    @Autowired
    private ScheduleFactory scheduleFactory;

    @Autowired
    private ClinicDoctorScheduleDao clinicDoctorScheduleDao;

    @Test
    public void testMapToEmergencyHour_ZeroSchedules() {
        //Given
        EmergencyHourDto emergencyHourDto = new EmergencyHourDto(70, Day.FRIDAY, Hour.SIX_PM,
                new ArrayList<>());

        //When
        EmergencyHour emergencyHour = emergencyHourMapper.mapToEmergencyHour(emergencyHourDto);

        //Then
        assertEquals(Integer.valueOf(70), emergencyHour.getEmergencyHour_id());
        assertEquals(Day.FRIDAY, emergencyHour.getDay());
        assertEquals(Hour.SIX_PM, emergencyHour.getHour());
        assertEquals(0, emergencyHour.getSchedules().size());
    }

    @Test
    public void testMapToEmergencyHour_WithSchedules() {
        //Given
        ClinicDoctorSchedule clinicDoctorSchedule =
                scheduleFactory.createSchedule(ScheduleFactory.THUR_FRI_EVENING_EMERGENCY);
        clinicDoctorScheduleDao.save(clinicDoctorSchedule);

        int scheduleId = clinicDoctorSchedule.getSchedule_id();

        EmergencyHourDto emergencyHourDto = new EmergencyHourDto(70, Day.FRIDAY, Hour.SIX_PM,
                Collections.singletonList(scheduleId));

        //When
        EmergencyHour emergencyHour = emergencyHourMapper.mapToEmergencyHour(emergencyHourDto);

        //Then
        assertEquals(Integer.valueOf(70), emergencyHour.getEmergencyHour_id());
        assertEquals(Day.FRIDAY, emergencyHour.getDay());
        assertEquals(Hour.SIX_PM, emergencyHour.getHour());
        assertEquals(1, emergencyHour.getSchedules().size());
        assertEquals(Integer.valueOf(scheduleId), emergencyHour.getSchedules().get(0).getSchedule_id());
    }

    @Test
    public void testMapToEmergencyHourDto_ZeroSchedules() {
        //Given
        EmergencyHour emergencyHour = new EmergencyHour(70, Day.FRIDAY, Hour.SIX_PM,
                new ArrayList<>());

        //When
        EmergencyHourDto emergencyHourDto = emergencyHourMapper.mapToEmergencyHourDto(emergencyHour);

        //Then
        assertEquals(Integer.valueOf(70), emergencyHourDto.getEmergencyHour_id());
        assertEquals(Day.FRIDAY, emergencyHourDto.getDay());
        assertEquals(Hour.SIX_PM, emergencyHourDto.getHour());
        assertEquals(0, emergencyHourDto.getSchedulesIds().size());
    }

    @Test
    public void testMapToEmergencyHourDto_WithSchedules() {
        //Given
        ClinicDoctorSchedule clinicDoctorSchedule =
                scheduleFactory.createSchedule(ScheduleFactory.THUR_FRI_EVENING_EMERGENCY);
        clinicDoctorScheduleDao.save(clinicDoctorSchedule);

        int scheduleId = clinicDoctorSchedule.getSchedule_id();

        EmergencyHour emergencyHour = new EmergencyHour(70, Day.FRIDAY, Hour.SIX_PM,
                Collections.singletonList(clinicDoctorSchedule));

        //When
        EmergencyHourDto emergencyHourDto = emergencyHourMapper.mapToEmergencyHourDto(emergencyHour);

        //Then
        assertEquals(Integer.valueOf(70), emergencyHourDto.getEmergencyHour_id());
        assertEquals(Day.FRIDAY, emergencyHourDto.getDay());
        assertEquals(Hour.SIX_PM, emergencyHourDto.getHour());
        assertEquals(1, emergencyHourDto.getSchedulesIds().size());
        assertEquals(Integer.valueOf(scheduleId), emergencyHourDto.getSchedulesIds().get(0));
    }
}