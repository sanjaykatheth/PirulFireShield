package com.pirul.springjwt.models;

public class EncryptionResponse {
	private String username;
	private String password;

	public String getUsername() {
		return username;
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

	public EncryptionResponse(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public EncryptionResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

}