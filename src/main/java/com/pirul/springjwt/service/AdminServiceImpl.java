package com.pirul.springjwt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pirul.springjwt.models.ERole;
import com.pirul.springjwt.models.User;
import com.pirul.springjwt.repository.UserRepository;

public class AdminServiceImpl implements AdminService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> getUsersWithRangerRole() {

		return userRepository.findByRolesName(ERole.ROLE_RANGER);
	}

}
