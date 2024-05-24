package com.pirul.springjwt.service;

import java.util.List;

import com.pirul.springjwt.models.User;

public interface AdminService {
	
	List<User> getUsersWithRangerRole();

}