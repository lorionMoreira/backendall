package com.example.demo.service;

import com.example.demo.dto.CredenciaisResponse;
import com.example.demo.model.Credenciais;
import com.example.demo.model.User;
import com.example.demo.repository.CredenciaisRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CredenciaisService {

    @Autowired
    private CredenciaisRepository credenciaisRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<Credenciais> getAllCredenciaisByUserId(Long userId) {
        return credenciaisRepository.findByUserId(userId);
    }

    public Optional<Credenciais> getCredencialByIdAndUserId(Long id, Long userId) {
        return credenciaisRepository.findByIdAndUserId(id, userId);
    }

    public Optional<Credenciais> getCredencialByUuidAndUserId(UUID uuid, Long userId) {
        return credenciaisRepository.findByUuidAndUserId(uuid, userId);
    }

    public UUID generateUniqueUuid() {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (credenciaisRepository.existsByUuid(uuid));
        return uuid;
    }

    public Credenciais saveCredencial(Credenciais credencial) {
        return credenciaisRepository.save(credencial);
    }

    public Optional<Credenciais> updateCredencial(UUID uuid, Long userId, String company, String senha) {
        Optional<Credenciais> credencialOptional = credenciaisRepository.findByUuidAndUserId(uuid, userId);
        
        if (credencialOptional.isPresent()) {
            Credenciais credencial = credencialOptional.get();
            credencial.setCompany(company);
            credencial.setSenha(senha);
            return Optional.of(credenciaisRepository.save(credencial));
        }
        
        return Optional.empty();
    }

    public List<CredenciaisResponse> mapToResponseList(List<Credenciais> credenciais) {
        return credenciais.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CredenciaisResponse mapToResponse(Credenciais credencial) {
        return new CredenciaisResponse(
            credencial.getId(),
            credencial.getUuid(),
            credencial.getCompany(),
            credencial.getSenha(),
            credencial.getCreatedAt(),
            credencial.getUpdatedAt()
        );
    }
}
