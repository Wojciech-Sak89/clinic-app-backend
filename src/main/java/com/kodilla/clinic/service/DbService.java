package com.kodilla.clinic.service;

import com.kodilla.clinic.dao.AppointmentDao;
import com.kodilla.clinic.dao.DoctorDao;
import com.kodilla.clinic.dao.PatientDao;
import com.kodilla.clinic.dao.StaffEvaluationDao;
import com.kodilla.clinic.domain.Appointment;
import com.kodilla.clinic.domain.Doctor;
import com.kodilla.clinic.domain.Patient;
import com.kodilla.clinic.domain.StaffEvaluation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DbService {
    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private StaffEvaluationDao staffEvaluationDao;

    public List<Patient> getAllPatients() {
        return patientDao.findAll();
    }

    public Optional<Patient> getPatient(Integer id) {
        return patientDao.findById(id);
    }

    public Patient savePatient(Patient patient) {
        return patientDao.save(patient);
    }

    public void deletePatient(Integer id) {
        patientDao.deleteById(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorDao.findAll();
    }

    public Optional<Doctor> getDoctor(Integer id) {
        return doctorDao.findById(id);
    }

    public Doctor saveDoctor(Doctor doctor) {
        return doctorDao.save(doctor);
    }

    public void deleteDoctor(Integer id) {
        doctorDao.deleteById(id);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentDao.findAll();
    }

    public Optional<Appointment> getAppointment(Integer id) {
        return appointmentDao.findById(id);
    }

    public Appointment saveAppointment(Appointment appointment) {
        return appointmentDao.save(appointment);
    }

    public void deleteAppointment(Integer id) {
        appointmentDao.deleteById(id);
    }

    public List<StaffEvaluation> getAllStaffEvaluations() {
        return staffEvaluationDao.findAll();
    }

    public Optional<StaffEvaluation> getStaffEvaluation(Integer id) {
        return staffEvaluationDao.findById(id);
    }

    public StaffEvaluation saveStaffEvaluation(StaffEvaluation staffEvaluation) {
        return staffEvaluationDao.save(staffEvaluation);
    }

    public void deleteStaffEvaluation(Integer id) {
        staffEvaluationDao.deleteById(id);
    }

//    public List<Appointment> getPatientsForthcomingAppointments(Integer patientId) {
////        appointmentDao.
//    }
}
