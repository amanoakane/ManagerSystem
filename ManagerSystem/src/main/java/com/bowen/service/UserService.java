package com.bowen.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

@Service
public class UserService {
    private static final String ACCESS_INFO_FILE = "access_info.txt";

    public ResponseEntity<String> readFile(int userId, String resource) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCESS_INFO_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("UserID: " + userId)) {
                    String[] parts = line.split(", Resources: ");
                    String[] resources = parts[1].split(",");
                    if (Arrays.asList(resources).contains(resource)) {
                        return ResponseEntity.ok("Access granted for resource: " + resource);
                    }
                }
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error reading file!");
        }

        return ResponseEntity.status(403).body("Access denied for resource: " + resource);
    }
}
