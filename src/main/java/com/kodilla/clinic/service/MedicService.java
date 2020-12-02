package com.kodilla.clinic.service;

import com.kodilla.clinic.enums.Gender;
import com.kodilla.clinic.outerapi.medic.client.MedicClient;
import com.kodilla.clinic.outerapi.medic.recommendation.SpecialisationDto;
import com.kodilla.clinic.outerapi.medic.symptom.SymptomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicService {
    @Autowired
    private MedicClient medicClient;

    public List<SymptomDto> getSymptoms() throws Exception {
        return medicClient.getSymptoms();
    }

    public List<SpecialisationDto> getSpecialisations(int birthYear, Gender gender, List<Integer> symptomsIds) throws Exception {
        return medicClient.getSpecialisations(birthYear, gender, symptomsIds);
    }
}
