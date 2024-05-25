package com.pirul.springjwt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.pirul.springjwt.models.RangerUpdateRequest;
import com.pirul.springjwt.models.User;

public interface AdminService {
	Page<User> getUsersWithRangerRole(Pageable pageable);

	ResponseEntity<?> deleteRanger(Long id);

	void updateRanger(Long id, RangerUpdateRequest rangerUpdateRequest);
}