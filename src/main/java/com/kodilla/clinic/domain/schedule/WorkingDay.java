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
@Table(name = "WORKING_DAYS")
public class WorkingDay {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "WORKING_DAY_ID", unique = true)
    private Integer workingDay_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "DAY")
    private Day day;

    @Enumerated(EnumType.STRING)
    @Column(name = "START_HOUR")
    private Hour startHour;

    @Enumerated(EnumType.STRING)
    @Column(name = "END_HOUR")
    private Hour endHour;

    //    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
//    @JoinColumn(name = "FK_SCHEDULE_ID")
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "workingDays")
    private List<ClinicDoctorSchedule> schedules;

    public WorkingDay(Day day, Hour startHour, Hour endHour) {
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public void setSchedules(List<ClinicDoctorSchedule> schedules) {
        this.schedules = schedules;
    }
}