package com.pirul.springjwt.service;

import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
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

import com.pirul.springjwt.constants.ResponseMessage;
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
public class PirulServiceImpl implements PirulService {

    private static final Logger logger = LoggerFactory.getLogger(PirulServiceImpl.class);

	@Autowired
	private PirulRepository pirulRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private Validator validator;

	@Autowired
	private ModelMapper modelMapper;
	

	@Override
	public void submitPirulData(PirulRecord pirulRecord, HttpServletRequest request) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String userId = userDetails.getUsername();
		User user = userRepository.findByUsername(userId)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + userId));

		pirulRecord.setUser(user);
		pirulRecord.setCreatedBy(userId);
		pirulRepository.save(pirulRecord);
		logger.info("Pirul Record submitted successfully by user: {}", userId);
	}

	@Override
	public Page<PirulRecord> getAllPirulRecords(Pageable pageable) {
        logger.info("Fetching all Pirul Records");
		return pirulRepository.findAll(pageable);

	}

	@Override
	public void updatePirulRecord(Long id, PirulRecordDTO pirulRecordDTO) {
        logger.info("Updating Pirul Record with ID: {}", id);
		Optional<PirulRecord> checkRecord = pirulRepository.findById(id);
		if (checkRecord.isPresent()) {
			PirulRecord existingRecord = checkRecord.get();

			// Explicitly map only the non-ID fields from the DTO to the existing entity
			modelMapper.typeMap(PirulRecordDTO.class, PirulRecord.class)
					.addMappings(mapper -> mapper.skip(PirulRecord::setId));

			modelMapper.map(pirulRecordDTO, existingRecord);

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
	public void deletePirulRecord(Long id) {
        logger.info("Deleting Pirul Record with ID: {}", id);
		if (pirulRepository.existsById(id)) {
			pirulRepository.deleteById(id);
            logger.info("Pirul Record with ID {} deleted successfully", id);

		} else {
			throw new IllegalArgumentException(ResponseMessage.RECORD_DOES_NOT_EXIST.getMessage() + " " + id);
		}
	}
}
