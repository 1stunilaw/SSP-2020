package ssp.marketplace.app.service;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.web.multipart.MultipartFile;

public interface S3Services {

    S3ObjectInputStream downloadFile(String filename, String pathS3);

    void uploadFile(MultipartFile multipartFile, String uniqueName, String pathS3);
}
