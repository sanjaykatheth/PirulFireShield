package com.pirul.springjwt.service;

import com.pirul.springjwt.constants.ResponseMessage;
import com.pirul.springjwt.models.PirulRecord;
import com.pirul.springjwt.models.PirulRecordDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PirulService {

    void submitPirulData(PirulRecord pirulRecord, HttpServletRequest request);

    Page<PirulRecord> getAllPirulRecords(Pageable pageable);

    void updatePirulRecord(Long id, PirulRecordDTO pirulRecord);

    void deletePirulRecord(Long id);

    ResponseMessage approvePirulRecord(Long id);

    Page<PirulRecord> getApprovedPirulRecords(Pageable pageable);

}
