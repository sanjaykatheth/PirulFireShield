package com.pirul.springjwt.controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pirul.springjwt.models.EncryptionRequest;
import com.pirul.springjwt.models.EncryptionResponse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@RestController
@RequestMapping("/encrypt")
public class EncryptionController {

	@Value("${encryption.key}")
	private String key;

	@Value("${encryption.initVector}")
	private String initVector;

	@PostMapping("/credentials")
	public ResponseEntity<?> encryptCredentials(@RequestBody EncryptionRequest encryptionRequest) {
		String encryptedUsername = null;
		String encryptedPassword = null;

		try {
			byte[] initVector = new byte[16];
			new SecureRandom().nextBytes(initVector); // Generate a random IV
			IvParameterSpec iv = new IvParameterSpec(initVector);
			SecretKeySpec skeySpec = new SecretKeySpec(
					MessageDigest.getInstance("SHA-256").digest(key.getBytes("UTF-8")), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			encryptedUsername = Base64.getEncoder()
					.encodeToString(cipher.doFinal(encryptionRequest.getUsername().getBytes()));
			encryptedPassword = Base64.getEncoder()
					.encodeToString(cipher.doFinal(encryptionRequest.getPassword().getBytes()));

			// Include the IV in the EncryptionResponse
			EncryptionResponse encryptionResponse = new EncryptionResponse(
					Base64.getEncoder().encodeToString(initVector), encryptedUsername, encryptedPassword);

			return ResponseEntity.ok(encryptionResponse);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error encrypting credentials");
		}
	}
}
