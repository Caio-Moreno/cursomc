package com.caiomoreno.cursomc.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class S3Service {

    private Logger log = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private AmazonS3 s3Client;

    @Value("${s3.bucket}")
    private String bucketName;

    public void uploadFile(String filePath) throws IOException {

        try {
            File arquivoOriginal = new File(filePath);
            FileInputStream input = new FileInputStream(arquivoOriginal);

            MultipartFile multipartFile = convertToMultipart(arquivoOriginal.getName(), input);
            File arquivoUpload = new File (Objects.requireNonNull(multipartFile.getOriginalFilename()));

            log.info("Uploading...");
            s3Client.putObject(new PutObjectRequest(bucketName, arquivoOriginal.getName(), arquivoUpload));
            log.info("Upload Realizado com Sucesso...");
        } catch (AmazonServiceException e) {
            log.error("AmazonServiceException " + e.getErrorMessage());
            log.error("Status code: " + e.getErrorCode());
        } catch (AmazonClientException e) {
            log.error("AmazonClientException " + e.getMessage());
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException " + e.getMessage());
        } catch (IOException e) {
            log.error("IOException " + e.getMessage());
        }
    }

    private MultipartFile convertToMultipart(String nome, FileInputStream input) throws IOException {
        return new MockMultipartFile("file",
                nome, "text/plain", IOUtils.toByteArray(input));
    }
}
