package com.example.LMS_APP.controller;

import com.example.LMS_APP.entity.User;
import com.example.LMS_APP.repository.UserRepository;
import com.example.LMS_APP.service.LeaveService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final LeaveService leaveService;
    private final UserRepository userRepo;

    public EmployeeController(LeaveService leaveService, UserRepository userRepo) {
        this.leaveService = leaveService;
        this.userRepo = userRepo;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "success", required = false) String success) {

        // 🔥 Reload user from database
        User employee = userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("employeeName", employee.getFullName());
        model.addAttribute("leaves", leaveService.employeeLeaves(employee));
        model.addAttribute("leaveBalance", employee.getLeaveBalance()); // 🔥 THIS FIXES NULL
        model.addAttribute("error", error);
        model.addAttribute("success", success);

        return "employee-dashboard";
    }

    @PostMapping("/apply")
    public String apply(Authentication auth,
                        @RequestParam String leaveType,
                        @RequestParam LocalDate startDate,
                        @RequestParam LocalDate endDate,
                        @RequestParam String reason) {

        User employee = userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            leaveService.applyLeave(employee, leaveType, startDate, endDate, reason);
            return "redirect:/employee/dashboard?success=Leave+submitted";
        } catch (Exception e) {
            return "redirect:/employee/dashboard?error=" + e.getMessage().replace(" ", "+");
        }
    }
}