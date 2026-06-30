package com.example.employeemanagement.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder

public class EmployeePageResponseDto {
    private List<EmployeeResponseDto> employees;
    private int currernPage;
    private int totalpages;
    private Long totalElements;
    private int pageSize;
}
