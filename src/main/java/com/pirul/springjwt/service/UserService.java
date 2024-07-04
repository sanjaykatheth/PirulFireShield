package com.pirul.springjwt.service;

import com.pirul.springjwt.models.RangerUpdateRequest;
import com.pirul.springjwt.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<User> getUsersWithRangerRole(Pageable pageable);

    void deleteRanger(Long id);

    void updateRanger(Long id, RangerUpdateRequest rangerUpdateRequest);

}