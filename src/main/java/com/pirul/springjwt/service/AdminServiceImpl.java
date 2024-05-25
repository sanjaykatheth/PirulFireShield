package com.pirul.springjwt.service;

import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pirul.springjwt.constants.ResponseMessage;
import com.pirul.springjwt.models.ERole;
import com.pirul.springjwt.models.RangerUpdateRequest;
import com.pirul.springjwt.models.Role;
import com.pirul.springjwt.models.User;
import com.pirul.springjwt.payload.response.MessageResponse;
import com.pirul.springjwt.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {

	private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Page<User> getUsersWithRangerRole(Pageable pageable) {
		logger.info("Fetching users with ranger role - Page: {}, Size: {}", pageable.getPageNumber(),
				pageable.getPageSize());

		Page<User> users = userRepository.findByRolesName(ERole.ROLE_RANGER, pageable);

		logger.info("Fetched {} users with ranger role", users.getNumberOfElements());
		return users;
	}

	@Override
	@Transactional
	public ResponseEntity<?> deleteRanger(Long id) {
		logger.info("Deleting Ranger with ID: {}", id);

		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			Set<Role> roles = user.getRoles();
			for (Role role : roles) {
				if (role.getName() == ERole.ROLE_RANGER) {
					userRepository.deleteById(id);
					logger.info("Deleted Ranger with ID: {}", id);
					return ResponseEntity
							.ok(new MessageResponse(ResponseMessage.RANGER_DELETED_SUCCESSFULLY.getMessage()));
				}
			}
		}

		throw new IllegalArgumentException("Ranger with ID " + id + " does not exist or cannot be deleted.");
	}

	@Override
	@Transactional
	public void updateRanger(Long id, RangerUpdateRequest rangerUpdateRequest) {
		logger.info("Updating Ranger with ID: {}", id);

		// Find the user by id
		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));

		// Log the fields being updated directly
		if (rangerUpdateRequest.getUsername() != null) {
			user.setUsername(rangerUpdateRequest.getUsername());
		}
		if (rangerUpdateRequest.getEmail() != null) {
			user.setEmail(rangerUpdateRequest.getEmail());
		}
		if (rangerUpdateRequest.getPassword() != null) {
			String maskedPassword = "*** (masked)"; 
			user.setPassword(passwordEncoder.encode(rangerUpdateRequest.getPassword()));
			logger.info("Updated password for Ranger with ID {}: {}", id, maskedPassword);
		}
		userRepository.save(user);
		logger.info("Ranger with ID {} updated successfully", id);
	}

}