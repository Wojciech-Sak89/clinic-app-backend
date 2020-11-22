package com.kodilla.clinic.controller.schedule;

import com.kodilla.clinic.dto.schedule.EmergencyHourDto;
import com.kodilla.clinic.exception.schedule.EmergencyHourNotFoundException;
import com.kodilla.clinic.mapper.schedule.EmergencyHourMapper;
import com.kodilla.clinic.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1")
public class EmergencyHourController {
    @Autowired
    private EmergencyHourMapper mapper;

    @Autowired
    private DbService service;

    @RequestMapping(method = RequestMethod.GET, value = "emergencyHours")
    public List<EmergencyHourDto> getEmergencyHours() {
        return mapper.mapToEmergencyHourDtos(service.getAllEmergencyHours());
    }

    @RequestMapping(method = RequestMethod.GET, value = "emergencyHours/{hourId}")
    public EmergencyHourDto getEmergencyHour(@PathVariable Integer hourId) throws EmergencyHourNotFoundException {
        return mapper.mapToEmergencyHourDto(service.getEmergencyHour(hourId).orElseThrow(EmergencyHourNotFoundException::new));
    }

    @RequestMapping(method = RequestMethod.POST, value = "emergencyHours")
    public EmergencyHourDto addEmergencyHour(@RequestBody EmergencyHourDto emergencyHourDto) {
        return mapper.mapToEmergencyHourDto(service.saveEmergencyHour(mapper.mapToEmergencyHour(emergencyHourDto)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "emergencyHours")
    public EmergencyHourDto updateEmergencyHour(@RequestBody EmergencyHourDto emergencyHourDto) {
        return mapper.mapToEmergencyHourDto(service.saveEmergencyHour(mapper.mapToEmergencyHour(emergencyHourDto)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "emergencyHours/{hourId}")
    public void deleteEmergencyHour(@PathVariable Integer hourId) {
        service.deleteEmergencyHour(hourId);
    }
}
