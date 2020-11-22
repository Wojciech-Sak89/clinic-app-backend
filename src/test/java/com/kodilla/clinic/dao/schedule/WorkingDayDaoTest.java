package com.kodilla.clinic.dao.schedule;

import com.kodilla.clinic.enums.Day;
import com.kodilla.clinic.enums.Hour;
import com.kodilla.clinic.domain.schedule.ClinicDoctorSchedule;
import com.kodilla.clinic.domain.schedule.WorkingDay;
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
public class WorkingDayDaoTest {
    @Autowired
    private WorkingDayDao workingDayDao;

    @Test
    public void testWorkingDayDaoSave() {
        //Given
        WorkingDay workingDay = new WorkingDay(Day.FRIDAY, Hour.SIX_PM, Hour.SEVEN_PM);

        //When
        workingDayDao.save(workingDay);

        //Then
        int id = workingDay.getWorkingDay_id();

        Optional<WorkingDay> workingDayDbRetrieved = workingDayDao.findById(id);
        assertTrue(workingDayDbRetrieved.isPresent());
        assertEquals(Day.FRIDAY, workingDayDbRetrieved.get().getDay());
        assertEquals(Hour.SIX_PM, workingDayDbRetrieved.get().getStartHour());
        assertEquals(Hour.SEVEN_PM, workingDayDbRetrieved.get().getEndHour());
    }

    @Test
    public void testWorkingDayDaoSave_With_DoctorSchedule() {
        //Given
        WorkingDay workingDay = new WorkingDay(Day.FRIDAY, Hour.FOUR_PM, Hour.SIX_THIRTY_PM);

        ClinicDoctorSchedule clinicDoctorSchedule = new ClinicDoctorSchedule.Builder()
                .workingDay(workingDay)
                .build();

        workingDay.setSchedules(Collections.singletonList(clinicDoctorSchedule));

        //When
        workingDayDao.save(workingDay);

        //Then
        int scheduleId = clinicDoctorSchedule.getSchedule_id();
        int workingDay_id = workingDay.getWorkingDay_id();

        Optional<WorkingDay> workingDayDbRetrieved = workingDayDao.findById(workingDay_id);
        assertTrue(workingDayDbRetrieved.isPresent());

        assertEquals(Integer.valueOf(scheduleId),
                workingDayDbRetrieved.get().getSchedules().get(0).getSchedule_id());
    }
}