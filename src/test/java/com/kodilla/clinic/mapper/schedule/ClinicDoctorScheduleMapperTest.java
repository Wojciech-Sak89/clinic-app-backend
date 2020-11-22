package com.kodilla.clinic.mapper.schedule;

import com.kodilla.clinic.dao.schedule.EmergencyHourDao;
import com.kodilla.clinic.dao.schedule.WorkingDayDao;
import com.kodilla.clinic.dto.schedule.ClinicDoctorScheduleDto;
import com.kodilla.clinic.dto.schedule.EmergencyHourDto;
import com.kodilla.clinic.dto.schedule.WorkingDayDto;
import com.kodilla.clinic.enums.Day;
import com.kodilla.clinic.enums.Hour;
import com.kodilla.clinic.domain.schedule.ClinicDoctorSchedule;
import com.kodilla.clinic.domain.schedule.EmergencyHour;
import com.kodilla.clinic.domain.schedule.WorkingDay;
import com.kodilla.clinic.domain.schedule.factory.ScheduleFactory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ClinicDoctorScheduleMapperTest {
    @Autowired
    private ClinicDoctorScheduleMapper clinicDoctorScheduleMapper;

    @Autowired
    private ScheduleFactory scheduleFactory;

    @Autowired
    private EmergencyHourDao emergencyHourDao;

    @Autowired
    private WorkingDayDao workingDayDao;

    @Test
    public void testMapToClinicDoctorSchedule_Zero_WorkingDays_EmergencyHours() {
        //Given
        ClinicDoctorScheduleDto clinicDoctorScheduleDto =
                new ClinicDoctorScheduleDto(44, new ArrayList<>(), new ArrayList<>());

        //When
        ClinicDoctorSchedule clinicDoctorSchedule =
                clinicDoctorScheduleMapper.mapToClinicDoctorSchedule(clinicDoctorScheduleDto);

        //Then
        assertEquals(Integer.valueOf(44), clinicDoctorSchedule.getSchedule_id());
        assertEquals(0, clinicDoctorSchedule.getWorkingDays().size());
        assertEquals(0, clinicDoctorSchedule.getEmergencyHours().size());
    }

    @Test
    public void testMapToClinicDoctorSchedule_With_WorkingDays_EmergencyHours() {
        //Given
        WorkingDayDto workingDayDto = new WorkingDayDto(70, Day.FRIDAY, Hour.SIX_PM, Hour.SEVEN_PM,
                new ArrayList<>());
        EmergencyHourDto emergencyHourDto = new EmergencyHourDto(75, Day.FRIDAY, Hour.SIX_PM,
                new ArrayList<>());

        ClinicDoctorScheduleDto clinicDoctorScheduleDto =
                new ClinicDoctorScheduleDto(44,
                        Collections.singletonList(workingDayDto), Collections.singletonList(emergencyHourDto));

        //When
        ClinicDoctorSchedule clinicDoctorSchedule =
                clinicDoctorScheduleMapper.mapToClinicDoctorSchedule(clinicDoctorScheduleDto);

        //Then
        assertEquals(Integer.valueOf(44), clinicDoctorSchedule.getSchedule_id());
        assertEquals(1, clinicDoctorSchedule.getWorkingDays().size());
        assertEquals(Integer.valueOf(70), clinicDoctorSchedule.getWorkingDays().get(0).getWorkingDay_id());
        assertEquals(1, clinicDoctorSchedule.getEmergencyHours().size());
        assertEquals(Integer.valueOf(75), clinicDoctorSchedule.getEmergencyHours().get(0).getEmergencyHour_id());
    }

    @Test
    public void testMapToClinicDoctorScheduleDto_Zero_WorkingDays_EmergencyHours() {
        //Given
        ClinicDoctorSchedule clinicDoctorSchedule =
                new ClinicDoctorSchedule(44, new ArrayList<>(), new ArrayList<>());

        //When
        ClinicDoctorScheduleDto clinicDoctorScheduleDto =
                clinicDoctorScheduleMapper.mapToClinicDoctorScheduleDto(clinicDoctorSchedule);

        //Then
        assertEquals(Integer.valueOf(44), clinicDoctorScheduleDto.getSchedule_id());
        assertEquals(0, clinicDoctorScheduleDto.getWorkingDaysDtos().size());
        assertEquals(0, clinicDoctorScheduleDto.getEmergencyHoursDtos().size());
    }

    @Test
    public void testMapToClinicDoctorScheduleDto_With_WorkingDays_EmergencyHours() {
        //Given
        WorkingDay workingDay = new WorkingDay(70, Day.FRIDAY, Hour.SIX_PM, Hour.SEVEN_PM,
                new ArrayList<>());
        EmergencyHour emergencyHour = new EmergencyHour(75, Day.FRIDAY, Hour.SIX_PM,
                new ArrayList<>());

        ClinicDoctorSchedule clinicDoctorSchedule =
                new ClinicDoctorSchedule(44,
                        Collections.singletonList(workingDay), Collections.singletonList(emergencyHour));

        //When
        ClinicDoctorScheduleDto clinicDoctorScheduleDto =
                clinicDoctorScheduleMapper.mapToClinicDoctorScheduleDto(clinicDoctorSchedule);

        //Then
        assertEquals(Integer.valueOf(44), clinicDoctorScheduleDto.getSchedule_id());
        assertEquals(1, clinicDoctorScheduleDto.getWorkingDaysDtos().size());
        assertEquals(Integer.valueOf(70), clinicDoctorScheduleDto.getWorkingDaysDtos().get(0).getWorkingDay_id());
        assertEquals(1, clinicDoctorScheduleDto.getEmergencyHoursDtos().size());
        assertEquals(Integer.valueOf(75), clinicDoctorScheduleDto.getEmergencyHoursDtos().get(0).getEmergencyHour_id());
    }
}