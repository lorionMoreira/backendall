package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateCredencialResponse {
    
    private UUID uuid;
    private String company;
    private String senha;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CreateCredencialResponse() {
    }

    public CreateCredencialResponse(UUID uuid, String company, String senha, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.uuid = uuid;
        this.company = company;
        this.senha = senha;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
