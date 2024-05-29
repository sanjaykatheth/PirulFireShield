package com.pirul.springjwt.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pirul.springjwt.constants.ResponseMessage;
import com.pirul.springjwt.exception.ResourceNotFoundException;
import com.pirul.springjwt.models.ERole;
import com.pirul.springjwt.models.PirulRecord;
import com.pirul.springjwt.models.PirulRecordDTO;
import com.pirul.springjwt.models.User;
import com.pirul.springjwt.repository.PirulRepository;
import com.pirul.springjwt.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@Service
public class RangerServiceImpl implements RangerService {

	private static final Logger logger = LoggerFactory.getLogger(RangerServiceImpl.class);

	@Autowired
	private PirulRepository pirulRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private Validator validator;

	@Override
	public void submitPirulData(PirulRecord pirulRecord, HttpServletRequest request) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String userId = userDetails.getUsername();
		User user = userRepository.findByUsername(userId)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + userId));

		pirulRecord.setUser(user);
		pirulRecord.setCreatedBy(userId);
		pirulRecord.setUpdatedBy(userId);
		pirulRepository.save(pirulRecord);
		logger.info("Pirul Record submitted successfully by user: {}", userId);
	}

	@Override
	public Page<PirulRecord> getAllPirulRecords(Pageable pageable) {
		logger.info("Fetching all Pirul Records");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		String username = userDetails.getUsername();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		Page<PirulRecord> records;
		if (roles.contains(ERole.ROLE_ADMIN.toString())) {
			records = pirulRepository.findAll(pageable);
		} else if (roles.contains(ERole.ROLE_RANGER.toString())) {
			records = pirulRepository.findByCreatedBy(username, pageable);
		} else {
			throw new ResourceNotFoundException("You do not have permission to access this resource");
		}

		return records;
	}

	@Override
	public void updatePirulRecord(Long id, PirulRecordDTO pirulRecordDTO) {
		logger.info("Updating Pirul Record with ID: {}", id);
		Optional<PirulRecord> checkRecord = pirulRepository.findById(id);
		if (checkRecord.isPresent()) {
			PirulRecord existingRecord = checkRecord.get();

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String userId = userDetails.getUsername();

			if (pirulRecordDTO.getName() != null) {
				existingRecord.setName(pirulRecordDTO.getName());
			}
			if (pirulRecordDTO.getMobileNumber() != null) {
				existingRecord.setMobileNumber(pirulRecordDTO.getMobileNumber());
			}
			if (pirulRecordDTO.getLocation() != null) {
				existingRecord.setLocation(pirulRecordDTO.getLocation());
			}
			if (pirulRecordDTO.getAadharNumber() != null) {
				existingRecord.setAadharNumber(pirulRecordDTO.getAadharNumber());
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
			existingRecord.setUpdatedBy(userId);

			// Validate the updated entity
			Set<ConstraintViolation<PirulRecord>> violations = validator.validate(existingRecord);
			if (!violations.isEmpty()) {
				throw new ConstraintViolationException(violations);
			}

			pirulRepository.save(existingRecord);
			logger.info("Pirul Record with ID {} updated successfully", id);
		} else {
			throw new IllegalArgumentException(ResponseMessage.RECORD_DOES_NOT_EXIST.getMessage());
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
			throw new IllegalArgumentException(ResponseMessage.RECORD_DOES_NOT_EXIST.getMessage() + " " + id);
		}
	}

	@Override
	@Transactional
	public String approvePirulRecord(Long recordId) {
		logger.info("Approving Pirul Record with ID: {}", recordId);
		Optional<PirulRecord> optionalPirulRecord = pirulRepository.findById(recordId);
		if (optionalPirulRecord.isPresent()) {
			PirulRecord pirulRecord = optionalPirulRecord.get();
			if (pirulRecord.isApproved()) {
				String message = "Pirul Record with ID " + recordId + " is already approved by User ID "
						+ pirulRecord.getUser().getId();
				logger.warn(message);
				return message;
			} else {
				pirulRecord.setApproved(true);
				pirulRepository.save(pirulRecord);
				Long userId = pirulRecord.getUser().getId(); // Assuming getUser() returns the User entity and getId()
																// returns the User ID
				String message = "Pirul Record with ID " + recordId + " approved successfully by User ID " + userId;
				logger.info(message);
				return message;
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
