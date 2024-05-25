package com.pirul.springjwt.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pirul.springjwt.config.AdminAccess;
import com.pirul.springjwt.constants.ResponseMessage;
import com.pirul.springjwt.models.RangerUpdateRequest;
import com.pirul.springjwt.models.User;
import com.pirul.springjwt.payload.response.MessageResponse;
import com.pirul.springjwt.service.AdminService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@AdminAccess
	@GetMapping
	public Page<User> getUsersWithRangerRole(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		logger.info("Fetching users with ranger role - Page: {}, Size: {}", page, size);
		Pageable pageable = PageRequest.of(page, size);
		Page<User> users = adminService.getUsersWithRangerRole(pageable);
		return users;

	}

	@AdminAccess
	@PatchMapping("/updateRanger/{id}")
	public ResponseEntity<?> updateRanger(@PathVariable Long id, @RequestBody RangerUpdateRequest rangerUpdateRequest) {
	    logger.info("Updating Ranger with ID: {}", id);

	    adminService.updateRanger(id, rangerUpdateRequest);
	    logger.info("Updated Ranger with ID: {}", id);
	    return ResponseEntity.ok()
	            .body(new MessageResponse(ResponseMessage.RANGER_UPDATED_SUCCESSFULLY.getMessage()));
	}


	@AdminAccess
	@DeleteMapping("/deleteRanger/{id}")
	public ResponseEntity<?> deleteRanger(@PathVariable Long id) {
		logger.info("Deleting user with ID: {}", id);
		return adminService.deleteRanger(id);

	}
}
