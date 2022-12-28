package com.gilclei.cursomc.services;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
	
	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;
	
	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	public void uploadFile(String localFilePath) {
		try {
			File file = new File(localFilePath);
			String fileName = generateFileName(file);
			LOG.info("Iniciando upload: {}",fileName);
			LOG.info("bucketName: {}",bucketName);
			s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
			LOG.info("Upload finalizado");
		}
		catch (AmazonServiceException e) {
			LOG.info("AmazonServiceException: " + e.getErrorMessage());
			LOG.info("Status code: " + e.getErrorCode());
		}
		catch (AmazonClientException e) {
			LOG.info("erro: " +  e.getMessage());
		}
	}
	
	private String generateFileName(File file) {
	    return new Date().getTime() + "-" + file.getName().replace(" ", "_");
	}

}