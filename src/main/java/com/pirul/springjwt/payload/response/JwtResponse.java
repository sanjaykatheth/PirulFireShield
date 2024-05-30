package com.pirul.springjwt.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private final List<String> roles;
    private String token;
    private Long id;
    private String username;
    private String email;
}
