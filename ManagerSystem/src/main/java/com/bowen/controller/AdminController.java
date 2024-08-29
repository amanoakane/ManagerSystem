package com.bowen.controller;

import javax.servlet.http.HttpServletRequest;

import com.bowen.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/addUser")
    public ResponseEntity<String> addUserAccess(@RequestBody Map<String, Object> accessRequest, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        return adminService.writeAcess(role, accessRequest);
    }
}

