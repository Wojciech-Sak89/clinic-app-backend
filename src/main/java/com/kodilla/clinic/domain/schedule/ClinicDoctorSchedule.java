package com.kodilla.clinic.domain.schedule;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CLINIC_DOCTORS_SCHEDULES")
public class ClinicDoctorSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CLINIC_DOCTOR_SCHEDULE_ID", unique = true)
    private Integer schedule_id;

    //    @OneToMany(
//            targetEntity = WorkingDay.class,
//            mappedBy = "schedule",
//            cascade = CascadeType.MERGE,
//            fetch = FetchType.LAZY
//    )
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "JOIN_SCHEDULE__WORKING_DAY",
            joinColumns = {@JoinColumn(name = "SCHEDULE_ID", referencedColumnName = "CLINIC_DOCTOR_SCHEDULE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "WORKING_DAY_ID", referencedColumnName = "WORKING_DAY_ID")}
    )
    private List<WorkingDay> workingDays = new ArrayList<>();

    //    @OneToMany(
//            targetEntity = EmergencyHour.class,
//            mappedBy = "schedule",
//            cascade = CascadeType.MERGE,
//            fetch = FetchType.LAZY
//    )
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "JOIN_SCHEDULE__EMERGENCY_HOUR",
            joinColumns = {@JoinColumn(name = "SCHEDULE_ID", referencedColumnName = "CLINIC_DOCTOR_SCHEDULE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "EMERGENCY_HOUR_ID", referencedColumnName = "EMERGENCY_HOUR_ID")}
    )
    private List<EmergencyHour> emergencyHours = new ArrayList<>();

    public static class Builder {
        private final List<WorkingDay> workingDays = new ArrayList<>();
        private final List<EmergencyHour> emergencyHours = new ArrayList<>();

        public Builder workingDay(WorkingDay workingDay) {
            workingDays.add(workingDay);
            return this;
        }

        public Builder emergencyHour(EmergencyHour emergencyHour) {
            emergencyHours.add(emergencyHour);
            return this;
        }

        public ClinicDoctorSchedule build() {
            return new ClinicDoctorSchedule(workingDays, emergencyHours);
        }
    }

    protected ClinicDoctorSchedule() {
    }

    public ClinicDoctorSchedule(Integer schedule_id, List<WorkingDay> workingDays, List<EmergencyHour> emergencyHours) {
        this.schedule_id = schedule_id;
        this.workingDays = workingDays;
        this.emergencyHours = emergencyHours;
    }

    private ClinicDoctorSchedule(List<WorkingDay> workingDays,
                                 List<EmergencyHour> emergencyHours) {
        this.workingDays = workingDays;
        this.emergencyHours = emergencyHours;
    }

    public Integer getSchedule_id() {
        return schedule_id;
    }

    public List<WorkingDay> getWorkingDays() {
        return workingDays;
    }

    public List<EmergencyHour> getEmergencyHours() {
        return emergencyHours;
    }
}
