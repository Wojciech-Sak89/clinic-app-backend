package com.kodilla.clinic.domain.schedule.factory;

import com.kodilla.clinic.enums.Day;
import com.kodilla.clinic.enums.Hour;
import com.kodilla.clinic.domain.schedule.ClinicDoctorSchedule;
import com.kodilla.clinic.domain.schedule.EmergencyHour;
import com.kodilla.clinic.domain.schedule.WorkingDay;
import org.springframework.stereotype.Component;

@Component
public class ScheduleFactory {
    public static final String FULLTIME_FROM_MORNING_WITH_STANDARD_EMERGENCY =
            "FULLTIME_FROM_MORNING_WITH_STANDARD_EMERGENCY";
    public static final String HALFTIME_EVERYDAY_FROM_MORNING_WITH_STANDARD_EMERGENCY =
            "HALFTIME_EVERYDAY_FROM_MORNING_WITH_STANDARD_EMERGENCY";
    public static final String MON_WED_FRI_NO_EMERGENCY = "MON_WED_FRI_NO_EMERGENCY";
    public static final String THUR_FRI_EVENING_EMERGENCY = "THUR_FRI_EVENING_EMERGENCY";

    public final ClinicDoctorSchedule createSchedule(final String scheduleType) {
        switch (scheduleType) {
            case FULLTIME_FROM_MORNING_WITH_STANDARD_EMERGENCY:
                return new ClinicDoctorSchedule.Builder()
                        .workingDay(new WorkingDay(Day.MONDAY, Hour.EIGHT_AM, Hour.THREE_THIRTY_PM))
                        .workingDay(new WorkingDay(Day.TUESDAY, Hour.EIGHT_AM, Hour.THREE_THIRTY_PM))
                        .workingDay(new WorkingDay(Day.WEDNESDAY, Hour.EIGHT_AM, Hour.THREE_THIRTY_PM))
                        .workingDay(new WorkingDay(Day.THURSDAY, Hour.EIGHT_AM, Hour.THREE_THIRTY_PM))
                        .workingDay(new WorkingDay(Day.FRIDAY, Hour.EIGHT_AM, Hour.THREE_THIRTY_PM))
                        .emergencyHour(new EmergencyHour(Day.MONDAY, Hour.TWELVE_PM))
                        .emergencyHour(new EmergencyHour(Day.TUESDAY, Hour.TWELVE_PM))
                        .emergencyHour(new EmergencyHour(Day.WEDNESDAY, Hour.TWELVE_PM))
                        .emergencyHour(new EmergencyHour(Day.THURSDAY, Hour.TWELVE_PM))
                        .emergencyHour(new EmergencyHour(Day.FRIDAY, Hour.TWELVE_PM))
                        .build();
            case HALFTIME_EVERYDAY_FROM_MORNING_WITH_STANDARD_EMERGENCY:
                return new ClinicDoctorSchedule.Builder()
                        .workingDay(new WorkingDay(Day.MONDAY, Hour.EIGHT_AM, Hour.ELEVEN_THIRTY_AM))
                        .workingDay(new WorkingDay(Day.TUESDAY, Hour.EIGHT_AM, Hour.ELEVEN_THIRTY_AM))
                        .workingDay(new WorkingDay(Day.WEDNESDAY, Hour.EIGHT_AM, Hour.ELEVEN_THIRTY_AM))
                        .workingDay(new WorkingDay(Day.THURSDAY, Hour.EIGHT_AM, Hour.ELEVEN_THIRTY_AM))
                        .workingDay(new WorkingDay(Day.FRIDAY, Hour.EIGHT_AM, Hour.ELEVEN_THIRTY_AM))
                        .emergencyHour(new EmergencyHour(Day.MONDAY, Hour.TWELVE_PM))
                        .emergencyHour(new EmergencyHour(Day.TUESDAY, Hour.TWELVE_PM))
                        .emergencyHour(new EmergencyHour(Day.WEDNESDAY, Hour.TWELVE_PM))
                        .emergencyHour(new EmergencyHour(Day.THURSDAY, Hour.TWELVE_PM))
                        .emergencyHour(new EmergencyHour(Day.FRIDAY, Hour.TWELVE_PM))
                        .build();
            case MON_WED_FRI_NO_EMERGENCY:
                return new ClinicDoctorSchedule.Builder()
                        .workingDay(new WorkingDay(Day.MONDAY, Hour.EIGHT_AM, Hour.THREE_THIRTY_PM))
                        .workingDay(new WorkingDay(Day.WEDNESDAY, Hour.EIGHT_AM, Hour.THREE_THIRTY_PM))
                        .workingDay(new WorkingDay(Day.FRIDAY, Hour.EIGHT_AM, Hour.THREE_THIRTY_PM))
                        .build();
            case THUR_FRI_EVENING_EMERGENCY:
                return new ClinicDoctorSchedule.Builder()
                        .workingDay(new WorkingDay(Day.THURSDAY, Hour.TWELVE_PM, Hour.SEVEN_THIRTY_PM))
                        .workingDay(new WorkingDay(Day.FRIDAY, Hour.TWELVE_PM, Hour.SEVEN_THIRTY_PM))
                        .emergencyHour(new EmergencyHour(Day.THURSDAY, Hour.SEVEN_THIRTY_PM))
                        .emergencyHour(new EmergencyHour(Day.FRIDAY, Hour.SEVEN_THIRTY_PM))
                        .build();
            default:
                return null;
        }
    }
}