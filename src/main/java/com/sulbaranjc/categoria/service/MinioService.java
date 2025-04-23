package com.sulbaranjc.categoria.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class MinioService {

    private static final int SEGUNDOS_POR_MINUTO = 60;

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    public MinioService(
            @Value("${minio.url}") String url,
            @Value("${minio.accessKey}") String accessKey,
            @Value("${minio.secretKey}") String secretKey
    ) {
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    public String subirArchivo(MultipartFile archivo) throws Exception {
        try (InputStream inputStream = archivo.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(archivo.getOriginalFilename())
                            .stream(inputStream, archivo.getSize(), -1)
                            .contentType(archivo.getContentType())
                            .build()
            );
            return "Archivo subido correctamente: " + archivo.getOriginalFilename();
        } catch (Exception e) {
            throw new RuntimeException("Error al subir el archivo: " + e.getMessage(), e);
        }
    }

    public String generarPresignedUrl(String objectName, int minutosExpiracion) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)  // Usamos directamente el @Value
                            .object(objectName)
                            .expiry(minutosExpiracion * SEGUNDOS_POR_MINUTO)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error generando URL presignada: " + e.getMessage(), e);
        }
    }
}
