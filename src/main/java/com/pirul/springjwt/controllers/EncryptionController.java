package com.pirul.springjwt.controllers;

import com.pirul.springjwt.models.EncryptionRequest;
import com.pirul.springjwt.models.EncryptionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;


@RestController
@RequestMapping("/encrypt")
public class EncryptionController {

    @Value("${encryption.key}")
    private String key;

    @PostMapping("/credentials")
    public ResponseEntity<?> encryptCredentials(@RequestBody EncryptionRequest encryptionRequest) {
        String encryptedUsername = null;
        String encryptedPassword = null;

        try {
            SecretKeySpec skeySpec = new SecretKeySpec(MessageDigest.getInstance("SHA-256").digest(key.getBytes(StandardCharsets.UTF_8)), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            encryptedUsername = Base64.getEncoder().encodeToString(cipher.doFinal(encryptionRequest.getUsername().getBytes()));
            encryptedPassword = Base64.getEncoder().encodeToString(cipher.doFinal(encryptionRequest.getPassword().getBytes()));
            EncryptionResponse encryptionResponse = new EncryptionResponse(encryptedUsername, encryptedPassword);
            return ResponseEntity.ok(encryptionResponse);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error encrypting credentials");
        }
    }
}