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
