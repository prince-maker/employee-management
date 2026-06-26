package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeRequestDto;
import com.example.employeemanagement.dto.EmployeeResponseDto;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.EmployeeNotFoundException;
import com.example.employeemanagement.mapper.EmployeeMapper;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeMapper mapper;

    public List<EmployeeResponseDto> postEmployees(List<EmployeeRequestDto> requestDtoList) {
        logger.info("Saving {} requestDtoList", requestDtoList.size());
        ArrayList<Employee> employeeList = new ArrayList<>();
        ArrayList<EmployeeResponseDto> ResponseDtoList=new ArrayList<>();
        for (EmployeeRequestDto requestDto : requestDtoList) {
            Department department = departmentRepository.findById(requestDto.getDepartmentId())
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND
                                    , "Department not found with id:" + requestDto.getDepartmentId()));
            Employee employee = mapper.tconvertRequestToEmployee(requestDto, department);
            employeeList.add(employee);
        }

        List<Employee> savedEmployee = employeeRepository.saveAll(employeeList);
        logger.info("Employees saved successfully");
        for (Employee employee : savedEmployee) {
            ResponseDtoList.add(mapper.tconvertEmployeeToResponseDto(employee));
        }
        return ResponseDtoList;
    }

    public EmployeeResponseDto getEmployeeById(Long id) {
        logger.info("Finding employee with id:{}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Employee not found with id:{}", id);
                    return new EmployeeNotFoundException(
                            "Employee Not Found with id " + id);
                });
        logger.info("Employee fetched successfully with id:{}", id);
        return mapper.convertEmployeeToResponseDto(employee);
    }

    public List<EmployeeResponseDto> getEmployees() {
        logger.info("Fetching all employees");
        List<Employee> employeeList = employeeRepository.findAll();
        logger.info("successfully fetched all Employees");
        return mapper.convertEmployeesToResponseDto(employeeList);
    }

    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto requestDto) {
        logger.info("Updating requestDto with id:{}", id);
        Employee existingEmployee = employeeRepository.findById(id).
                orElseThrow(() -> new EmployeeNotFoundException("Employee Not Found with id:" + id));
        existingEmployee.setName(requestDto.getName());
        existingEmployee.setCountry(requestDto.getCountry());
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        logger.info("Successfully updated requestDto with id:{}", id);
        return mapper.convertEmployeeToResponseDto(updatedEmployee);
    }

    public EmployeeResponseDto deleteEmployee(Long id) {
        logger.info("Deleting employee with id:{}", id);
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> {
            logger.error("Employee not found with id:{}", id);
            return new EmployeeNotFoundException("Employee Not Found with id:" + id);
        });
        employeeRepository.deleteById(id);
        logger.info("Employee deleted with id:{}", id);
        return mapper.convertEmployeeToResponseDto(employee);
    }
}



