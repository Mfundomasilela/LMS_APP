package com.example.LMS_APP.controller;

import com.example.LMS_APP.service.LeaveService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final LeaveService leaveService;

    public ManagerController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pendingLeaves", leaveService.pendingLeaves());
        return "manager-dashboard";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {
        leaveService.approve(id);
        return "redirect:/manager/dashboard";
    }

    @PostMapping("/reject/{id}")
    public String reject(@PathVariable Long id) {
        leaveService.reject(id);
        return "redirect:/manager/dashboard";
    }
}