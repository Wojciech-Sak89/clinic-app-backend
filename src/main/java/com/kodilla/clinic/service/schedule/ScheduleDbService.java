package com.kodilla.clinic.service.schedule;

import com.kodilla.clinic.dao.schedule.ClinicDoctorScheduleDao;
import com.kodilla.clinic.dao.schedule.EmergencyHourDao;
import com.kodilla.clinic.dao.schedule.WorkingDayDao;
import com.kodilla.clinic.domain.schedule.ClinicDoctorSchedule;
import com.kodilla.clinic.domain.schedule.EmergencyHour;
import com.kodilla.clinic.domain.schedule.WorkingDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleDbService {
    @Autowired
    private WorkingDayDao workingDayDao;

    @Autowired
    private EmergencyHourDao emergencyHourDao;

    @Autowired
    private ClinicDoctorScheduleDao clinicDoctorScheduleDao;

    public List<WorkingDay> getAllWorkingDays() {
        return workingDayDao.findAll();
    }

    public Optional<WorkingDay> getWorkingDay(Integer id) {
        return workingDayDao.findById(id);
    }

    public WorkingDay saveWorkingDay(WorkingDay workingDay) {
        return workingDayDao.save(workingDay);
    }

    public void deleteWorkingDay(Integer id) {
        workingDayDao.deleteById(id);
    }

    public List<EmergencyHour> getAllEmergencyHours() {
        return emergencyHourDao.findAll();
    }

    public Optional<EmergencyHour> getEmergencyHour(Integer id) {
        return emergencyHourDao.findById(id);
    }

    public EmergencyHour saveEmergencyHour(EmergencyHour emergencyHour) {
        return emergencyHourDao.save(emergencyHour);
    }

    public void deleteEmergencyHour(Integer id) {
        emergencyHourDao.deleteById(id);
    }

    public List<ClinicDoctorSchedule> getAllClinicDoctorSchedules() {
        return clinicDoctorScheduleDao.findAll();
    }

    public Optional<ClinicDoctorSchedule> getClinicDoctorSchedule(Integer id) {
        return clinicDoctorScheduleDao.findById(id);
    }

    public ClinicDoctorSchedule saveClinicDoctorSchedule(ClinicDoctorSchedule clinicDoctorSchedule) {
        return clinicDoctorScheduleDao.save(clinicDoctorSchedule);
    }

    public void deleteClinicDoctorSchedule(Integer id) {
        clinicDoctorScheduleDao.deleteById(id);
    }
}
