package com.pirul.springjwt.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

@Service
public class AWSSesEmailService {

	@Autowired
	private AmazonSimpleEmailService amazonSESClient;

	public void sendEmail(String to, String subject, String text) {
		SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(to))
				.withMessage(
						new Message().withBody(new Body().withText(new Content().withCharset("UTF-8").withData(text)))
								.withSubject(new Content().withCharset("UTF-8").withData(subject)))
				.withSource("sanjaykathethkk0135@gmail.com"); 

		try {
			amazonSESClient.sendEmail(request);
			System.out.println("Email sent successfully to " + to);
		} catch (Exception ex) {
			System.out.println("The email was not sent. Error message: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
