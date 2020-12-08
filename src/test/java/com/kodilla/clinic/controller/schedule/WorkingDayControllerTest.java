package com.kodilla.clinic.controller.schedule;

import com.google.gson.Gson;
import com.kodilla.clinic.domain.schedule.WorkingDay;
import com.kodilla.clinic.dto.schedule.WorkingDayDto;
import com.kodilla.clinic.enums.Day;
import com.kodilla.clinic.enums.Hour;
import com.kodilla.clinic.mapper.schedule.WorkingDayMapper;
import com.kodilla.clinic.service.DbService;
import com.kodilla.clinic.service.schedule.ScheduleDbService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WorkingDayController.class)
public class WorkingDayControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleDbService scheduleDbService;

    @MockBean
    private WorkingDayMapper mapper;

    @Test
    public void shouldGetEmptyWorkingDayList() throws Exception {
        //Given
        List<WorkingDay> workingDayList = new ArrayList<>();
        when(scheduleDbService.getAllWorkingDays()).thenReturn(workingDayList);
        when(mapper.mapToWorkingDaysDtos(workingDayList)).thenReturn(new ArrayList<>());

        //When & Then
        mockMvc.perform(get("/v1/workingDays").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldGetWorkingDayList() throws Exception {
        //Given
        List<WorkingDay> workingDayList = new ArrayList<>();
        workingDayList.add(new WorkingDay(15, Day.FRIDAY, Hour.EIGHT_AM, Hour.ELEVEN_AM, new ArrayList<>()));

        List<WorkingDayDto> workingDayDtos = new ArrayList<>();
        workingDayDtos.add(new WorkingDayDto(15, Day.FRIDAY, Hour.EIGHT_AM, Hour.ELEVEN_AM, new ArrayList<>()));

        when(scheduleDbService.getAllWorkingDays()).thenReturn(workingDayList);
        when(mapper.mapToWorkingDaysDtos(workingDayList)).thenReturn(workingDayDtos);

        //When & Then
        mockMvc.perform(get("/v1/workingDays").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].workingDay_id", is(15)))
                .andExpect(jsonPath("$[0].day", is("FRIDAY")))
                .andExpect(jsonPath("$[0].startHour", is("EIGHT_AM")))
                .andExpect(jsonPath("$[0].endHour", is("ELEVEN_AM")))
                .andExpect(jsonPath("$[0].schedulesIds", hasSize(0)));
    }

    @Test
    public void shouldGetWorkingDay() throws Exception {
        //Given
        WorkingDay workingDay =
                new WorkingDay(15, Day.FRIDAY, Hour.EIGHT_AM, Hour.ELEVEN_AM, new ArrayList<>());
        WorkingDayDto workingDayDto =
                new WorkingDayDto(15, Day.FRIDAY, Hour.EIGHT_AM, Hour.ELEVEN_AM, new ArrayList<>());

        when(scheduleDbService.getWorkingDay(15)).thenReturn(java.util.Optional.of(workingDay));
        when(mapper.mapToWorkingDayDto(workingDay)).thenReturn(workingDayDto);

        //When & Then
        mockMvc.perform(get("/v1/workingDays/{dayId}", 15).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workingDay_id", is(15)))
                .andExpect(jsonPath("$.day", is("FRIDAY")))
                .andExpect(jsonPath("$.startHour", is("EIGHT_AM")))
                .andExpect(jsonPath("$.endHour", is("ELEVEN_AM")))
                .andExpect(jsonPath("$.schedulesIds", hasSize(0)));
    }


    @Test
    public void shouldCreateWorkingDay() throws Exception {
        //Given
        WorkingDay workingDay =
                new WorkingDay(15, Day.FRIDAY, Hour.EIGHT_AM, Hour.ELEVEN_AM, new ArrayList<>());
        WorkingDayDto workingDayDto =
                new WorkingDayDto(15, Day.FRIDAY, Hour.EIGHT_AM, Hour.ELEVEN_AM, new ArrayList<>());

        Gson gson = new Gson();
        String jsonContent = gson.toJson(workingDayDto);

        when(mapper.mapToWorkingDay(workingDayDto)).thenReturn(workingDay);

        //When & Then
        mockMvc.perform(post("/v1/workingDays").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateWorkingDay() throws Exception {
        //Given
        WorkingDay workingDay =
                new WorkingDay(15, Day.FRIDAY, Hour.EIGHT_AM, Hour.ELEVEN_AM, new ArrayList<>());
        WorkingDayDto workingDayDto =
                new WorkingDayDto(15, Day.FRIDAY, Hour.EIGHT_AM, Hour.ELEVEN_AM, new ArrayList<>());

        when(mapper.mapToWorkingDayDto(any())).thenReturn(workingDayDto);
        when(scheduleDbService.saveWorkingDay(workingDay)).thenReturn(workingDay);
        when(mapper.mapToWorkingDay(workingDayDto)).thenReturn(workingDay);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(workingDayDto);

        //When & Then
        mockMvc.perform(put("/v1/workingDays").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workingDay_id", is(15)))
                .andExpect(jsonPath("$.day", is("FRIDAY")))
                .andExpect(jsonPath("$.startHour", is("EIGHT_AM")))
                .andExpect(jsonPath("$.endHour", is("ELEVEN_AM")))
                .andExpect(jsonPath("$.schedulesIds", hasSize(0)));
    }

    @Test
    public void shouldDeleteWorkingDay() throws Exception {
        mockMvc.perform(delete("/v1/workingDays/{dayId}", 11).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}