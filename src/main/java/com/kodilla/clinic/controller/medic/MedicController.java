package com.kodilla.clinic.controller.medic;

import com.kodilla.clinic.enums.Gender;
import com.kodilla.clinic.outerapi.medic.recommendation.SpecialisationDto;
import com.kodilla.clinic.outerapi.medic.symptom.SymptomDto;
import com.kodilla.clinic.service.MedicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/medic/")
public class MedicController {
    @Autowired
    private MedicService service;

    @RequestMapping(method = RequestMethod.GET, value = "symptoms")
    public List<SymptomDto> getSymptoms() throws Exception {
        return service.getSymptoms();
    }

    @RequestMapping(method = RequestMethod.GET, value = "recommendations")
    public List<SpecialisationDto> getRecommendations(
            @RequestParam("birthYear") int birthYear,
            @RequestParam("gender") Gender gender,
            @RequestBody(required = false) int[] symptoms) throws Exception {
        return service.getSpecialisations(birthYear, gender, symptoms);
    }
}


/*    @RequestMapping(method = RequestMethod.GET, value = "emergencyHours/{hourId}")
    public EmergencyHourDto getEmergencyHour(@PathVariable Integer hourId) throws EmergencyHourNotFoundException {
        return mapper.mapToEmergencyHourDto(service.getEmergencyHour(hourId).orElseThrow(EmergencyHourNotFoundException::new));
    }

    @RequestMapping(method = RequestMethod.POST, value = "emergencyHours")
    public EmergencyHourDto addEmergencyHour(@RequestBody EmergencyHourDto emergencyHourDto) {
        return mapper.mapToEmergencyHourDto(service.saveEmergencyHour(mapper.mapToEmergencyHour(emergencyHourDto)));
    }*/

/*    public List<SpecialisationDto> getSpecialisations(int birthYear, Gender gender, List<Integer> symptomsIds) {
        return medicClient.getSpecialisations(birthYear, gender, symptomsIds);
    }*/