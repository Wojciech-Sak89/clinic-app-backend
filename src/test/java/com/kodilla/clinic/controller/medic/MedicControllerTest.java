package com.kodilla.clinic.controller.medic;

import com.kodilla.clinic.enums.Gender;
import com.kodilla.clinic.outerapi.medic.recommendation.SpecialisationDto;
import com.kodilla.clinic.outerapi.medic.symptom.SymptomDto;
import com.kodilla.clinic.service.MedicService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MedicController.class)
public class MedicControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicService medicService;

    @Test
    public void shouldGetSymptoms() throws Exception {
        //Given
        SymptomDto symptomDto = new SymptomDto(5, "Cough");

        when(medicService.getSymptoms()).thenReturn(Collections.singletonList(symptomDto));

        //When & Then
        mockMvc.perform(get("/v1/medic/symptoms").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ID", is(5)))
                .andExpect(jsonPath("$[0].Name", is("Cough")));
    }

    @Test
    public void shouldGetRecommendations() throws Exception {
        //Given
        SpecialisationDto specialisationDto = new SpecialisationDto("Orthopedics", "93");

        List<Integer> symptomsIds = new ArrayList<>();
        symptomsIds.add(234);
        symptomsIds.add(112);

        when(medicService.getSpecialisations(1990, Gender.MALE, symptomsIds))
                .thenReturn(Collections.singletonList(specialisationDto));

        //When & Then
        mockMvc.perform(post("/v1/medic/recommendations")
                .contentType(MediaType.APPLICATION_JSON)
                .param("birthYear", "1990")
                .param("gender", String.valueOf(Gender.MALE))
                .content(String.valueOf(symptomsIds)))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].Name", is("Orthopedics")))
                .andExpect(jsonPath("$[0].Accuracy", is("93")));
    }
}