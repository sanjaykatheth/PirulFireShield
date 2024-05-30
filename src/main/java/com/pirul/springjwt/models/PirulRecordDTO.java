package com.pirul.springjwt.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PirulRecordDTO {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Pattern(regexp = "[0-9]{10}", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    private String location;

    @Pattern(regexp = "[0-9]{12}", message = "Aadhar number must be 12 digits")
    private String aadhaarNumber;

    @Pattern(regexp = "[0-9]{9,18}", message = "Bank account number must be between 9 and 18 digits")
    private String bankAccountNumber;

    private String bankName;

    private String ifscCode;

    private double weightOfPirul;

    private double ratePerKg;

    private double totalAmount;

    private String createdBy;

    private boolean approved;

    // Getters and setters

}
