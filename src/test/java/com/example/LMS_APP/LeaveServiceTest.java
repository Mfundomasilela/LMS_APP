package com.example.LMS_APP;

import com.example.LMS_APP.service.LeaveService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class LeaveServiceTest {

    private final LeaveService leaveService = new LeaveService();

    @Test
    void testCalculateLeaveDays() {
        LocalDate start = LocalDate.of(2026, 2, 20);
        LocalDate end = LocalDate.of(2026, 2, 22);

        long days = leaveService.calculateLeaveDays(start, end);

        assertEquals(3, days);
    }

    @Test
    void testLeaveBalanceDeduction() {
        int initialBalance = 15;
        int leaveDays = 3;

        int newBalance = leaveService.deductLeaveBalance(initialBalance, leaveDays);

        assertEquals(12, newBalance);
    }
}