package com.bowen;

import com.bowen.interceptor.RoleInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Base64;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RoleInterceptorTest {

    @InjectMocks
    private RoleInterceptor roleInterceptor;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        roleInterceptor = new RoleInterceptor();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void testPreHandle_ValidHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic " + Base64.getEncoder().encodeToString("{\"userId\":123,\"role\":\"admin\"}".getBytes()));

        boolean result = roleInterceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verify(request,times(1)).setAttribute("userId",123);
        verify(request,times(1)).setAttribute("role","admin");
    }

    @Test
    void testPreHandle_InvalidHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        boolean result = roleInterceptor.preHandle(request, response, new Object());

        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).write("Missing or invalid Authorization header");
    }

    @Test
    void testPreHandle_MissingHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        boolean result = roleInterceptor.preHandle(request, response, new Object());

        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).write("Missing or invalid Authorization header");
    }
}

