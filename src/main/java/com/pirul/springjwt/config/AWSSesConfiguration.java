package com.pirul.springjwt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;

@Configuration
public class AWSSesConfiguration {

	@Value("${aws.access.key}")
	private String awsAccessKey;

	@Value("${aws.secret.key}")
	private String awsSecretKey;

	@Value("${spring.mail.host}")
	private String host;

//	@Value("${spring.mail.username}")
//	private String userName;
//
//	@Value("${spring.mail.password}")
//	private String password;

	@Bean
	public AmazonSimpleEmailService amazonSimpleEmailService() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
		return AmazonSimpleEmailServiceClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_1).build();
	}
}
