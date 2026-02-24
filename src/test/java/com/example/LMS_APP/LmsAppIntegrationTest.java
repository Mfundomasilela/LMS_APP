package com.example.LMS_APP;

import com.example.LMS_APP.entity.LeaveRequest;
import com.example.LMS_APP.entity.LeaveStatus;
import com.example.LMS_APP.entity.User;
import com.example.LMS_APP.entity.UserRole;
import com.example.LMS_APP.repository.LeaveRequestRepository;
import com.example.LMS_APP.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LmsAppIntegrationTest {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveLeaveRequest() {

        // 1️⃣ Create User (Employee)
        User user = new User();
        user.setUsername("mfundo");
        user.setPassword("1234");  // just test value
        user.setRole(UserRole.EMPLOYEE);
        user.setFullName("Mfundo Masilela");
        user.setLeaveBalance(15);

        User savedUser = userRepository.save(user);

        // 2️⃣ Create Leave Request
        LeaveRequest request = new LeaveRequest();
        request.setLeaveType("Annual");
        request.setReason("Vacation");
        request.setStartDate(LocalDate.of(2026, 2, 25));
        request.setEndDate(LocalDate.of(2026, 2, 27));
        request.setEmployee(savedUser);
        request.setStatus(LeaveStatus.PENDING);

        LeaveRequest saved = leaveRequestRepository.save(request);

        // 3️⃣ Assertions
        assertNotNull(saved.getId());
        assertEquals("Annual", saved.getLeaveType());
        assertEquals(savedUser.getId(), saved.getEmployee().getId());
    }
}
