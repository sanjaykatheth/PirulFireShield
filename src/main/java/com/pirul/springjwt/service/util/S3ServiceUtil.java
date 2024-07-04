package com.pirul.springjwt.service.util;

import java.io.File;
import java.io.IOException;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3ServiceUtil {

	@Autowired
	private AmazonS3 s3Client;

	@Value("${aws.s3.bucket.name}")
	private String bucketName;

	public void uploadFile(String fileName, File file) throws IOException {
		final String uniqueFileName = LocalDateTime.now() + "_" + file.getName();
		final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName+""+"/piruldata", uniqueFileName, file);
		s3Client.putObject(putObjectRequest);
	}

}