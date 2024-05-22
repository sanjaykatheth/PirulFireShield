package com.pirul.springjwt.security.services;

import java.util.List;

import com.pirul.springjwt.models.PirulRecord;

import jakarta.servlet.http.HttpServletRequest;


public interface PirulService {

	void submitPirulData(PirulRecord pirulRecord,HttpServletRequest request);

	List<PirulRecord> getAllPirulRecord();
}
