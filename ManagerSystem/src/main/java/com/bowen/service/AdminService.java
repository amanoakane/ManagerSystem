package com.bowen.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    private static final String ACCESS_INFO_FILE = "access_info.txt";

    public ResponseEntity<String> writeAcess(String role, Map<String, Object> accessRequest) {
        if (!"admin".equals(role)) {
            return ResponseEntity.status(403).body("Access denied: Admin role required.");
        }

        Integer userId = (Integer) accessRequest.get("userId");
        List<String> resources = (List<String>) accessRequest.get("endpoint");
        if (userId == null || resources == null || resources.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid request body: userId and endpoint should not be null");
        }

        try (FileWriter writer = new FileWriter(ACCESS_INFO_FILE, true)) {
            writer.write("UserID: " + userId + ", Resources: " + String.join(",", resources) + "\n");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error saving access information.");
        }
        return ResponseEntity.ok("User access granted successfully");
    }
}
