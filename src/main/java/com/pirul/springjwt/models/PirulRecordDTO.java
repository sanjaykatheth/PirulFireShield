package com.pirul.springjwt.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PirulRecordDTO {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Pattern(regexp="[0-9]{10}", message="Mobile number must be 10 digits")
    private String mobileNumber;

    private String location;

    @Pattern(regexp="[0-9]{12}", message="Aadhar number must be 12 digits")
    private String aadharNumber;

    @Pattern(regexp="[0-9]{9,18}", message="Bank account number must be between 9 and 18 digits")
    private String bankAccountNumber;

	private String bankName;
	
	private String ifscCode;
	
	private double weightOfPirul;
	
	private double ratePerKg;
	
	private double totalAmount;
	
    private String createdBy;
    
    private boolean approved;

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public double getWeightOfPirul() {
        return weightOfPirul;
    }

    public void setWeightOfPirul(double weightOfPirul) {
        this.weightOfPirul = weightOfPirul;
    }

    public double getRatePerKg() {
        return ratePerKg;
    }

    public void setRatePerKg(double ratePerKg) {
        this.ratePerKg = ratePerKg;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
    
}
