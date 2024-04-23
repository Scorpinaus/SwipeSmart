package com.fdmgroup.apmproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.apmproject.model.Status;

/**
 * This interface extends the JpaRepository interface to provide additional methods for accessing and manipulating Status entities.
 * 
 * @author Bard
 * @version 1.0
 * @since 2024-04-22
 */
@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {

    /**
     * Finds a status by its status name.
     *
     * @param statusName The status name.
     * @return An optional Status object.
     */
    Optional<Status> findByStatusName(String statusName);
}