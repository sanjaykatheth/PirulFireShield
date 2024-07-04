package com.pirul.springjwt.controllers;

import com.pirul.springjwt.config.AdminAccess;
import com.pirul.springjwt.constants.ResponseMessage;
import com.pirul.springjwt.models.PirulRecord;
import com.pirul.springjwt.models.RangerUpdateRequest;
import com.pirul.springjwt.models.User;
import com.pirul.springjwt.payload.response.MessageResponse;
import com.pirul.springjwt.service.PirulService;
import com.pirul.springjwt.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.pirul.springjwt.constants.ResponseMessage.RANGER_DELETED_SUCCESSFULLY;
import static com.pirul.springjwt.constants.ResponseMessage.RANGER_UPDATED_SUCCESSFULLY;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private PirulService pirulService;

    @AdminAccess
    @GetMapping("/rangers")
    public Page<User> getUsersWithRangerRole(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        logger.info("Fetching users with ranger role - Page: {}, Size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return userService.getUsersWithRangerRole(pageable);
    }

    @AdminAccess
    @PatchMapping("/rangers/{id}")
    public ResponseEntity<?> updateRanger(@PathVariable Long id, @RequestBody RangerUpdateRequest rangerUpdateRequest) {
        logger.info("Updating Ranger with ID: {}", id);
        userService.updateRanger(id, rangerUpdateRequest);
        return ResponseEntity.ok(new MessageResponse(RANGER_UPDATED_SUCCESSFULLY));
    }

    @AdminAccess
    @DeleteMapping("/rangers/{id}")
    public ResponseEntity<?> deleteRanger(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);
        userService.deleteRanger(id);
        return ResponseEntity.ok(new MessageResponse(RANGER_DELETED_SUCCESSFULLY));
    }


    @AdminAccess
    @PostMapping("/pirul-record/{id}/approve")
    public ResponseEntity<?> approvePirulRecord(@PathVariable Long id) {
        logger.info("Approving Pirul Record with ID: {}", id);
        ResponseMessage message = pirulService.approvePirulRecord(id);
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @AdminAccess
    @GetMapping("/pirul-records/approved")
    public ResponseEntity<Page<PirulRecord>> getApprovedPirulRecords(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        logger.info("Fetching all approved Pirul Records - Page: {}, Size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<PirulRecord> approvedPirulRecords = pirulService.getApprovedPirulRecords(pageable);
        return ResponseEntity.ok(approvedPirulRecords);
    }

}
