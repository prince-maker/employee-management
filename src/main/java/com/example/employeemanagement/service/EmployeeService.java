package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeePageResponseDto;
import com.example.employeemanagement.dto.EmployeeRequestDto;
import com.example.employeemanagement.dto.EmployeeResponseDto;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.DepartmentNotFoundException;
import com.example.employeemanagement.exception.EmployeeNotFoundException;
import com.example.employeemanagement.mapper.EmployeeMapper;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper mapper;

    // ==============================
    // Create operations
    // ==============================
    @Caching(evict = {
            @CacheEvict(value = "allEmployees", allEntries = true),
            @CacheEvict(value = "employeeByDepartmentName", allEntries = true),
            @CacheEvict(value = "employeeByCountryAndDeptName", allEntries = true)

    })
    public List<EmployeeResponseDto> postEmployees(List<EmployeeRequestDto> requestDtoList) {
        logger.info("Saving {} requestDtoList", requestDtoList.size());
        ArrayList<Employee> employeeList = new ArrayList<>();
        ArrayList<EmployeeResponseDto> responseDtoList = new ArrayList<>();
        for (EmployeeRequestDto requestDto : requestDtoList) {
            Department department = departmentRepository.findById(requestDto.getDepartmentId())
                    .orElseThrow(() -> {
                        logger.warn("department not found with id: {}", requestDto.getDepartmentId());
                        return new DepartmentNotFoundException(
                                "Department not found with id:" + requestDto.getDepartmentId());
                    });

            Employee employee = mapper.convertRequestToEmployee(requestDto, department);
            employeeList.add(employee);
        }

        List<Employee> savedEmployee = employeeRepository.saveAll(employeeList);
        logger.info("Employees saved successfully");
        for (Employee employee : savedEmployee) {
            responseDtoList.add(mapper.tconvertEmployeeToResponseDto(employee));
        }
        return responseDtoList;
    }

    // ==============================
    // Fetch employee by id
    // ==============================

    @Cacheable(value = "employee", key = "#id")
    public EmployeeResponseDto getEmployeeById(String id) {
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

    // ==============================
    // Fetch all employees with pagination
    // ==============================

    @Cacheable(value = "allEmployees", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public EmployeePageResponseDto getEmployees(Pageable pageable) {
        logger.info("Fetching all employees");
        Page<Employee> employeeList = employeeRepository.findAll(pageable);
        if (employeeList.isEmpty()) throw new EmployeeNotFoundException("Employees not found");
        logger.info("successfully fetched all Employees");
        Page<EmployeeResponseDto> employeeResponseDto = mapper.pageAllEmployee(employeeList);
        EmployeePageResponseDto employeePageResponseDto = EmployeePageResponseDto.builder()
                .employees(employeeResponseDto.getContent())
                .currernPage(employeeResponseDto.getNumber())
                .totalpages(employeeResponseDto.getTotalPages())
                .totalElements(employeeResponseDto.getTotalElements())
                .pageSize(employeeResponseDto.getSize())
                .build();
        return employeePageResponseDto;
    }

    // ==============================
    // update operations
    // ==============================

    @Caching(
            put = {@CachePut(value = "employee", key = "#id")},
            evict = {
                    @CacheEvict(value = "allEmployees", allEntries = true),
                    @CacheEvict(value = "employeeByDepartmentName", allEntries = true),
                    @CacheEvict(value = "employeeByCountryAndDeptName", allEntries = true)

            })
    public EmployeeResponseDto updateEmployee(String id, EmployeeRequestDto requestDto) {
        logger.info("Updating requestDto with id:{}", id);
        Employee existingEmployee = employeeRepository.findById(id).
                orElseThrow(() -> new EmployeeNotFoundException("Employee Not Found with id:" + id));
        existingEmployee.setName(requestDto.getName());
        existingEmployee.setCountry(requestDto.getCountry());
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        logger.info("Successfully updated requestDto with id:{}", id);
        return mapper.convertEmployeeToResponseDto(updatedEmployee);
    }

    // ==============================
    // Delete operation
    // ==============================

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "employee", key = "#id", beforeInvocation = true),
            @CacheEvict(value = "allEmployees", allEntries = true, beforeInvocation = true),
            @CacheEvict(value = "employeeByDepartmentName", allEntries = true, beforeInvocation = true),
            @CacheEvict(value = "employeeByCountryAndDeptName", allEntries = true, beforeInvocation = true)

    })

    public EmployeeResponseDto deleteEmployee(String id) {
        logger.info("Deleting employee with id:{}", id);
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> {
            logger.error("Employee not found with id:{}", id);
            return new EmployeeNotFoundException("Employee Not Found with id:" + id);
        });
        employeeRepository.deleteById(id);
        logger.info("Employee deleted with id:{}", id);
        return mapper.convertEmployeeToResponseDto(employee);
    }

    // ==============================
    // Fetch employees by department name
    // ==============================

    @Cacheable(value = "employeeByDepartmentName", key = "#departmentName")
    public List<EmployeeResponseDto> getEmployeesByDepartment(String departmentName) {
        logger.info("Finding Employees with Department name:{}", departmentName);
        Department department = departmentRepository.findByDepartmentName(departmentName).orElseThrow(() -> new DepartmentNotFoundException("Department not found with department name: " + departmentName));
        List<Employee> employees = employeeRepository.findByDepartmentId(department.getId());
        if (employees.isEmpty())
            throw new EmployeeNotFoundException("Employees not found With the department name: " + departmentName);

        return mapper.tconvertEmployeeToResponse(employees);


    }

    // ==============================
    // Fetch employees by country and department name
    // ==============================
    @Cacheable(value = "employeeByCountryAndDeptName", key = "#country + '-' + #departmentName")
    public List<EmployeeResponseDto> getEmpByCountryDept(String country, String departmentName) {
        logger.info("Fetching employees with country:{} and department name:{} ", country, departmentName);
        Department department = departmentRepository.findByDepartmentName(departmentName).orElseThrow(() -> new DepartmentNotFoundException("Department not found wth department name: " + departmentName));
        List<Employee> employees = employeeRepository.findEmployees(country, department.getId());
        if (employees.isEmpty()) throw new EmployeeNotFoundException("employees not found");
        return mapper.tconvertEmployeeToResponse(employees);

    }
}




