package com.pirul.springjwt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pirul.springjwt.models.PirulRecord;
import com.pirul.springjwt.models.PirulRecordDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface RangerService {

	void submitPirulData(PirulRecord pirulRecord, HttpServletRequest request);

	Page<PirulRecord> getAllPirulRecords(Pageable pageable);

	void updatePirulRecord(Long id, PirulRecordDTO pirulRecord);

	void deletePirulRecord(Long id);

}
