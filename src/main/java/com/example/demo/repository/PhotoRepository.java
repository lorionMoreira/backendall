package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Page<Photo> findByUserId(Long userId, Pageable pageable);
    Optional<Photo> findByUuidAndUserId(UUID uuid, Long userId);
}
