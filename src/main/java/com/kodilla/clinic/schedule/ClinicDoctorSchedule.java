package com.kodilla.clinic.schedule;


import com.kodilla.clinic.enums.Day;
import com.kodilla.clinic.enums.Hour;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.*;

@Embeddable
public final class ClinicDoctorSchedule {
    @ElementCollection
    private List<WorkingDay> workingDays = new ArrayList<>();
    @ElementCollection
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

    private ClinicDoctorSchedule(List<WorkingDay> workingDays,
                                List<EmergencyHour> emergencyHours) {
        this.workingDays = workingDays;
        this.emergencyHours = emergencyHours;
    }

    public List<WorkingDay> getWorkingDays() {
        return workingDays;
    }

    public List<EmergencyHour> getEmergencyHours() {
        return emergencyHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClinicDoctorSchedule that = (ClinicDoctorSchedule) o;

        if (!workingDays.equals(that.workingDays)) return false;
        return Objects.equals(emergencyHours, that.emergencyHours);
    }

    @Override
    public int hashCode() {
        int result = workingDays.hashCode();
        result = 31 * result + (emergencyHours != null ? emergencyHours.hashCode() : 0);
        return result;
    }
}
