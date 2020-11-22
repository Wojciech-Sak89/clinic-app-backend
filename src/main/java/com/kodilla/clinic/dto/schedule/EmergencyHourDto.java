package com.kodilla.clinic.dto.schedule;

import com.kodilla.clinic.enums.Day;
import com.kodilla.clinic.enums.Hour;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmergencyHourDto {
    private Integer emergencyHour_id;
    private Day day;
    private Hour hour;
    private List<Integer> schedulesIds;
}
