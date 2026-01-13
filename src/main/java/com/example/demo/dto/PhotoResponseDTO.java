package com.example.demo.dto;

import java.util.UUID;

public class PhotoResponseDTO {
    
    private UUID uuid;
    private String originalFileName;
    private String downloadUrl;

    public PhotoResponseDTO() {
    }

    public PhotoResponseDTO(UUID uuid, String originalFileName, String downloadUrl) {
        this.uuid = uuid;
        this.originalFileName = originalFileName;
        this.downloadUrl = downloadUrl;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
