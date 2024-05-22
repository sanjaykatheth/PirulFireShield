package com.pirul.springjwt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pirul.springjwt.models.PirulRecord;
import com.pirul.springjwt.security.services.PirulService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pirul")
public class PirulController {

	@Autowired
	private PirulService pirulService;

	@PostMapping("/submit")
	public ResponseEntity<?> submitPirulRecored(@Valid @RequestBody PirulRecord pirulRecord,HttpServletRequest request) {
		pirulService.submitPirulData(pirulRecord,request);
		return ResponseEntity.ok("Pirul submission data added successfully");
	}
}
