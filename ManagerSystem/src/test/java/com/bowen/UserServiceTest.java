package com.bowen;

import com.bowen.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private BufferedReader bufferedReader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReadFile_AccessGranted() throws IOException {
        String userId = "UserID: 123";
        String resource = "Resource A";
        String line = userId + ", Resources: " + resource;

        when(bufferedReader.readLine())
                .thenReturn(line)
                .thenReturn(null);

        ResponseEntity<String> response = userService.readFile(123, resource);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Access granted for resource: " + resource, response.getBody());
    }

    @Test
    void testReadFile_AccessDenied() throws IOException {
        // Arrange
        String userId = "UserID: 123";
        String resource = "Resource B";
        String line = userId + ", Resources: Resource A";

        when(bufferedReader.readLine())
                .thenReturn(line)
                .thenReturn(null);

        ResponseEntity<String> response = userService.readFile(123, resource);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Access denied for resource: " + resource, response.getBody());
    }
}
