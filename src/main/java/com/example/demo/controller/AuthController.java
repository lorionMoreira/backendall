package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.security.SecureRandom;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.registration.enabled:true}")
    private boolean registrationEnabled;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        // Buscar usuário
        Optional<User> userOptional = userRepository.findByUsername(authRequest.getUsername());
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        
        User user = userOptional.get();
        
        // Verificar se a conta está bloqueada
        if (user.getLockTime() != null && user.getLockTime().isAfter(LocalDateTime.now())) {
            long minutesLeft = java.time.Duration.between(LocalDateTime.now(), user.getLockTime()).toMinutes();
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Account temporarily locked. Try again in " + minutesLeft + " minutes.");
        }
        
        // Se o tempo de bloqueio já passou, desbloquear
        if (user.getLockTime() != null && user.getLockTime().isBefore(LocalDateTime.now())) {
            user.setFailedAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
        }
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            
            // Login bem-sucedido: zerar tentativas
            user.setFailedAttempts(0);
            user.setLockTime(null);
            userRepository.save(user);
            
            String token = jwtUtil.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(new AuthResponse(token, authRequest.getUsername(), user.getSalt()));
            
        } catch (BadCredentialsException e) {
            // Senha errada: incrementar tentativas
            user.setFailedAttempts(user.getFailedAttempts() + 1);
            
            if (user.getFailedAttempts() >= 5) {
                user.setLockTime(LocalDateTime.now().plusMinutes(30));
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Account locked due to too many failed attempts. Try again in 30 minutes.");
            }
            
            userRepository.save(user);
            int attemptsLeft = 5 - user.getFailedAttempts();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password. " + attemptsLeft + " attempts remaining.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        
        // 1. Validações iniciais
        if (!registrationEnabled) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Registration is currently disabled");
        }

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        // 2. GERAÇÃO DO SALT NO SERVIDOR (Temporário)
        // Gera 16 bytes aleatórios e converte para String Hexadecimal
        String generatedSalt = generateHexSalt(); 

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        
        user.setEmail(registerRequest.getEmail());
        user.setSalt(generatedSalt); // Define o salt gerado aqui
        user.setRole("USER");
        
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername());
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new AuthResponse(token, user.getUsername(), generatedSalt));
    }

    // --- Função Auxiliar para gerar o Salt Hexadecimal ---
    private String generateHexSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        StringBuilder sb = new StringBuilder();
        for (byte b : salt) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(java.util.Map.of(
            "message", "Logged out successfully"
        ));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            String token = request.getRefreshToken();
            String username = jwtUtil.extractUsernameFromExpiredToken(token);
             Date expiration = jwtUtil.extractExpirationFromExpiredToken(token);
            
            // Security: Only allow refresh within 3 days after expiration
            long daysSinceExpiration = java.time.Duration.between(
                expiration.toInstant(), 
                java.time.Instant.now()
            ).toDays();
            
            if (daysSinceExpiration > 3) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token expired too long ago. Please login again.");
            }
            
            // Buscar usuário
            Optional<User> userOptional = userRepository.findByUsername(username);
            
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }
            
            User user = userOptional.get();
            
            // Gerar novo token
            String newToken = jwtUtil.generateToken(username);
            
            return ResponseEntity.ok(new AuthResponse(newToken, username, user.getSalt()));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or corrupted token");
        }
    }
}
