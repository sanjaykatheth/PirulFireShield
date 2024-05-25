package com.pirul.springjwt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pirul.springjwt.models.User;

public interface AdminService {
    Page<User> getUsersWithRangerRole(Pageable pageable);
}