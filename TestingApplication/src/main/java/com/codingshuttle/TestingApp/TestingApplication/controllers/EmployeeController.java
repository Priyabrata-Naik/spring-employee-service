package com.codingshuttle.TestingApp.TestingApplication.controllers;

import com.codingshuttle.TestingApp.TestingApplication.dto.EmployeeDto;
import com.codingshuttle.TestingApp.TestingApplication.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);

        return ResponseEntity.ok(employeeDto);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createNewEmployee(@RequestBody EmployeeDto employeeDto) {
        EmployeeDto newEmployee = employeeService.createNewEmployee(employeeDto);

        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeDto employeeDto
    ) {
        EmployeeDto updatedEmployee = employeeService.updateEmployee(id, employeeDto);

        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }

}
