package com.kodilla.clinic.dao.schedule;

import com.kodilla.clinic.enums.Day;
import com.kodilla.clinic.enums.Hour;
import com.kodilla.clinic.domain.schedule.ClinicDoctorSchedule;
import com.kodilla.clinic.domain.schedule.EmergencyHour;
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
public class EmergencyHourDaoTest {
    @Autowired
    private EmergencyHourDao emergencyHourDao;

    @Autowired
    private ClinicDoctorScheduleDao clinicDoctorScheduleDao;

    @Test
    public void testEmergencyHourDaoSave() {
        //Given
        EmergencyHour emergencyHour = new EmergencyHour(Day.FRIDAY, Hour.EIGHT_AM);

        //When
        emergencyHourDao.save(emergencyHour);

        //Then
        int id = emergencyHour.getEmergencyHour_id();

        Optional<EmergencyHour> emergencyHourDbRetrieved = emergencyHourDao.findById(id);
        assertTrue(emergencyHourDbRetrieved.isPresent());
        assertEquals(Day.FRIDAY, emergencyHourDbRetrieved.get().getDay());
        assertEquals(Hour.EIGHT_AM, emergencyHourDbRetrieved.get().getHour());
    }

    @Test
    public void testEmergencyHourDaoSave_With_DoctorSchedule() {
        //Given
        EmergencyHour emergencyHour = new EmergencyHour(Day.FRIDAY, Hour.FOUR_PM);

        ClinicDoctorSchedule clinicDoctorSchedule = new ClinicDoctorSchedule.Builder()
                .emergencyHour(emergencyHour)
                .build();

        emergencyHour.setSchedules(Collections.singletonList(clinicDoctorSchedule));

        //When
        emergencyHourDao.save(emergencyHour);

        //Then
        int scheduleId = clinicDoctorSchedule.getSchedule_id();
        int emergencyHour_id = emergencyHour.getEmergencyHour_id();

        Optional<EmergencyHour> emergencyHourDbRetrieved = emergencyHourDao.findById(emergencyHour_id);
        assertTrue(emergencyHourDbRetrieved.isPresent());

        assertEquals(Integer.valueOf(scheduleId),
                emergencyHourDbRetrieved.get().getSchedules().get(0).getSchedule_id());
    }
}