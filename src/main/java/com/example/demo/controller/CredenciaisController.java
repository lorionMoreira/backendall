package com.example.demo.controller;

import com.example.demo.dto.CredencialRequest;
import com.example.demo.dto.CredenciaisResponse;
import com.example.demo.dto.CreateCredencialRequest;
import com.example.demo.dto.CreateCredencialResponse;
import com.example.demo.dto.SaveCredencialRequest;
import com.example.demo.model.Credenciais;
import com.example.demo.model.User;
import com.example.demo.service.CredenciaisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/credentials")
@Tag(name = "Credentials", description = "Endpoints for managing user credentials")
public class CredenciaisController {

    @Autowired
    private CredenciaisService credenciaisService;

    @GetMapping
    @Operation(summary = "Get all credentials for authenticated user",
               description = "Returns all credentials associated with the current authenticated user with pagination",
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getAllCredentials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOptional = credenciaisService.getUserByUsername(username);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Credenciais> credenciaisPage = credenciaisService.getAllCredenciaisByUserId(userOptional.get().getId(), pageable);
        Page<CreateCredencialResponse> responsePage = credenciaisPage.map(credenciaisService::mapToCreateResponse);

        return ResponseEntity.ok(Map.of(
            "content", responsePage.getContent(),
            "currentPage", responsePage.getNumber(),
            "totalItems", responsePage.getTotalElements(),
            "totalPages", responsePage.getTotalPages(),
            "pageSize", responsePage.getSize(),
            "hasNext", responsePage.hasNext(),
            "hasPrevious", responsePage.hasPrevious()
        ));
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

    @PostMapping
    @Operation(summary = "Create a new credential",
               description = "Creates a new credential for the authenticated user",
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> createCredential(@RequestBody CreateCredencialRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOptional = credenciaisService.getUserByUsername(username);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        Credenciais credencial = new Credenciais(
            userOptional.get(),
            request.getCompany(),
            request.getSenha()
        );
        credencial.setUuid(credenciaisService.generateUniqueUuid());
        credencial.setFavoritos(request.getFavoritos() != null ? request.getFavoritos() : false);
        credencial.setIv1(request.getIv1());
        credencial.setIv2(request.getIv2());

        Credenciais savedCredencial = credenciaisService.saveCredencial(credencial);
        CreateCredencialResponse response = credenciaisService.mapToCreateResponse(savedCredencial);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/update")
    @Operation(summary = "Update an existing credential",
               description = "Updates an existing credential for the authenticated user",
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> updateCredential(@RequestBody SaveCredencialRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOptional = credenciaisService.getUserByUsername(username);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        Optional<Credenciais> updatedCredencial = credenciaisService.updateCredencial(
            request.getUuid(),
            userOptional.get().getId(),
            request.getCompany(),
            request.getSenha(),
            request.getFavoritos(),
            request.getIv1(),
            request.getIv2()
        );

        if (updatedCredencial.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credential not found");
        }

        CreateCredencialResponse response = credenciaisService.mapToCreateResponse(updatedCredencial.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/minus")
    @Operation(summary = "Delete a credential",
               description = "Deletes a credential by UUID for the authenticated user",
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> deleteCredential(@RequestBody CredencialRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOptional = credenciaisService.getUserByUsername(username);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        boolean deleted = credenciaisService.deleteCredencial(request.getUuid(), userOptional.get().getId());

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credential not found");
        }

        return ResponseEntity.ok().body(Map.of("message", "Credential deleted successfully"));
    }
}
