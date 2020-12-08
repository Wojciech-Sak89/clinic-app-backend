package com.kodilla.clinic.controller.schedule;

import com.kodilla.clinic.dto.schedule.WorkingDayDto;
import com.kodilla.clinic.exception.schedule.WorkingDayNotFoundException;
import com.kodilla.clinic.mapper.schedule.WorkingDayMapper;
import com.kodilla.clinic.service.DbService;
import com.kodilla.clinic.service.schedule.ScheduleDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1")
public class WorkingDayController {
    @Autowired
    private WorkingDayMapper mapper;

    @Autowired
    private ScheduleDbService scheduleDbService;

    @RequestMapping(method = RequestMethod.GET, value = "workingDays")
    public List<WorkingDayDto> getWorkingDays() {
        return mapper.mapToWorkingDaysDtos(scheduleDbService.getAllWorkingDays());
    }

    @RequestMapping(method = RequestMethod.GET, value = "workingDays/{dayId}")
    public WorkingDayDto getWorkingDay(@PathVariable Integer dayId) throws WorkingDayNotFoundException {
        return mapper.mapToWorkingDayDto(scheduleDbService.getWorkingDay(dayId).orElseThrow(WorkingDayNotFoundException::new));
    }

    @RequestMapping(method = RequestMethod.POST, value = "workingDays")
    public WorkingDayDto addWorkingDay(@RequestBody WorkingDayDto workingDayDto) {
        return mapper.mapToWorkingDayDto(scheduleDbService.saveWorkingDay(mapper.mapToWorkingDay(workingDayDto)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "workingDays")
    public WorkingDayDto updateWorkingDay(@RequestBody WorkingDayDto workingDayDto) {
        return mapper.mapToWorkingDayDto(scheduleDbService.saveWorkingDay(mapper.mapToWorkingDay(workingDayDto)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "workingDays/{dayId}")
    public void deleteWorkingDay(@PathVariable Integer dayId) {
        scheduleDbService.deleteWorkingDay(dayId);
    }
}
