package com.example.demo.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.web.PagedModel;

import com.example.demo.dto.PhotoResponseDTO;
import com.example.demo.model.Photo;
import com.example.demo.model.User;
import com.example.demo.service.PhotoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/photos")
@Tag(name = "Photos", description = "Endpoints for managing user photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping("/upload")
    @Operation(summary = "Upload a photo",
               description = "Uploads a photo for the authenticated user",
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Map<String, String>> uploadPhoto(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        User user = photoService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        String uuid = photoService.uploadPhoto(file, user);

        Map<String, String> response = new HashMap<>();
        response.put("uuid", uuid);
        response.put("message", "Upload realizado com sucesso!");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all photos for authenticated user",
               description = "Returns all photos for the current authenticated user with pagination",
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PagedModel<PhotoResponseDTO>> getAllPhotos(Pageable pageable){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        User user = photoService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Page<Photo> photos = photoService.getAllPhotos(user.getId(), pageable);

        Page<PhotoResponseDTO> dtoPage = photos.map(photo -> {
            String downloadUrl = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replacePath("/api/photos/download/" + photo.getUuid().toString())
                    .replaceQuery(null)
                    .scheme("https")
                    .toUriString();
            
            return new PhotoResponseDTO(photo.getUuid(), photo.getOriginalFileName(), downloadUrl);
        });

        return ResponseEntity.ok(new PagedModel<>(dtoPage));
    }

    @GetMapping("/download/{uuid}")
    @Operation(summary = "Download a photo",
               description = "Downloads a photo for the authenticated user",
               security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<InputStreamResource> downloadPhoto(@PathVariable UUID uuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        User user = photoService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Photo photo = photoService.getPhotoMetadata(uuid, user.getId());

        InputStream photoStream = photoService.getPhotoStream(photo.getStoredFileName());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(photo.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + photo.getOriginalFileName() + "\"")
                .body(new InputStreamResource(photoStream));
    }
}