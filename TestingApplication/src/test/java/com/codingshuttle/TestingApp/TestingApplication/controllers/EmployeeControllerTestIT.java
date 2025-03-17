package com.codingshuttle.TestingApp.TestingApplication.controllers;

import com.codingshuttle.TestingApp.TestingApplication.dto.EmployeeDto;
import com.codingshuttle.TestingApp.TestingApplication.entities.Employee;
import com.codingshuttle.TestingApp.TestingApplication.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

class EmployeeControllerTestIT extends AbstractionIntegrationTest{

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    void testGetEmployeeById_Success() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
        webTestClient.get()
                .uri("/employees/{id}", savedEmployee.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(savedEmployee.getId())
                .jsonPath("$.email")
                .isEqualTo(savedEmployee.getEmail());
//                .isEqualTo(testEmployeeDto);
//                .value(employeeDto -> {
//                    assertThat(employeeDto.getEmail()).isEqualTo(savedEmployee.getEmail());
//                    assertThat(employeeDto.getId()).isEqualTo(savedEmployee.getId());
//                });
    }

    @Test
    void testGetEmployeeById_Failure() {
        webTestClient.get()
                .uri("/employees/1")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void testCreateNewEmployee_WhenEmployeeAlreadyExists_ThenThrowException() {
        Employee savedEmployee = employeeRepository.save(testEmployee);

        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void testCreateNewEmployee_WhenEmployeeDoesNotExist_ThenCreateEmployee() {
        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.email")
                .isEqualTo(testEmployeeDto.getEmail())
                .jsonPath("$.name")
                .isEqualTo(testEmployeeDto.getName());
    }

    @Test
    void testUpdateEmployee_WhenEmployeeDoesNotExits_ThenThrowException() {
        webTestClient.put()
                .uri("/employees/999")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void testUpdateEmployee_WhenAttemptingToUpdateTheEmail_ThenThrowException() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
        testEmployeeDto.setName("Random");
        testEmployeeDto.setEmail("random@gmail.com");

        webTestClient.put()
                .uri("/employees/{id}", savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void testUpdateEmployee_WhenEmployeeIsValid_ThenUpdateEmployee() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
        testEmployeeDto.setName("Shark");
        testEmployeeDto.setSalary(10000L);

        webTestClient.put()
                .uri("/employees/{id}", savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(EmployeeDto.class)
                .isEqualTo(testEmployeeDto);
    }

    @Test
    void testDeleteEmployee_WhenEmployeeDoesNotExist_ThenThrowException() {
        webTestClient.delete()
                .uri("/employees/1")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void testDeleteEmployee_WhenEmployeeExists_ThenDeleteEmployee() {
        Employee savedEmployee = employeeRepository.save(testEmployee);

        webTestClient.delete()
                .uri("/employees/{id}", savedEmployee.getId())
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody(Void.class);

        webTestClient.delete()
                .uri("/employees/{id}", savedEmployee.getId())
                .exchange()
                .expectStatus()
                .isNotFound();
    }

}