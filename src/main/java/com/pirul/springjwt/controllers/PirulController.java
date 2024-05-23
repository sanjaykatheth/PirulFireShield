package com.pirul.springjwt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pirul.springjwt.constants.ResponseMessage;
import com.pirul.springjwt.models.PirulRecord;
import com.pirul.springjwt.payload.response.MessageResponse;
import com.pirul.springjwt.security.services.PirulService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pirul")
public class PirulController {

	@Autowired
	private PirulService pirulService;

	@PostMapping("/submit")
	public ResponseEntity<?> submitPirulRecored(@Valid @RequestBody PirulRecord pirulRecord,
			HttpServletRequest request) {
		pirulService.submitPirulData(pirulRecord, request);
		return ResponseEntity.ok(new MessageResponse(ResponseMessage.PIRUL_SUBMISSION_SUCCESS.getMessage()));
	}

	@GetMapping
	public ResponseEntity<Page<PirulRecord>> getAllPirulRecords(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<PirulRecord> records = pirulService.getAllPirulRecords(pageable);
		return ResponseEntity.ok(records);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updatePirulRecord(@PathVariable Long id, @Valid @RequestBody PirulRecord pirulRecord) {
		pirulService.updatePirulRecord(id, pirulRecord);
		return ResponseEntity.ok(ResponseMessage.PIRUL_RECORD_UPDATED_SUCCESSFULLY.getMessage());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePirulRecord(@PathVariable Long id) {
		pirulService.deletePirulRecord(id);
		return ResponseEntity.ok(ResponseMessage.PIRUL_RECORD_DELETED_SUCCESSFULLY.getMessage());
	}

}
