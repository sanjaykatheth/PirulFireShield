package com.pirul.springjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pirul.springjwt.models.PirulRecord;

@Repository
public interface PirulRepository extends JpaRepository<PirulRecord, Long> {

}
