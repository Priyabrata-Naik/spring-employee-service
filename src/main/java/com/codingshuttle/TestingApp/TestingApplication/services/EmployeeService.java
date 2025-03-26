package com.codingshuttle.TestingApp.TestingApplication.services;

import com.codingshuttle.TestingApp.TestingApplication.dto.EmployeeDto;

public interface EmployeeService {

    EmployeeDto getEmployeeById(Long id);

    EmployeeDto createNewEmployee(EmployeeDto employeeDto);

    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto);

    void deleteEmployee(Long id);

}
