package com.pirul.springjwt.security.services;

import java.util.Optional;
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
import com.pirul.springjwt.models.User;
import com.pirul.springjwt.repository.PirulRepository;
import com.pirul.springjwt.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PirulServiceImpl implements PirulService {

	@Autowired
	private PirulRepository pirulRepository;

	@Autowired
	UserRepository userRepository;
	
	@Override
	public void submitPirulData(PirulRecord pirulRecord, HttpServletRequest request) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    // Get the UserDetails object from authentication
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    String userId = userDetails.getUsername();
	    System.out.println("userId"+userId);
	    // Fetch the User entity corresponding to the userId
	    User user = userRepository.findByUsername(userId)
	        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + userId));

	    pirulRecord.setUser(user); // Set the user
	    pirulRecord.setCreatedBy(userId);
	    pirulRepository.save(pirulRecord);
	}

	@Override
	public Page<PirulRecord> getAllPirulRecords(Pageable pageable) {
		return pirulRepository.findAll(pageable);

	}

	@Override
	public void updatePirulRecord(Long id, PirulRecord pirulRecord) {
		Optional<PirulRecord> checkRecord = pirulRepository.findById(id);
		if (checkRecord.isPresent()) {
			PirulRecord existingRecord = checkRecord.get();
			existingRecord.setAadharNumber(pirulRecord.getAadharNumber());
			existingRecord.setBankAccountNumber(pirulRecord.getBankAccountNumber());
			existingRecord.setBankName(pirulRecord.getBankName());
			existingRecord.setCreatedBy(pirulRecord.getCreatedBy());
			existingRecord.setIfscCode(pirulRecord.getIfscCode());
			existingRecord.setLocation(pirulRecord.getLocation());
			existingRecord.setMobileNumber(pirulRecord.getMobileNumber());
			existingRecord.setName(pirulRecord.getName());
			existingRecord.setRatePerKg(pirulRecord.getRatePerKg());
			existingRecord.setTotalAmount(pirulRecord.getTotalAmount());
			existingRecord.setWeightOfPirul(pirulRecord.getWeightOfPirul());
			pirulRepository.save(existingRecord);
		} else {
			throw new RuntimeException("record not found with id: " + id);
		}
	}
	
	@Override
	public void deletePirulRecord(Long id) {
	    if (pirulRepository.existsById(id)) {
	        pirulRepository.deleteById(id);
	    } else {
	        throw new IllegalArgumentException(ResponseMessage.RECORD_DOES_NOT_EXIST.getMessage() + " " + id);
	    }
	}
}
