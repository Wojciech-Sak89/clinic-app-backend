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

    @RequestMapping(method = RequestMethod.POST, value = "recommendations")
    public List<SpecialisationDto> getRecommendations(
            @RequestParam("birthYear") int birthYear,
            @RequestParam("gender") Gender gender,
            @RequestBody List<Integer> symptoms) throws Exception {
        return service.getSpecialisations(birthYear, gender, symptoms);
    }
}