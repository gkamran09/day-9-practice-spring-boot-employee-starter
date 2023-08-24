package service;

import com.afs.restapi.entity.Employee;
import com.afs.restapi.repository.EmployeeJpaRepository;
import com.afs.restapi.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
}
