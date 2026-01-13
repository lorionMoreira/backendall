package com.example.demo.service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Photo;
import com.example.demo.model.User;
import com.example.demo.repository.PhotoRepository;
import com.example.demo.repository.UserRepository;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Service
public class PhotoService {
    
    @Autowired
    private MinioClient minioClient;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public String uploadPhoto(MultipartFile file, User user) {
        try {
            // 1. Verificar se o bucket existe, senão cria
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // 2. Gerar nome único (UUID) + extensão original
            String originalName = file.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf("."));
            
            String fileName = UUID.randomUUID().toString() + extension;

            // 3. Enviar para o MinIO
            // Usamos o InputStream para não carregar o arquivo todo na memória RAM
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType()) // Importante para o navegador saber que é imagem
                    .build()
            );

            // 4. Salvar metadados no banco de dados
            Photo photo = new Photo();
            photo.setUuid(UUID.randomUUID());
            photo.setUser(user);
            photo.setOriginalFileName(originalName);
            photo.setStoredFileName(fileName);
            photo.setContentType(file.getContentType());
            photo.setUploadedAt(LocalDateTime.now());
            photoRepository.save(photo);

            return photo.getUuid().toString(); // Retorna o UUID do arquivo

        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar foto: " + e.getMessage());
        }
    }

    public Page<Photo> getAllPhotos(Long userId, Pageable pageable) {
        return photoRepository.findByUserId(userId, pageable);
    }

    public Photo getPhotoMetadata(UUID uuid, Long userId) {
        return photoRepository.findByUuidAndUserId(uuid, userId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada ou você não tem permissão para acessá-la"));
    }

    public InputStream getPhotoStream(String fileName) {
        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar foto do MinIO: " + e.getMessage());
        }
    }

    public java.util.Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
