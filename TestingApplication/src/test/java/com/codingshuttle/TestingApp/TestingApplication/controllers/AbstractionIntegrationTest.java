package com.codingshuttle.TestingApp.TestingApplication.controllers;

import com.codingshuttle.TestingApp.TestingApplication.TestContainerConfiguration;
import com.codingshuttle.TestingApp.TestingApplication.dto.EmployeeDto;
import com.codingshuttle.TestingApp.TestingApplication.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000")
@Import(TestContainerConfiguration.class)
public class AbstractionIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    Employee testEmployee = Employee.builder()
            .id(1L)
                .name("Priyabrata")
                .email("priyabrata@gmail.com")
                .salary(100L)
                .build();

    EmployeeDto testEmployeeDto = EmployeeDto.builder()
            .id(1L)
                .name("Priyabrata")
                .email("priyabrata@gmail.com")
                .salary(100L)
                .build();

}
