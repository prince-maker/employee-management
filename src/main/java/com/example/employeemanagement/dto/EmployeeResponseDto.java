package com.example.employeemanagement.dto;

import com.example.employeemanagement.entity.Department;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {
    private Long id;
    private String name;
    private String country;
    private String departmentName;
}
