package com.pirul.springjwt.security.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pirul.springjwt.models.PirulRecord;
import com.pirul.springjwt.repository.PirulRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PirulServiceImpl implements PirulService {

	@Autowired
	private PirulRepository pirulRepository;

	@Override
	public void submitPirulData(PirulRecord pirulRecord, HttpServletRequest request) {
		String createdBy = request.getUserPrincipal().getName();
		pirulRecord.setCreatedBy(createdBy);
		pirulRepository.save(pirulRecord);

	}

	@Override
	public List<PirulRecord> getAllPirulRecord() {
		return null;
	}

}
