package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartmentId(Long id);

    Optional<Employee> findById(String id);

    Optional<Employee> deleteById(String id);

    @Query("SELECT e FROM Employee e JOIN e.department d where e.country=:country AND d.id=:departmentId")
    List<Employee> findEmployees(@Param("country") String country, @Param("departmentId") Long departmentId);
}
