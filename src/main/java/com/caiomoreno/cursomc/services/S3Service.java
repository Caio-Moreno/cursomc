package com.caiomoreno.cursomc.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.caiomoreno.cursomc.services.exceptions.FileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class S3Service {

    private Logger log = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private AmazonS3 s3Client;

    @Value("${s3.bucket}")
    private String bucketName;

    public URI uploadFile(MultipartFile multipartFile) {
        try {
            String filename = multipartFile.getOriginalFilename();
            InputStream is = multipartFile.getInputStream();
            String contentType = multipartFile.getContentType();
            return uploadFile(is, filename, contentType);
        } catch (IOException e) {
            throw new FileException("Erro de IO" + e.getMessage());
        }
    }

    public URI uploadFile(InputStream is, String filename, String contentType) {
        try {
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType(contentType);
            log.info("Uploading...");
            s3Client.putObject(bucketName, filename, is, meta);
            log.info("Upload Realizado com Sucesso...");
            return s3Client.getUrl(bucketName, filename).toURI();
        } catch (URISyntaxException e) {
            throw new FileException("Erro para converter URL para URI");
        }
    }

}
