package com.example.employeemanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    private String id;
    private String name;
    private String country;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "department_id")
    private Department department;
    /*@PrePersist
    public void generateId() {
        id = UUID.randomUUID().toString();
    }*/

    @PrePersist
    public void generateMycustomId() {
        id = "EMP-" + UUID.randomUUID()
                .toString()
                .replace("_", "")
                .toUpperCase()
                .substring(0, 6);
    }


}
