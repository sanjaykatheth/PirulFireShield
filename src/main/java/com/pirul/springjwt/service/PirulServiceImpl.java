package com.pirul.springjwt.service;

import com.pirul.springjwt.constants.ResponseMessage;
import com.pirul.springjwt.exception.ResourceNotFoundException;
import com.pirul.springjwt.models.PirulRecord;
import com.pirul.springjwt.models.PirulRecordDTO;
import com.pirul.springjwt.models.Role;
import com.pirul.springjwt.models.User;
import com.pirul.springjwt.repository.PirulRepository;
import com.pirul.springjwt.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PirulServiceImpl implements PirulService {

    private static final Logger logger = LoggerFactory.getLogger(PirulServiceImpl.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PirulRepository pirulRepository;
    @Autowired
    private Validator validator;

    @Override
    public void submitPirulData(PirulRecord pirulRecord, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();
        User user = userRepository.findByUsername(userId).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + userId));
        pirulRecord.setUser(user);
        pirulRepository.save(pirulRecord);
    }

    @Override
    public Page<PirulRecord> getAllPirulRecords(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        Page<PirulRecord> records;
        if (roles.contains(Role.ROLE_ADMIN.toString())) {
            records = pirulRepository.findAll(pageable);
        } else if (roles.contains(Role.ROLE_RANGER.toString())) {
            records = pirulRepository.findByUser(user, pageable);
        } else {
            throw new ResourceNotFoundException("You do not have permission to access this resource");
        }

        return records;
    }

    @Override
    public void updatePirulRecord(Long id, PirulRecordDTO pirulRecordDTO) {
        Optional<PirulRecord> checkRecord = pirulRepository.findById(id);
        if (checkRecord.isPresent()) {
            PirulRecord existingRecord = checkRecord.get();

            if (pirulRecordDTO.getName() != null) {
                existingRecord.setName(pirulRecordDTO.getName());
            }
            if (pirulRecordDTO.getMobileNumber() != null) {
                existingRecord.setMobileNumber(pirulRecordDTO.getMobileNumber());
            }
            if (pirulRecordDTO.getLocation() != null) {
                existingRecord.setLocation(pirulRecordDTO.getLocation());
            }
            if (pirulRecordDTO.getAadhaarNumber() != null) {
                existingRecord.setAadharNumber(pirulRecordDTO.getAadhaarNumber());
            }
            if (pirulRecordDTO.getBankAccountNumber() != null) {
                existingRecord.setBankAccountNumber(pirulRecordDTO.getBankAccountNumber());
            }
            if (pirulRecordDTO.getBankName() != null) {
                existingRecord.setBankName(pirulRecordDTO.getBankName());
            }
            if (pirulRecordDTO.getIfscCode() != null) {
                existingRecord.setIfscCode(pirulRecordDTO.getIfscCode());
            }
            if (pirulRecordDTO.getWeightOfPirul() != Double.MIN_VALUE) { // Use a default value for comparison
                existingRecord.setWeightOfPirul(pirulRecordDTO.getWeightOfPirul());
            }
            if (pirulRecordDTO.getRatePerKg() != Double.MIN_VALUE) {
                existingRecord.setRatePerKg(pirulRecordDTO.getRatePerKg());
            }
            if (pirulRecordDTO.getTotalAmount() != Double.MIN_VALUE) {
                existingRecord.setTotalAmount(pirulRecordDTO.getTotalAmount());
            }
            if (pirulRecordDTO.getCreatedBy() != null) {
                existingRecord.setCreatedBy(pirulRecordDTO.getCreatedBy());
            }

            // Validate the updated entity
            Set<ConstraintViolation<PirulRecord>> violations = validator.validate(existingRecord);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }

            pirulRepository.save(existingRecord);
            logger.info("Pirul Record with ID {} updated successfully", id);
        } else {
            throw new IllegalArgumentException(ResponseMessage.PIRUL_RECORD_DOES_NOT_EXIST.getMessage());
        }
    }

    @Override
    @Transactional
    public void deletePirulRecord(Long id) {
        logger.info("Deleting Pirul Record with ID: {}", id);
        if (pirulRepository.existsById(id)) {
            pirulRepository.deleteById(id);
            logger.info("Pirul Record with ID {} deleted successfully", id);
        } else {
            throw new IllegalArgumentException(ResponseMessage.PIRUL_RECORD_DOES_NOT_EXIST.getMessage() + " " + id);
        }
    }

    @Override
    @Transactional
    public ResponseMessage approvePirulRecord(Long recordId) {
        logger.info("Approving Pirul Record with ID: {}", recordId);
        Optional<PirulRecord> optionalPirulRecord = pirulRepository.findById(recordId);
        if (optionalPirulRecord.isPresent()) {
            PirulRecord pirulRecord = optionalPirulRecord.get();
            if (pirulRecord.isApproved()) {
                String message = "Pirul Record with ID " + recordId + " is already approved by User ID " + pirulRecord.getUser().getId();
                logger.warn(message);
                return ResponseMessage.PIRUL_RECORD_ALREADY_APPROVED;
            } else {
                pirulRecord.setApproved(true);
                pirulRepository.save(pirulRecord);
                Long userId = pirulRecord.getUser().getId(); // Assuming getUser() returns the User entity and getId()
                // returns the User ID
                String message = "Pirul Record with ID " + recordId + " approved successfully by User ID " + userId;
                logger.info(message);
                return ResponseMessage.PIRUL_RECORD_SUCCESSFULLY_APPROVED;
            }
        } else {
            logger.error("Pirul Record with ID {} not found", recordId);
            throw new ResourceNotFoundException("PirulRecord not found with id " + recordId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PirulRecord> getApprovedPirulRecords(Pageable pageable) {
        return pirulRepository.findByApprovedTrue(pageable);
    }
}
