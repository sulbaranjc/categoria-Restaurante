package com.sulbaranjc.categoria.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class MinioService {

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
        InputStream inputStream = archivo.getInputStream();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(archivo.getOriginalFilename())
                        .stream(inputStream, archivo.getSize(), -1)
                        .contentType(archivo.getContentType())
                        .build()
        );

        inputStream.close();
        return "Archivo subido correctamente: " + archivo.getOriginalFilename();
    }
}
