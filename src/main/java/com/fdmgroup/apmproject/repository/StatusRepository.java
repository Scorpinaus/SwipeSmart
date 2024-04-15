package com.fdmgroup.apmproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.apmproject.model.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
	
	Optional<Status> findByStatusName(String statusName);
}
