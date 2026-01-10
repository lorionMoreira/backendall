package com.example.demo.controller;

import com.example.demo.dto.CredencialRequest;
import com.example.demo.dto.CredenciaisResponse;
import com.example.demo.model.Credenciais;
import com.example.demo.model.User;
import com.example.demo.service.CredenciaisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/credentials")
@Tag(name = "Credentials", description = "Endpoints for managing user credentials")
public class CredenciaisController {

    @Autowired
    private CredenciaisService credenciaisService;

    @GetMapping
    @Operation(summary = "Get all credentials for authenticated user",
               description = "Returns all credentials associated with the current authenticated user",
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getAllCredentials() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOptional = credenciaisService.getUserByUsername(username);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        List<Credenciais> credenciais = credenciaisService.getAllCredenciaisByUserId(userOptional.get().getId());
        List<CredenciaisResponse> response = credenciaisService.mapToResponseList(credenciais);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/get")
    @Operation(summary = "Get credential by UUID",
               description = "Returns a specific credential by UUID for the authenticated user",
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getCredentialByUuid(@RequestBody CredencialRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOptional = credenciaisService.getUserByUsername(username);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        Optional<Credenciais> credencialOptional = credenciaisService.getCredencialByUuidAndUserId(request.getUuid(), userOptional.get().getId());

        if (credencialOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credential not found");
        }

        CredenciaisResponse response = credenciaisService.mapToResponse(credencialOptional.get());
        return ResponseEntity.ok(response);
    }
}
