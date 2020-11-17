package com.kodilla.clinic.controller;

import com.kodilla.clinic.dto.StaffEvaluationDto;
import com.kodilla.clinic.exception.StaffEvaluationNotFoundException;
import com.kodilla.clinic.mapper.StaffEvaluationMapper;
import com.kodilla.clinic.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1")
public class StaffEvaluationController {
    @Autowired
    private StaffEvaluationMapper mapper;

    @Autowired
    private DbService service;

    @RequestMapping(method = RequestMethod.GET, value = "staffEvaluations")
    public List<StaffEvaluationDto> getStaffEvaluations() {
        return mapper.mapToStaffEvaluationDtoList(service.getAllStaffEvaluations());
    }

    @RequestMapping(method = RequestMethod.GET, value = "staffEvaluations/{evaluationId}")
    public StaffEvaluationDto getStaffEvaluation(@PathVariable Integer evaluationId)
            throws StaffEvaluationNotFoundException {
        return mapper.mapToStuffEvaluationDto(service.getStaffEvaluation(evaluationId)
                .orElseThrow(StaffEvaluationNotFoundException::new));
    }

    @RequestMapping(method = RequestMethod.POST, value = "staffEvaluations")
    public StaffEvaluationDto addStaffEvaluation(@RequestBody StaffEvaluationDto staffEvaluationDto) {
        return mapper.mapToStuffEvaluationDto(service.saveStaffEvaluation(
                mapper.mapToStuffEvaluation(staffEvaluationDto)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "staffEvaluations")
    public StaffEvaluationDto updateStaffEvaluation(@RequestBody StaffEvaluationDto staffEvaluationDto) {
        return mapper.mapToStuffEvaluationDto(service.saveStaffEvaluation(
                mapper.mapToStuffEvaluation(staffEvaluationDto)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "staffEvaluations/{evaluationId}")
    public void deleteStaffEvaluation(@PathVariable Integer evaluationId) {
        service.deleteStaffEvaluation(evaluationId);
    }
}
