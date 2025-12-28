package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/protected")
@Tag(name = "Protected Routes", description = "Example protected endpoints requiring JWT authentication")
public class ExampleController {

    @GetMapping("/hello")
    @Operation(summary = "Get a protected hello message", 
               description = "Returns a greeting message with authenticated user information",
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Map<String, Object>> getProtectedHello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello from protected route!");
        response.put("username", username);
        response.put("timestamp", LocalDateTime.now());
        response.put("authorities", authentication.getAuthorities());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-info")
    @Operation(summary = "Get authenticated user information",
               description = "Returns details about the currently authenticated user",
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Map<String, Object>> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> response = new HashMap<>();
        response.put("username", authentication.getName());
        response.put("authenticated", authentication.isAuthenticated());
        response.put("authorities", authentication.getAuthorities());
        response.put("principal", authentication.getPrincipal().getClass().getSimpleName());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    @Operation(summary = "Simple protected test endpoint",
               description = "A simple endpoint to test JWT authentication",
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "You have successfully accessed a protected route!");
        
        return ResponseEntity.ok(response);
    }
}
