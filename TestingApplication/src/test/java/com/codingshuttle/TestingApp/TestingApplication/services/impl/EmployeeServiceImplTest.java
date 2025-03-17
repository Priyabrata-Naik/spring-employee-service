package com.codingshuttle.TestingApp.TestingApplication.services.impl;

import com.codingshuttle.TestingApp.TestingApplication.TestContainerConfiguration;
import com.codingshuttle.TestingApp.TestingApplication.dto.EmployeeDto;
import com.codingshuttle.TestingApp.TestingApplication.entities.Employee;
import com.codingshuttle.TestingApp.TestingApplication.exceptions.ResourceNotFoundException;
import com.codingshuttle.TestingApp.TestingApplication.repositories.EmployeeRepository;
import com.codingshuttle.TestingApp.TestingApplication.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Import(value = TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee mockEmployee;
    private EmployeeDto mockEmployeeDto;

    @BeforeEach
    void setUp() {
        mockEmployee = Employee.builder()
                .id(1L)
                .name("Priyabrata")
                .email("priyabrata@gmail.com")
                .salary(100L)
                .build();

        mockEmployeeDto = modelMapper.map(mockEmployee, EmployeeDto.class);
    }

    @Test
    @DisplayName("testGetEmployeeById")
    void testGetEmployeeById_WhenEmployeeIdPresent_ThenReturnEmployeeDto() {

//        note: Testing format Assign, Act, Assert

        Long id = mockEmployee.getId();
//        oper: assign
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee)); //info: stubbing

//        oper: act
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);

//        oper: assert
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(id);
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
        verify(employeeRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("testGetEmployeeByIdIfNotPresent")
    void testGetEmployeeById_WhenEmployeeIsNotPresent_ThenThrowException() {
//        oper: arrange
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

//        oper: act and assert
        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

    }

    @Test
    @DisplayName("testCreateNewEmployee")
    void testCreateNewEmployee_WhenValidEmployee_ThenCreateNewEmployee() {
//        oper: assign
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);

//        oper: act
        EmployeeDto employeeDto = employeeService.createNewEmployee(mockEmployeeDto);

//        oper: assert

        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());

        ArgumentCaptor<Employee> employeeArgumentCaptor
                = ArgumentCaptor.forClass(Employee.class);
//        verify(employeeRepository).save(any(Employee.class));
        verify(employeeRepository).save(employeeArgumentCaptor.capture());

        Employee capturedEmployee = employeeArgumentCaptor.getValue();
        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());
    }

    @Test
    @DisplayName("testCreateNewEmployeeForCasePresent")
    void testCreateNewEmployee_WhenAttemptingToCreateEmployeeWithExistingEmail_ThenReturnException() {
//        oper: arrange
        when(employeeRepository.findByEmail(mockEmployeeDto.getEmail())).thenReturn(List.of(mockEmployee));

//        oper: act & assert
        assertThatThrownBy(() -> employeeService.createNewEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: " + mockEmployeeDto.getEmail());

        verify(employeeRepository).findByEmail(mockEmployeeDto.getEmail());
        verify(employeeRepository, never()).save(any());

    }

    @Test
    void testUpdateEmployee_WhenEmployeeDoesNotExist_ThenReturnException() {
//        oper: arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

//        oper: act & assert
        assertThatThrownBy(() -> employeeService.updateEmployee(1L, mockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository).findById(1L);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testUpdateEmployee_WhenUpdatingEmail_ThenReturnException() {
//        oper: arrange
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("Random");
        mockEmployeeDto.setEmail("random@gmail.com");

//        oper: act & assert
        assertThatThrownBy(() -> employeeService.updateEmployee(mockEmployeeDto.getId(), mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee can't be updated");

        verify(employeeRepository).findById(mockEmployeeDto.getId());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testUpdateEmployee_WhenEmployeeExist_ThenUpdateEmployee() {
//        oper: assign
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("Random Name");
        mockEmployeeDto.setSalary(1000L);

        Employee newEmployee = modelMapper.map(mockEmployeeDto, Employee.class);
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

//        oper: act
        EmployeeDto updatedEmployeeDto = employeeService.updateEmployee(mockEmployeeDto.getId(), mockEmployeeDto);

//        oper: assert
        assertThat(updatedEmployeeDto).isEqualTo(mockEmployeeDto);

        verify(employeeRepository).findById(mockEmployeeDto.getId());
        verify(employeeRepository).save(any());
    }

    @Test
    void testDeleteEmployee_WhenEmployeeDoesNotExist_ThenThrowException() {
//        oper: arrange
        when(employeeRepository.existsById(1L)).thenReturn(false);

//        oper: act & assert
        assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: " + 1L);

        verify(employeeRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteEmployee_WhenValidEmployee_ThenDeleteEmployee() {
//        oper: arrange
        when(employeeRepository.existsById(1L)).thenReturn(true);


//        oper: assert
        assertThatCode(() -> employeeService.deleteEmployee(1L))
                .doesNotThrowAnyException();

        verify(employeeRepository).deleteById(1L);
    }

}