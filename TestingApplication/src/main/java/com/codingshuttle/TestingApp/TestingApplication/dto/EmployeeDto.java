package com.codingshuttle.TestingApp.TestingApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private Long id;

    private String name;

    private String email;

    private Long salary;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EmployeeDto that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(salary, that.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, salary);
    }
}
