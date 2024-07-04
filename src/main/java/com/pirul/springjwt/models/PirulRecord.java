package com.pirul.springjwt.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class PirulRecord extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Pattern(regexp = "[0-9]{10}", message = "Mobile number must be 10 digits")
    private String mobileNumber;
    private String location;

    @Pattern(regexp = "[0-9]{12}", message = "Aadhar number must be 12 digits")
    private String aadharNumber;

    @Pattern(regexp = "[0-9]{9,18}", message = "Bank account number must be between 9 and 18 digits")
    private String bankAccountNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ranger_id", nullable = false)
    @JsonBackReference
    private User user;

    private String bankName;

    private String ifscCode;

    private double weightOfPirul;

    private double ratePerKg;

    private double totalAmount;

    private boolean approved;

}
