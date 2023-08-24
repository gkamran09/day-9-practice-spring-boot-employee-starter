package service;

import com.afs.restapi.entity.Employee;
import com.afs.restapi.exception.EmployeeCreateException;
import com.afs.restapi.repository.EmployeeJpaRepository;
import com.afs.restapi.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    private EmployeeService employeeService;

    @Mock
    private EmployeeJpaRepository employeeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employeeService = new EmployeeService(employeeRepository);
    }

    @Test
    void should_get_employee_by_id() {
        // Given
        Employee mockEmployee = new Employee();
        mockEmployee.setId(1L);
        mockEmployee.setName("John");
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(mockEmployee));

        // When
        Employee retrievedEmployee = employeeService.findById(1L);

        // Then
        assertNotNull(retrievedEmployee);
        assertEquals("John", retrievedEmployee.getName());
        assertEquals(1L, retrievedEmployee.getId());
    }

    @Test
    void should_return_created_employee_when_create_given_employee_service_and_valid_age() {
        // Given
        Employee employee = new Employee(null,"Alice",23,"Female",5000);
        Employee savedEmployee = new Employee(null,"Alice",23,"Female",5000);
        when(employeeRepository.save(employee)).thenReturn(savedEmployee);

        // When
        Employee employeeResponse = employeeService.create(employee);

        // Then
        assertEquals(savedEmployee.getId(),employeeResponse.getId());
        assertEquals("Alice",savedEmployee.getName());
        assertEquals(23,savedEmployee.getAge());
        assertEquals("Female",savedEmployee.getGender());
        assertEquals(5000,savedEmployee.getSalary());
    }

    @Test
    void should_throw_exception_when_create_given_employee_service_and_employee_age_less_than_18() {
        // Given
        Employee employee = new Employee(null, "Lucy", 17, "Female", 5999);

        // When and Then
        EmployeeCreateException exception = assertThrows(EmployeeCreateException.class, () -> {
            employeeService.create(employee);
        });

        // Then
        assertEquals("Employee must be 18-65", exception.getMessage());
    }

}
