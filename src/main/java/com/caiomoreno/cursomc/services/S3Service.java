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

@Service
public class S3Service {

    private Logger log = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private AmazonS3 s3Client;

    @Value("${s3.bucket}")
    private String bucketName;

    public void uploadFile(String filePath) throws IOException {



        try {
            log.info("Buscando o arquivo...");
            File file = new File(filePath);
            FileInputStream input = new FileInputStream(file);

            log.info("Transforma em MultipartFile...");
            MultipartFile multipartFile = new MockMultipartFile("file",
                    file.getName(), "text/plain", IOUtils.toByteArray(input));

            File arquivoUpload = new File (multipartFile.getOriginalFilename());
            FileOutputStream outputStream = new FileOutputStream(arquivoUpload);
            outputStream.write(multipartFile.getBytes());

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, "novaCoisa.png",arquivoUpload);
            log.info("Uploading...");
            s3Client.putObject(putObjectRequest);
            log.info("Upload Realizado com Sucesso...");

            log.info("UploadFinalizado");
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
}
