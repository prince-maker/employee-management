package com.example.employeemanagement.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDto {
    @NotBlank(message = "It should not be blank")
    private String name;
    private String country;
    private Long departmentId;

}
