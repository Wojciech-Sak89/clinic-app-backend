package com.kodilla.clinic.domain.schedule;

import com.kodilla.clinic.enums.Day;
import com.kodilla.clinic.enums.Hour;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "EMERGENCY_HOURS")
public class EmergencyHour {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EMERGENCY_HOUR_ID", unique = true)
    private Integer emergencyHour_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "DAY")
    private Day day;

    @Enumerated(EnumType.STRING)
    @Column(name = "HOUR")
    private Hour hour;

    //    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
//    @JoinColumn(name = "FK_SCHEDULE_ID")
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "emergencyHours")
    private List<ClinicDoctorSchedule> schedules;

    public EmergencyHour(Day day, Hour hour) {
        this.day = day;
        this.hour = hour;
    }

    public void setSchedules(List<ClinicDoctorSchedule> schedules) {
        this.schedules = schedules;
    }
}