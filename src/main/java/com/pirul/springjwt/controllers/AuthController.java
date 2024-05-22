package com.pirul.springjwt.controllers;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pirul.springjwt.constants.ErrorMessage;
import com.pirul.springjwt.models.ERole;
import com.pirul.springjwt.models.Role;
import com.pirul.springjwt.models.User;
import com.pirul.springjwt.payload.request.LoginRequest;
import com.pirul.springjwt.payload.request.SignupRequest;
import com.pirul.springjwt.payload.response.JwtResponse;
import com.pirul.springjwt.payload.response.MessageResponse;
import com.pirul.springjwt.repository.RoleRepository;
import com.pirul.springjwt.repository.UserRepository;
import com.pirul.springjwt.security.jwt.JwtUtils;
import com.pirul.springjwt.security.services.UserDetailsImpl;
import com.pirul.springjwt.utils.SHA256Hasher;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private SHA256Hasher hash;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
			throws NoSuchPaddingException {

		String key = "secret123"; // original key
		String decryptedUsername = null;
		String decryptedPassword = null;

		try {
			byte[] initVector = Base64.getDecoder().decode(loginRequest.getInitVector()); // Get the IV from the request
			IvParameterSpec iv = new IvParameterSpec(initVector);
			SecretKeySpec skeySpec = new SecretKeySpec(
					MessageDigest.getInstance("SHA-256").digest(key.getBytes("UTF-8")), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			decryptedUsername = new String(cipher.doFinal(Base64.getDecoder().decode(loginRequest.getUsername())));
			decryptedPassword = new String(cipher.doFinal(Base64.getDecoder().decode(loginRequest.getPassword())));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | IllegalBlockSizeException
				| InvalidKeyException | BadPaddingException | InvalidAlgorithmParameterException
				| NoSuchPaddingException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error decrypting credentials");
		}

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(decryptedUsername, decryptedPassword));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(
				new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
	    // Check if the requested role is allowed for signup
	    if (!signUpRequest.getRole().equals(ERole.ROLE_RANGER.toString())) {
	        return ResponseEntity.badRequest()
	                .body(new MessageResponse(ErrorMessage.INVALID_SIGNUP_ROLE.getMessage()));
	    }

	    // Proceed with other checks only if the role is valid

	    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
	        return ResponseEntity.badRequest()
	                .body(new MessageResponse(ErrorMessage.USERNAME_ALREADY_TAKEN.getMessage()));
	    }

	    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
	        return ResponseEntity.badRequest()
	                .body(new MessageResponse(ErrorMessage.EMAIL_ALREADY_IN_USE.getMessage()));
	    }

	    // Create new user's account
	    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
	            encoder.encode(signUpRequest.getPassword()));

	    Set<Role> roles = new HashSet<>();

	    // Add the ranger role to the user
	    Role rangerRole = roleRepository.findByName(ERole.ROLE_RANGER)
	            .orElseThrow(() -> new RuntimeException(ErrorMessage.ROLE_NOT_FOUND.getMessage()));
	    roles.add(rangerRole);

	    user.setRoles(roles);
	    userRepository.save(user);

	    return ResponseEntity.ok(new MessageResponse(ErrorMessage.USER_REGISTERED_SUCCESSFULLY.getMessage()));
	}


}