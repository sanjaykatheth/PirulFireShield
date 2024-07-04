package com.pirul.springjwt.repository;

import com.pirul.springjwt.models.PirulRecord;
import com.pirul.springjwt.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PirulRepository extends JpaRepository<PirulRecord, Long> {

    Page<PirulRecord> findByApprovedTrue(Pageable pageable);

    Page<PirulRecord> findByUser(User user, Pageable pageable);

}
