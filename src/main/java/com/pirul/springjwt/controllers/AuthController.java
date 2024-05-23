package com.pirul.springjwt.controllers;

import java.io.UnsupportedEncodingException;
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
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
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

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	 @Value("${encryption.key}")
	 private String encryptionKey;
	 
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
			throws NoSuchPaddingException {
		String decryptedUsername = null;
		String decryptedPassword = null;

//		try {
//			SecretKeySpec skeySpec = new SecretKeySpec(
//                    MessageDigest.getInstance("SHA-256").digest(encryptionKey.getBytes("UTF-8")), "AES");
//            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
//            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
//			decryptedUsername = new String(cipher.doFinal(Base64.getDecoder().decode(loginRequest.getUsername())));
//			decryptedPassword = new String(cipher.doFinal(Base64.getDecoder().decode(loginRequest.getPassword())));
//		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | IllegalBlockSizeException
//				| InvalidKeyException | BadPaddingException
//				| NoSuchPaddingException e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error decrypting credentials");
//		}

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
	    JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setCacheControl(CacheControl.noCache().getHeaderValue()); // Disable caching
	    headers.set("Authorization", "Bearer " + jwt); // Set JWT in Authorization header

	    // Check if token nearing expiration, refresh if needed and set in response header
	    if (jwtUtils.isTokenNearExpiration(jwt)) {
	        String refreshedToken = jwtUtils.generateJwtToken(authentication);
	        headers.set("Authorization", "Bearer " + refreshedToken);
	    }
	    return ResponseEntity.ok().headers(headers).body(jwtResponse);

	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
	    if (!signUpRequest.getRole().equals(ERole.ROLE_RANGER.toString())) {
	        return ResponseEntity.badRequest()
	                .body(new MessageResponse(ErrorMessage.INVALID_SIGNUP_ROLE.getMessage()));
	    }
	    
	    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
	        return ResponseEntity.badRequest()
	                .body(new MessageResponse(ErrorMessage.USERNAME_ALREADY_TAKEN.getMessage()));
	    }

	    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
	        return ResponseEntity.badRequest()
	                .body(new MessageResponse(ErrorMessage.EMAIL_ALREADY_IN_USE.getMessage()));
	    }
	    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
	            encoder.encode(signUpRequest.getPassword()));

	    Set<Role> roles = new HashSet<>();

	    Role rangerRole = roleRepository.findByName(ERole.ROLE_RANGER)
	            .orElseThrow(() -> new RuntimeException(ErrorMessage.ROLE_NOT_FOUND.getMessage()));
	    roles.add(rangerRole);

	    user.setRoles(roles);
	    userRepository.save(user);

	    return ResponseEntity.ok(new MessageResponse(ErrorMessage.USER_REGISTERED_SUCCESSFULLY.getMessage()));
	}


}