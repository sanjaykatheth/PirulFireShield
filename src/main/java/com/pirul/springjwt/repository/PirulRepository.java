package com.pirul.springjwt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pirul.springjwt.models.PirulRecord;

@Repository
public interface PirulRepository extends JpaRepository<PirulRecord, Long> {

    Page<PirulRecord> findByApprovedTrue(Pageable pageable);

     Page<PirulRecord> findByCreatedBy(String createdBy, Pageable pageable);
    

}
