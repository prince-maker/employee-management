package com.example.employeemanagement.mapper;

import com.example.employeemanagement.dto.EmployeeRequestDto;
import com.example.employeemanagement.dto.EmployeeResponseDto;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    public Employee convertRequestDtoToEmployee(EmployeeRequestDto requestDto,Department department) {
        Employee employee = new Employee();
        employee.setName(requestDto.getName());
        employee.setCountry(requestDto.getCountry());
        employee.setDepartment(department);

        return employee;
    }

    public List<EmployeeResponseDto> convertEmployeesToResponseDto(List<Employee> employees) {
        ArrayList<EmployeeResponseDto> responseDtoList = new ArrayList<>();
        for (Employee emp : employees) {
            EmployeeResponseDto responseDto = EmployeeResponseDto.builder()
                    .id(emp.getId()).name(emp.getName())
                    .country(emp.getCountry()).build();
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    //DTO to employee mapping
    public List<Employee> convertRequestDtoToEmployees(List<EmployeeRequestDto> requestDtoList, Department department) {
        ArrayList<Employee> employeeList = new ArrayList<>();
        for (EmployeeRequestDto employeeRequestDto : requestDtoList) {
            Employee emp = new Employee();
            emp.setName(employeeRequestDto.getName());
            emp.setCountry(employeeRequestDto.getCountry());
            emp.setDepartment(department);
            employeeList.add(emp);
        }
        return employeeList;
    }

    public Employee tconvertRequestToEmployee(EmployeeRequestDto requestDto,Department department) {
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


}

