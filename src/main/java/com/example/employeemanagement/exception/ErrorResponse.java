package com.example.employeemanagement.exception;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private String message;
    private int status;
    private LocalDateTime timeStamp;
}
