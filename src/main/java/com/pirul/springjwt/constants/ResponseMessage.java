package com.pirul.springjwt.constants;

import lombok.Getter;

@Getter
public enum ResponseMessage {
    EMAIL_ALREADY_IN_USE("Email is already in use!"),
    USERNAME_ALREADY_TAKEN("Username is already taken!"),
    USERNAME_NOT_PERSENT("User is not available."),
    

    ROLE_NOT_FOUND("Role is not found."),
    ADMIN_ROLE_NOT_ALLOWED("Admin role is not allowed for signup!"),

    USER_REGISTERED_SUCCESSFULLY("User registered successfully!"),
    INVALID_SIGNUP_ROLE("Invalid role cannot signup"),

    PIRUL_SUBMISSION_SUCCESS("Pirul submission data added successfully"),
    PIRUL_RECORD_UPDATED_SUCCESSFULLY("Pirul record updated successfully"),
    PIRUL_RECORD_DELETED_SUCCESSFULLY("Pirul record deleted successfully"),
    PIRUL_RECORD_DOES_NOT_EXIST("Pirul Records does not exist"),

    PIRUL_RECORD_ALREADY_APPROVED("Pirul record is already approved"),
    PIRUL_RECORD_SUCCESSFULLY_APPROVED("Pirul Record successfully approved"),

    RANGER_DELETED_SUCCESSFULLY("Ranger deleted successfully"),
    RANGER_UPDATED_SUCCESSFULLY("Ranger updated successfully");

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

}