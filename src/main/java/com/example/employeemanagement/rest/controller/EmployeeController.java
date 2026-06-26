package com.example.employeemanagement.rest.controller;

import com.example.employeemanagement.dto.EmployeeRequestDto;
import com.example.employeemanagement.dto.EmployeeResponseDto;
import com.example.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService service;
    private static final Logger logger= LoggerFactory.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public List<EmployeeResponseDto> postEmployees(@RequestBody List<@Valid  EmployeeRequestDto> requestDtoList) {
        return service.postEmployees(requestDtoList);
    }

    @GetMapping("/{id}")
    public EmployeeResponseDto getEmployeeById(@PathVariable @Min(1) Long id) {
        logger.info("Fetching employee with id: {}", id);
        return service.getEmployeeById(id);
    }

    @GetMapping
    public List<EmployeeResponseDto> getEmployees() {
        return service.getEmployees();
    }

    @PutMapping("/{id}")
    public EmployeeResponseDto updateEmployee(@PathVariable @Min(1) Long id, @Valid @RequestBody EmployeeRequestDto requestDto) {
        return service.updateEmployee(id, requestDto);

    }

    @DeleteMapping("/{id}")
    public EmployeeResponseDto deleteEmployee(@PathVariable @Min(1) Long id) {
        return service.deleteEmployee(id);
    }
}
