package com.example.demo.dto;

public class CreateCredencialRequest {
    
    private String company;
    private String senha;
    private Boolean favoritos;

    public CreateCredencialRequest() {
    }

    public CreateCredencialRequest(String company, String senha, Boolean favoritos) {
        this.company = company;
        this.senha = senha;
        this.favoritos = favoritos;
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
