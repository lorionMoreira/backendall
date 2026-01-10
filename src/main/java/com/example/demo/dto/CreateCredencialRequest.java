package com.example.demo.dto;

public class CreateCredencialRequest {
    
    private String company;
    private String senha;

    public CreateCredencialRequest() {
    }

    public CreateCredencialRequest(String company, String senha) {
        this.company = company;
        this.senha = senha;
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
}
