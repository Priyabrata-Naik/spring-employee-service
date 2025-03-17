package com.codingshuttle.TestingApp.TestingApplication.repositories;

import com.codingshuttle.TestingApp.TestingApplication.TestContainerConfiguration;
import com.codingshuttle.TestingApp.TestingApplication.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import(value = TestContainerConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1L)
                .name("Priyabrata")
                .email("priyabrata@gmail.com")
                .salary(100L)
                .build();
    }

    @Test
    @DisplayName("testFindByEmailIfPresent")
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployee() {

        /*info: Format for testing:- Arrange, Act, Assert*/

//        note: Arrange/Given
        employeeRepository.save(employee);

//        note: Act/When
        List<Employee> employeeList = employeeRepository.findByEmail(employee.getEmail());

//        note: Assert/Then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.getFirst().getEmail())
                .isEqualTo(employee.getEmail());
    }

    @Test
    @DisplayName("testFindByEmailIfNotPresent")
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmptyEmployeeList() {

//        note: Given
        String email = "notPresent12@gmail.com";
//        note: When
        List<Employee> employeeList = employeeRepository.findByEmail(email);
//        note: Then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();
    }

}