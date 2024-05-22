package com.pirul.springjwt.constants;

public enum ErrorMessage {

	EMAIL_ALREADY_IN_USE("Email is already in use!"), USERNAME_ALREADY_TAKEN("Username is already taken!"),
	ROLE_NOT_FOUND("Error: Role is not found."), 
	ADMIN_ROLE_NOT_ALLOWED("Error: Admin role is not allowed for signup!"),
	USER_REGISTERED_SUCCESSFULLY("User registered successfully!"),INVALID_SIGNUP_ROLE("invalid role can not signup");

	private final String message;

	ErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
