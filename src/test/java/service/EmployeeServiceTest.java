package service;

import com.afs.restapi.entity.Employee;
import com.afs.restapi.exception.EmployeeCreateException;
import com.afs.restapi.repository.EmployeeJpaRepository;
import com.afs.restapi.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

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

    @Test
    void should_throw_exception_when_create_given_employee_service_and_employee_age_less_than_18_and_greater_than_65() {
        // Given
        Employee employee = new Employee(null, "Lucy", 67, "Female", 5999);

        // When and Then
        EmployeeCreateException employeeCreateException = assertThrows(EmployeeCreateException.class, () -> {
            employeeService.create(employee);
        });

        // Check the exception message
        assertEquals("Employee must be 18-65", employeeCreateException.getMessage());
    }

    @Test
    void should_return_employees_by_page_when_get_by_page_given_page_number_and_page_size() {
        // Given
        int pageNumber = 2;
        int pageSize = 3;

        List<Employee> expectedEmployees = Arrays.asList(
                new Employee(3L, "Alice", 30, "female", 99239),
                new Employee(4L, "Lucy", 30, "female", 99239),
                new Employee(5L, "Linne", 30, "female", 99239)
        );

        when(employeeRepository.findAll(PageRequest.of(pageNumber, pageSize)))
                .thenReturn(new PageImpl<>(expectedEmployees));

        // When
        List<Employee> employees = employeeService.findByPage(pageNumber, pageSize);

        // Then
        assertEquals(expectedEmployees.size(), employees.size());
        assertEquals(expectedEmployees.get(0).getName(), employees.get(0).getName());
    }

    @Test
    void should_return_list_of_all_employees_when_list_all_employees() {
        // Given
        List<Employee> expectedEmployees = new ArrayList<>();
        expectedEmployees.add(new Employee(1L, "Alice", 24, "Female", 9000));
        expectedEmployees.add(new Employee(2L, "Bob", 25, "Male", 8500));

        when(employeeRepository.findAll()).thenReturn(expectedEmployees);

        // When
        List<Employee> actualEmployees = employeeService.findAll();

        // Then
        assertEquals(expectedEmployees, actualEmployees);
    }

    @Test
    void should_return_list_of_employees_by_gender_when_find_employees_by_gender_given_valid_gender() {
        // Given
        String gender = "Female";
        List<Employee> expectedEmployees = Arrays.asList(
                new Employee(1L, "Alice", 24, "Female", 9000),
                new Employee(2L, "Eve", 28, "Female", 8000)
        );

        when(employeeRepository.findAllByGender(gender)).thenReturn(expectedEmployees);

        // When
        List<Employee> actualEmployees = employeeService.findAllByGender(gender);

        // Then
        assertEquals(expectedEmployees.size(), actualEmployees.size());
        assertEquals(expectedEmployees.get(0).getName(), actualEmployees.get(0).getName());
    }

    @Test
    void should_create_employee_when_create_given_valid_employee() {
        // Given
        Employee employeeToCreate = new Employee();
        employeeToCreate.setName("Alice");
        employeeToCreate.setAge(25);
        employeeToCreate.setGender("Female");
        employeeToCreate.setSalary(60000);

        Employee savedEmployee = new Employee();
        savedEmployee.setId(1L);
        savedEmployee.setName("Alice");
        savedEmployee.setAge(25);
        savedEmployee.setGender("Female");
        savedEmployee.setSalary(60000);

        when(employeeRepository.save(employeeToCreate)).thenReturn(savedEmployee);

        // When
        Employee returnedEmployee = employeeService.create(employeeToCreate);

        // Then
        assertNotNull(returnedEmployee);
        assertEquals(savedEmployee.getId(), returnedEmployee.getId());
        assertEquals("Alice", returnedEmployee.getName());
        assertEquals(25, returnedEmployee.getAge());
        assertEquals("Female", returnedEmployee.getGender());
        assertEquals(60000, returnedEmployee.getSalary());
    }

    @Test
    void should_remove_employee_when_delete_given_valid_employee_id() {
        // Given
        Long employeeId = 1L;

        // When
        employeeService.delete(employeeId);

        // Then
        verify(employeeRepository).deleteById(employeeId);
    }

    @Test
    void should_update_employee_when_update_given_valid_employee() {
        // Given
        Long existingEmployeeId = 1L;

        Employee existingEmployee = new Employee();
        existingEmployee.setId(existingEmployeeId);
        existingEmployee.setName("Alice");
        existingEmployee.setAge(24);
        existingEmployee.setGender("Female");
        existingEmployee.setSalary(9000);

        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(existingEmployeeId);
        updatedEmployee.setName("Alice");
        updatedEmployee.setAge(25);
        updatedEmployee.setGender("Female");
        updatedEmployee.setSalary(9500);

        when(employeeRepository.findById(existingEmployeeId)).thenReturn(Optional.of(existingEmployee));

        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);

        // When
        employeeService.update(existingEmployeeId, updatedEmployee);

        // Then
        verify(employeeRepository).findById(existingEmployeeId);
        verify(employeeRepository).save(employeeCaptor.capture());
        Employee savedEmployee = employeeCaptor.getValue();

        assertEquals(updatedEmployee.getName(), savedEmployee.getName());
        assertEquals(updatedEmployee.getAge(), savedEmployee.getAge());
        assertEquals(updatedEmployee.getGender(), savedEmployee.getGender());
        assertEquals(updatedEmployee.getSalary(), savedEmployee.getSalary());
        assertEquals(existingEmployeeId, savedEmployee.getId());
    }

}
