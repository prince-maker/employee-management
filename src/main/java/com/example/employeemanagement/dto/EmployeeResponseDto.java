package com.example.employeemanagement.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto implements Serializable {
    // private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String country;
    private String departmentName;
}
