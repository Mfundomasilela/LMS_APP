package com.example.LMS_APP.service;

import com.example.LMS_APP.entity.LeaveRequest;
import com.example.LMS_APP.entity.LeaveStatus;
import com.example.LMS_APP.entity.User;
import com.example.LMS_APP.repository.LeaveRequestRepository;
import com.example.LMS_APP.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeaveService {

    private LeaveRequestRepository leaveRepo;
    private UserRepository userRepo;

    public LeaveService(LeaveRequestRepository leaveRepo, UserRepository userRepo) {
        this.leaveRepo = leaveRepo;
        this.userRepo = userRepo;
    }
    public LeaveService() {

    }


    public List<LeaveRequest> employeeLeaves(User employee) {
        return leaveRepo.findByEmployeeOrderByIdDesc(employee);
    }

    public List<LeaveRequest> pendingLeaves() {
        return leaveRepo.findByStatusOrderByIdDesc(LeaveStatus.PENDING);
    }

    @Transactional
    public void applyLeave(User employee, String leaveType, LocalDate start, LocalDate end, String reason) {

        if (leaveType == null || leaveType.isBlank())
            throw new IllegalArgumentException("Leave type required");

        if (reason == null || reason.isBlank())
            throw new IllegalArgumentException("Reason required");

        if (start == null || end == null)
            throw new IllegalArgumentException("Dates required");

        if (end.isBefore(start))
            throw new IllegalArgumentException("End date cannot be before start date");

        LeaveRequest req = new LeaveRequest();
        req.setEmployee(employee);
        req.setLeaveType(leaveType.trim());
        req.setStartDate(start);
        req.setEndDate(end);
        req.setReason(reason.trim());
        req.setStatus(LeaveStatus.PENDING);

        leaveRepo.save(req);
    }

    // 🔥 Calculate number of leave days
    public long calculateLeaveDays(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end) + 1;
    }

    @Transactional
    public void approve(Long leaveId) {

        LeaveRequest req = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));

        if (req.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Leave already processed");
        }

        User user = req.getEmployee();

        long days = calculateLeaveDays(req.getStartDate(), req.getEndDate());

        int newBalance = user.getLeaveBalance() - (int) days;

        if (newBalance < 0) {
            throw new IllegalStateException("Insufficient leave balance");
        }

        user.setLeaveBalance(newBalance);

        req.setStatus(LeaveStatus.APPROVED);

        userRepo.save(user);      // 🔥 CRITICAL LINE
        leaveRepo.save(req);
    }

    @Transactional
    public void reject(Long leaveId) {

        LeaveRequest req = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));

        if (req.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Leave already processed");
        }

        req.setStatus(LeaveStatus.REJECTED);

        leaveRepo.save(req);
    }

    public int deductLeaveBalance(int initialBalance, int leaveDays) {

        int result = initialBalance - leaveDays;
        return result;
    }
}