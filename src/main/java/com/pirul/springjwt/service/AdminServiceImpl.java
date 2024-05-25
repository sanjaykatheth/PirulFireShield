package com.pirul.springjwt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pirul.springjwt.models.ERole;
import com.pirul.springjwt.models.User;
import com.pirul.springjwt.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	

	@Override
	public Page<User> getUsersWithRangerRole(Pageable pageable) {
		 logger.info("Fetching users with ranger role - Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());

	        Page<User> users = userRepository.findByRolesName(ERole.ROLE_RANGER, pageable);

	        logger.info("Fetched {} users with ranger role", users.getNumberOfElements());
	        return users;
	    }
}