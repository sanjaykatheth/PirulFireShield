package com.pirul.springjwt.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class PirulRecord {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
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
	public PirulRecord(String name, String mobileNumber, String location, String aadharNumber, String bankAccountNumber,
			String bankName, String ifscCode, double weightOfPirul, double ratePerKg, double totalAmount) {
		super();
		this.name = name;
		this.mobileNumber = mobileNumber;
		this.location = location;
		this.aadharNumber = aadharNumber;
		this.bankAccountNumber = bankAccountNumber;
		this.bankName = bankName;
		this.ifscCode = ifscCode;
		this.weightOfPirul = weightOfPirul;
		this.ratePerKg = ratePerKg;
		this.totalAmount = totalAmount;
	}
	public PirulRecord() {
		super();
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
}
