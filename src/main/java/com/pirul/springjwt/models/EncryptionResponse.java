package com.pirul.springjwt.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EncryptionResponse {
    private String username;
    private String password;
}