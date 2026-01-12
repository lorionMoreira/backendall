package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateCredencialResponse {
    
    private UUID uuid;
    private String company;
    private String senha;
    private Boolean favoritos;
    private String iv1;
    private String iv2;
    private String iv3;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CreateCredencialResponse() {
    }

    public CreateCredencialResponse(UUID uuid, String company, String senha, Boolean favoritos, String iv1, String iv2, String iv3, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.uuid = uuid;
        this.company = company;
        this.senha = senha;
        this.favoritos = favoritos;
        this.iv1 = iv1;
        this.iv2 = iv2;
        this.iv3 = iv3;
        this.email = email;
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

    public Boolean getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(Boolean favoritos) {
        this.favoritos = favoritos;
    }

    public String getIv1() {
        return iv1;
    }

    public void setIv1(String iv1) {
        this.iv1 = iv1;
    }

    public String getIv2() {
        return iv2;
    }

    public void setIv2(String iv2) {
        this.iv2 = iv2;
    }

    public String getIv3() {
        return iv3;
    }

    public void setIv3(String iv3) {
        this.iv3 = iv3;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
