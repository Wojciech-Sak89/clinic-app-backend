package com.kodilla.clinic.dao.schedule;

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

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ClinicDoctorScheduleDaoTest {
    @Autowired
    private ClinicDoctorScheduleDao clinicDoctorScheduleDao;

    @Autowired
    private WorkingDayDao workingDayDao;

    @Autowired
    private EmergencyHourDao emergencyHourDao;

    @Autowired
    private ScheduleFactory scheduleFactory;

    @Test
    public void testClinicDoctorScheduleDaoSave() {
        //Given
        ClinicDoctorSchedule clinicDoctorSchedule = new ClinicDoctorSchedule.Builder().build();

        //When
        clinicDoctorScheduleDao.save(clinicDoctorSchedule);

        //Then
        int id = clinicDoctorSchedule.getSchedule_id();

        Optional<ClinicDoctorSchedule> scheduleDbRetrieved = clinicDoctorScheduleDao.findById(id);
        assertTrue(scheduleDbRetrieved.isPresent());
    }

    @Test
    public void testClinicDoctorScheduleDaoSave_With_WorkingDays_EmergencyHours() {
        //Given
        ClinicDoctorSchedule clinicDoctorSchedule =
                scheduleFactory.createSchedule(ScheduleFactory.THUR_FRI_EVENING_EMERGENCY);

        //When
        clinicDoctorScheduleDao.save(clinicDoctorSchedule);

        //Then
        int id = clinicDoctorSchedule.getSchedule_id();

        Optional<ClinicDoctorSchedule> scheduleDbRetrieved = clinicDoctorScheduleDao.findById(id);
        assertTrue(scheduleDbRetrieved.isPresent());
        assertEquals(Day.THURSDAY, scheduleDbRetrieved.get().getWorkingDays().get(0).getDay());
        assertEquals(Day.FRIDAY, scheduleDbRetrieved.get().getWorkingDays().get(1).getDay());
        assertEquals(Hour.TWELVE_PM, scheduleDbRetrieved.get().getWorkingDays().get(1).getStartHour());
        assertEquals(Day.FRIDAY, scheduleDbRetrieved.get().getEmergencyHours().get(1).getDay());
        assertEquals(Hour.SEVEN_THIRTY_PM, scheduleDbRetrieved.get().getEmergencyHours().get(1).getHour());
    }

    @Test
    public void testClinicDoctorScheduleDaoSave_With_WorkingDays_And_EmergencyHours_ManuallySet() {
        //Given
        WorkingDay workingDay1 = new WorkingDay(Day.MONDAY, Hour.EIGHT_AM, Hour.FIVE_PM);
        WorkingDay workingDay2 = new WorkingDay(Day.WEDNESDAY, Hour.NINE_THIRTY_AM, Hour.SIX_THIRTY_PM);
        workingDayDao.save(workingDay1);
        workingDayDao.save(workingDay2);

        EmergencyHour emergencyHour = new EmergencyHour(Day.FRIDAY, Hour.FOUR_PM);
        emergencyHourDao.save(emergencyHour);

        ClinicDoctorSchedule clinicDoctorSchedule = new ClinicDoctorSchedule.Builder()
                .workingDay(workingDay1)
                .workingDay(workingDay2)
                .emergencyHour(emergencyHour)
                .build();
        workingDay1.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        workingDay2.setSchedules(Collections.singletonList(clinicDoctorSchedule));
        emergencyHour.setSchedules(Collections.singletonList(clinicDoctorSchedule));

        //When
        clinicDoctorScheduleDao.save(clinicDoctorSchedule);

        //Then
        int scheduleId = clinicDoctorSchedule.getSchedule_id();
        int emergencyHour_id = emergencyHour.getEmergencyHour_id();
        int workingDay1_id = workingDay1.getWorkingDay_id();

        Optional<ClinicDoctorSchedule> scheduleDbRetrieved = clinicDoctorScheduleDao.findById(scheduleId);
        assertTrue(scheduleDbRetrieved.isPresent());

        assertEquals(Integer.valueOf(workingDay1_id),
                scheduleDbRetrieved.get().getWorkingDays().get(0).getWorkingDay_id());
        assertEquals(Day.MONDAY, scheduleDbRetrieved.get().getWorkingDays().get(0).getDay());
        assertEquals(Hour.EIGHT_AM, scheduleDbRetrieved.get().getWorkingDays().get(0).getStartHour());
        assertEquals(Hour.FIVE_PM, scheduleDbRetrieved.get().getWorkingDays().get(0).getEndHour());

        assertEquals(Day.WEDNESDAY, scheduleDbRetrieved.get().getWorkingDays().get(1).getDay());
        assertEquals(Hour.NINE_THIRTY_AM, scheduleDbRetrieved.get().getWorkingDays().get(1).getStartHour());
        assertEquals(Hour.SIX_THIRTY_PM, scheduleDbRetrieved.get().getWorkingDays().get(1).getEndHour());

        assertEquals(Integer.valueOf(emergencyHour_id),
                scheduleDbRetrieved.get().getEmergencyHours().get(0).getEmergencyHour_id());
        assertEquals(Day.FRIDAY, scheduleDbRetrieved.get().getEmergencyHours().get(0).getDay());
        assertEquals(Hour.FOUR_PM, scheduleDbRetrieved.get().getEmergencyHours().get(0).getHour());
    }
}