package com.bowen;

import com.bowen.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testWriteAccess_NotAdmin() {
        String role = "user";
        Map<String, Object> accessRequest = new HashMap<>();
        accessRequest.put("userId", 1);
        accessRequest.put("endpoint", Arrays.asList("Resource1", "Resource2"));

        ResponseEntity<String> response = adminService.writeAcess(role, accessRequest);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Access denied: Admin role required.", response.getBody());
    }

    @Test
    void testWriteAccess_InvalidRequestBody() {
        String role = "admin";
        Map<String, Object> accessRequest = new HashMap<>();
        accessRequest.put("userId", 1);

        ResponseEntity<String> response = adminService.writeAcess(role, accessRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid request body: userId and endpoint should not be null", response.getBody());
    }

    @Test
    void testWriteAccessSuccess() throws IOException {
        String role = "admin";
        Map<String, Object> accessRequest = new HashMap<>();
        accessRequest.put("userId", 111);
        accessRequest.put("endpoint", Arrays.asList("Resource X", "Resource Y"));

        AdminService spyAdminService = Mockito.spy(adminService);
        FileWriter mockFileWriter = mock(FileWriter.class);
        doReturn(mockFileWriter).when(spyAdminService).createFileWriter(anyString(), eq(true));

        ResponseEntity<String> response = spyAdminService.writeAcess(role, accessRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User access granted successfully", response.getBody());

        verify(mockFileWriter).write("UserID: 111, Resources: Resource X,Resource Y\n");
        verify(mockFileWriter).close();
    }
}
