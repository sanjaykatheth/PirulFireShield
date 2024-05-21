package com.pirul.springjwt.models;


public class EncryptionResponse {
    private String initVector;
	private String decryptedUsername;
	private String decryptedPassword;

	public String getDecryptedUsername() {
		return decryptedUsername;
	}

	public void setDecryptedUsername(String decryptedUsername) {
		this.decryptedUsername = decryptedUsername;
	}

	public String getDecryptedPassword() {
		return decryptedPassword;
	}

	public void setDecryptedPassword(String decryptedPassword) {
		this.decryptedPassword = decryptedPassword;
	}


	public EncryptionResponse(String initVector, String decryptedUsername, String decryptedPassword) {
		super();
		this.initVector = initVector;
		this.decryptedUsername = decryptedUsername;
		this.decryptedPassword = decryptedPassword;
	}

	public EncryptionResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getInitVector() {
		return initVector;
	}

	public void setInitVector(String initVector) {
		this.initVector = initVector;
	}

}
