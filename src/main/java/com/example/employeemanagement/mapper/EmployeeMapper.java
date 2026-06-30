package com.example.employeemanagement.mapper;

import com.example.employeemanagement.dto.EmployeeRequestDto;
import com.example.employeemanagement.dto.EmployeeResponseDto;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class EmployeeMapper {
    public EmployeeResponseDto convertEmployeeToResponseDto(Employee employee) {
        EmployeeResponseDto responseDto = EmployeeResponseDto.builder()
                .id(employee.getId())
                .name(employee.getName())
                .country(employee.getCountry())
                .build();
        return responseDto;
    }


    public Employee convertRequestToEmployee(EmployeeRequestDto requestDto, Department department) {
        Employee employee = new Employee();
        employee.setName(requestDto.getName());
        employee.setCountry(requestDto.getCountry());
        employee.setDepartment(department);

        return employee;
    }

    public EmployeeResponseDto tconvertEmployeeToResponseDto(Employee employee) {
        EmployeeResponseDto responseDto = EmployeeResponseDto.builder()
                .id(employee.getId())
                .name(employee.getName())
                .country(employee.getCountry())
                .departmentName(employee.getDepartment().getDepartmentName())
                .build();
        return responseDto;
    }

    public List<EmployeeResponseDto> tconvertEmployeeToResponse(List<Employee> employees) {

        List<EmployeeResponseDto> employeeResponseDto = employees.stream()
                .map(emp -> EmployeeResponseDto.builder()
                        .id(emp.getId())
                        .name(emp.getName())
                        .country(emp.getCountry())
                        .departmentName(emp.getDepartment().getDepartmentName())
                        .build())
                .toList();
        return employeeResponseDto;
    }

    public Page<EmployeeResponseDto> pageAllEmployee(Page<Employee> employeeList) {
        Page<EmployeeResponseDto> employeeResponseDto = employeeList
                .map(emp -> EmployeeResponseDto.builder()
                        .id(emp.getId())
                        .name(emp.getName())
                        .country(emp.getCountry())
                        .departmentName(emp.getDepartment().getDepartmentName())
                        .build());
        return employeeResponseDto;

    }


}

