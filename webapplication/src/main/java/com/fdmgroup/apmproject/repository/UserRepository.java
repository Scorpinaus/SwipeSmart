package com.fdmgroup.apmproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fdmgroup.apmproject.model.User;

/**
 * This interface extends the JpaRepository interface to provide additional methods for accessing and manipulating User entities.
 * 
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user.
     * @return An optional User object.
     */
	@Query("SELECT u FROM User u WHERE u.username = :userUsername")
    Optional<User> findByUsername(@Param("userUsername") String username);

    /**
     * Finds all users.
     *
     * @return A list of all users.
     */
    List<User> findAll();
}