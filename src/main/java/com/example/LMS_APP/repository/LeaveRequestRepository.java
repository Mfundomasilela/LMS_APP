package com.example.LMS_APP.repository;

import com.example.LMS_APP.entity.LeaveRequest;
import com.example.LMS_APP.entity.LeaveStatus;
import com.example.LMS_APP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployeeOrderByIdDesc(User employee);
    List<LeaveRequest> findByStatusOrderByIdDesc(LeaveStatus status);
}
