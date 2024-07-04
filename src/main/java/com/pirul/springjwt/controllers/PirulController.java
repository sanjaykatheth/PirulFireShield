package com.pirul.springjwt.controllers;

import com.pirul.springjwt.constants.ResponseMessage;
import com.pirul.springjwt.models.PirulRecord;
import com.pirul.springjwt.models.PirulRecordDTO;
import com.pirul.springjwt.payload.response.MessageResponse;
import com.pirul.springjwt.service.PirulService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/pirul")
public class PirulController {

    @Autowired
    private PirulService pirulService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitPirulRecord(@Valid @RequestBody PirulRecord pirulRecord, HttpServletRequest request) {
        pirulService.submitPirulData(pirulRecord, request);
        return ResponseEntity.ok(new MessageResponse(ResponseMessage.PIRUL_SUBMISSION_SUCCESS));
    }

    @GetMapping
    public ResponseEntity<Page<PirulRecord>> getAllPirulRecords(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PirulRecord> records = pirulService.getAllPirulRecords(pageable);
        return ResponseEntity.ok(records);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePirulRecord(@PathVariable Long id, @Valid @RequestBody PirulRecordDTO pirulRecord) {
        pirulService.updatePirulRecord(id, pirulRecord);
        return ResponseEntity.ok(new MessageResponse(ResponseMessage.PIRUL_RECORD_UPDATED_SUCCESSFULLY));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePirulRecord(@PathVariable Long id) {
        pirulService.deletePirulRecord(id);
        return ResponseEntity.ok(new MessageResponse(ResponseMessage.PIRUL_RECORD_DELETED_SUCCESSFULLY));
    }

}
