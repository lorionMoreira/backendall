package com.example.demo.repository;

import com.example.demo.model.Credenciais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CredenciaisRepository extends JpaRepository<Credenciais, Long> {
    
    List<Credenciais> findByUserId(Long userId);
    
    Optional<Credenciais> findByIdAndUserId(Long id, Long userId);
    
    Optional<Credenciais> findByUuidAndUserId(UUID uuid, Long userId);
    
    boolean existsByUuid(UUID uuid);
}
