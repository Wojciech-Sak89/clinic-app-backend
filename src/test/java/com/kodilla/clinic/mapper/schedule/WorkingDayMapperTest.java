package com.kodilla.clinic.mapper.schedule;

import com.kodilla.clinic.dao.schedule.ClinicDoctorScheduleDao;
import com.kodilla.clinic.dto.schedule.WorkingDayDto;
import com.kodilla.clinic.enums.Day;
import com.kodilla.clinic.enums.Hour;
import com.kodilla.clinic.domain.schedule.ClinicDoctorSchedule;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WorkingDayMapperTest {
    @Autowired
    private WorkingDayMapper workingDayMapper;

    @Autowired
    private ScheduleFactory scheduleFactory;

    @Autowired
    private ClinicDoctorScheduleDao clinicDoctorScheduleDao;

    @Test
    public void testMapToWorkingDay_ZeroSchedules() {
        //Given
        WorkingDayDto workingDayDto = new WorkingDayDto(70, Day.FRIDAY, Hour.SIX_PM, Hour.SEVEN_PM,
                new ArrayList<>());

        //When
        WorkingDay workingDay = workingDayMapper.mapToWorkingDay(workingDayDto);

        //Then
        assertEquals(Integer.valueOf(70), workingDay.getWorkingDay_id());
        assertEquals(Day.FRIDAY, workingDay.getDay());
        assertEquals(Hour.SIX_PM, workingDay.getStartHour());
        assertEquals(Hour.SEVEN_PM, workingDay.getEndHour());
        assertEquals(0, workingDay.getSchedules().size());
    }

    @Test
    public void testMapToWorkingDay_WithSchedules() {
        //Given
        ClinicDoctorSchedule clinicDoctorSchedule =
                scheduleFactory.createSchedule(ScheduleFactory.THUR_FRI_EVENING_EMERGENCY);
        clinicDoctorScheduleDao.save(clinicDoctorSchedule);

        int scheduleId = clinicDoctorSchedule.getSchedule_id();

        WorkingDayDto workingDayDto = new WorkingDayDto(70, Day.FRIDAY, Hour.SIX_PM, Hour.SEVEN_PM,
                Collections.singletonList(scheduleId));

        //When
        WorkingDay workingDay = workingDayMapper.mapToWorkingDay(workingDayDto);

        //Then
        assertEquals(Integer.valueOf(70), workingDay.getWorkingDay_id());
        assertEquals(Day.FRIDAY, workingDay.getDay());
        assertEquals(Hour.SIX_PM, workingDay.getStartHour());
        assertEquals(Hour.SEVEN_PM, workingDay.getEndHour());
        assertEquals(1, workingDay.getSchedules().size());
    }

    @Test
    public void testMapToWorkingDayDto_ZeroSchedules() {
        //Given
        WorkingDay workingDay = new WorkingDay(70, Day.FRIDAY, Hour.SIX_PM, Hour.SEVEN_PM,
                new ArrayList<>());

        //When
        WorkingDayDto workingDayDto = workingDayMapper.mapToWorkingDayDto(workingDay);

        //Then
        assertEquals(Integer.valueOf(70), workingDayDto.getWorkingDay_id());
        assertEquals(Day.FRIDAY, workingDayDto.getDay());
        assertEquals(Hour.SIX_PM, workingDayDto.getStartHour());
        assertEquals(Hour.SEVEN_PM, workingDayDto.getEndHour());
        assertEquals(0, workingDayDto.getSchedulesIds().size());
    }

    @Test
    public void testMapToWorkingDayDto_WithSchedules() {
        //Given
        ClinicDoctorSchedule clinicDoctorSchedule =
                scheduleFactory.createSchedule(ScheduleFactory.THUR_FRI_EVENING_EMERGENCY);
        clinicDoctorScheduleDao.save(clinicDoctorSchedule);

        int scheduleId = clinicDoctorSchedule.getSchedule_id();

        WorkingDay workingDay = new WorkingDay(70, Day.FRIDAY, Hour.SIX_PM, Hour.SEVEN_PM,
                Collections.singletonList(clinicDoctorSchedule));

        //When
        WorkingDayDto workingDayDto = workingDayMapper.mapToWorkingDayDto(workingDay);

        //Then
        assertEquals(Integer.valueOf(70), workingDayDto.getWorkingDay_id());
        assertEquals(Day.FRIDAY, workingDayDto.getDay());
        assertEquals(Hour.SIX_PM, workingDayDto.getStartHour());
        assertEquals(Hour.SEVEN_PM, workingDayDto.getEndHour());
        assertEquals(1, workingDayDto.getSchedulesIds().size());
        assertEquals(Integer.valueOf(scheduleId), workingDayDto.getSchedulesIds().get(0));
    }
}