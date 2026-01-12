package com.example.demo.service;

import com.example.demo.dto.CredenciaisResponse;
import com.example.demo.dto.CreateCredencialResponse;
import com.example.demo.model.Credenciais;
import com.example.demo.model.User;
import com.example.demo.repository.CredenciaisRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Credenciais> getAllCredenciaisByUserId(Long userId, Pageable pageable) {
        return credenciaisRepository.findByUserId(userId, pageable);
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

    public Optional<Credenciais> updateCredencial(UUID uuid, Long userId, String company, String senha, Boolean favoritos, String iv1, String iv2, String iv3, String email) {
        Optional<Credenciais> credencialOptional = credenciaisRepository.findByUuidAndUserId(uuid, userId);
        
        if (credencialOptional.isPresent()) {
            Credenciais credencial = credencialOptional.get();
            credencial.setCompany(company);
            credencial.setSenha(senha);
            credencial.setFavoritos(favoritos);
            credencial.setIv1(iv1);
            credencial.setIv2(iv2);
            credencial.setIv3(iv3);
            credencial.setEmail(email);
            return Optional.of(credenciaisRepository.save(credencial));
        }
        
        return Optional.empty();
    }

    public boolean deleteCredencial(UUID uuid, Long userId) {
        Optional<Credenciais> credencialOptional = credenciaisRepository.findByUuidAndUserId(uuid, userId);
        
        if (credencialOptional.isPresent()) {
            credenciaisRepository.delete(credencialOptional.get());
            return true;
        }
        
        return false;
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
            credencial.getFavoritos(),
            credencial.getIv1(),
            credencial.getIv2(),
            credencial.getIv3(),
            credencial.getEmail(),
            credencial.getCreatedAt(),
            credencial.getUpdatedAt()
        );
    }

    public CreateCredencialResponse mapToCreateResponse(Credenciais credencial) {
        return new CreateCredencialResponse(
            credencial.getUuid(),
            credencial.getCompany(),
            credencial.getSenha(),
            credencial.getFavoritos(),
            credencial.getIv1(),
            credencial.getIv2(),
            credencial.getIv3(),
            credencial.getEmail(),
            credencial.getCreatedAt(),
            credencial.getUpdatedAt()
        );
    }
}
