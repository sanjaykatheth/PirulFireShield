package com.pirul.springjwt.service;

import com.pirul.springjwt.models.RangerUpdateRequest;
import com.pirul.springjwt.models.Role;
import com.pirul.springjwt.models.User;
import com.pirul.springjwt.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<User> getUsersWithRangerRole(Pageable pageable) {
        logger.info("Fetching users with ranger role - Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_RANGER);
        Page<User> users = userRepository.findByRolesIn(roles, pageable);
        logger.info("Fetched {} users with ranger role", users.getNumberOfElements());
        return users;
    }

    @Override
    @Transactional
    public void deleteRanger(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<Role> roles = user.getRoles();
            for (Role role : roles) {
                if (role == Role.ROLE_RANGER) {
                    userRepository.deleteById(id);
                    logger.info("Deleted Ranger with ID: {}", id);
                    return;
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
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));

        // Check if the user has the role 'ROLE_ADMIN'
        if (user.getRoles().stream().anyMatch(role -> role == Role.ROLE_ADMIN)) {
            throw new IllegalArgumentException("Role of admin user cannot be updated");
        }

        // Validate if the new username already exists
        if (rangerUpdateRequest.getUsername() != null && !user.getUsername().equals(rangerUpdateRequest.getUsername())) {
            Optional<User> existingUser = userRepository.findByUsername(rangerUpdateRequest.getUsername());
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("Username already exists");
            }
        }

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