package com.example.demo.dto;

import java.util.UUID;

public class CredencialRequest {
    
    private UUID uuid;

    public CredencialRequest() {
    }

    public CredencialRequest(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
