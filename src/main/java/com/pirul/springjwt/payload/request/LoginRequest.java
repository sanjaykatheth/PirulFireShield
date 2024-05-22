package com.pirul.springjwt.payload.request;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	public String getUsername() {
		return username;
	}

	private String initVector;

	public String getInitVector() {
		return this.initVector;
	}

	public void setInitVector(String initVector) {
		this.initVector = initVector;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
