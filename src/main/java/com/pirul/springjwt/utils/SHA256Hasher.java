package com.pirul.springjwt.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component	
public class SHA256Hasher {
	public static String hashData(String data) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hashedBytes = digest.digest(data.getBytes());
		return Base64.getEncoder().encodeToString(hashedBytes);
	}
}