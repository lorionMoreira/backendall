package com.example.demo.dto;

public class CreateCredencialRequest {
    
    private String company;
    private String senha;
    private Boolean favoritos;
    private String iv1;
    private String iv2;

    public CreateCredencialRequest() {
    }

    public CreateCredencialRequest(String company, String senha, Boolean favoritos, String iv1, String iv2) {
        this.company = company;
        this.senha = senha;
        this.favoritos = favoritos;
        this.iv1 = iv1;
        this.iv2 = iv2;
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
}
