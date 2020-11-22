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
public class WorkingDayDto {
    private Integer workingDay_id;
    private Day day;
    private Hour startHour;
    private Hour endHour;
    private List<Integer> schedulesIds;
}
