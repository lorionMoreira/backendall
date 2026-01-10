package com.example.demo.dto;

import java.util.UUID;

public class SaveCredencialRequest {
    
    private UUID uuid;
    private String company;
    private String senha;
    private Boolean favoritos;

    public SaveCredencialRequest() {
    }

    public SaveCredencialRequest(UUID uuid, String company, String senha, Boolean favoritos) {
        this.uuid = uuid;
        this.company = company;
        this.senha = senha;
        this.favoritos = favoritos;
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
}
