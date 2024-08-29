package com.bowen.controller;

import javax.servlet.http.HttpServletRequest;

import com.bowen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{resource}")
    public ResponseEntity<String> checkUserAccess(@PathVariable String resource, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(400).body("Invalid userId!");
        }
        return userService.readFile(userId, resource);
    }
}

