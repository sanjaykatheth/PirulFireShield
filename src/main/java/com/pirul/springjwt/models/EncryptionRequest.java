package com.pirul.springjwt.models;

import lombok.Data;

@Data
public class EncryptionRequest {
    private String username;
    private String password;
}