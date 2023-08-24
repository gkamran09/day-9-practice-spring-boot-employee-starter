package service;

import com.afs.restapi.repository.EmployeeJpaRepository;
import com.afs.restapi.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EmployeeServiceTest {

    private EmployeeService employeeService;

    @Mock
    private EmployeeJpaRepository employeeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employeeService = new EmployeeService(employeeRepository);
    }
}
