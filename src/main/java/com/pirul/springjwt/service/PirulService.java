package com.pirul.springjwt.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pirul.springjwt.models.PirulRecord;

import jakarta.servlet.http.HttpServletRequest;


public interface PirulService {

	void submitPirulData(PirulRecord pirulRecord,HttpServletRequest request);

	List<PirulRecord> getAllPirulRecord();
}
