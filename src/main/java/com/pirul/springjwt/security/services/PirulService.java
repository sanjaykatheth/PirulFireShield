package com.pirul.springjwt.security.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pirul.springjwt.models.PirulRecord;

import jakarta.servlet.http.HttpServletRequest;

public interface PirulService {

	void submitPirulData(PirulRecord pirulRecord, HttpServletRequest request);

	Page<PirulRecord> getAllPirulRecords(Pageable pageable);

	void updatePirulRecord(Long id, PirulRecord pirulRecord);

	void deletePirulRecord(Long id);

}
