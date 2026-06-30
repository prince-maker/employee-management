package com.example.employeemanagement.rest.controller;

import com.example.employeemanagement.dto.EmployeePageResponseDto;
import com.example.employeemanagement.dto.EmployeeRequestDto;
import com.example.employeemanagement.dto.EmployeeResponseDto;
import com.example.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public List<EmployeeResponseDto> postEmployees(@RequestBody List<@Valid EmployeeRequestDto> requestDtoList) {
        return service.postEmployees(requestDtoList);
    }

    @GetMapping("/{id}")
    public EmployeeResponseDto getEmployeeById(@PathVariable String id) {
        logger.info("Fetching employee with id: {}", id);
        return service.getEmployeeById(id);
    }

    @GetMapping
    public EmployeePageResponseDto getEmployees(Pageable pageable) {
        return service.getEmployees(pageable);
    }

    @PutMapping("/{id}")
    public EmployeeResponseDto updateEmployee(@PathVariable String id, @Valid @RequestBody EmployeeRequestDto requestDto) {
        return service.updateEmployee(id, requestDto);

    }

    @DeleteMapping("/{id}")
    public EmployeeResponseDto deleteEmployee(@PathVariable String id) {
        return service.deleteEmployee(id);
    }

    @GetMapping("/department")
    public List<EmployeeResponseDto> getEmployeesByDepartment(@RequestParam String departmentName) {
        return service.getEmployeesByDepartment(departmentName);


    }

    @GetMapping("/search")
    public List<EmployeeResponseDto> getEmpByCountryDept(@RequestParam String country, @RequestParam String DepartmentName) {
        return service.getEmpByCountryDept(country, DepartmentName);
    }

}
