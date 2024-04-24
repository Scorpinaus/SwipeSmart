package com.fdmgroup.apmproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.apmproject.model.Account;

/**
 * This interface extends the JpaRepository interface to provide additional
 * methods for accessing and manipulating Account entities.
 * 
 * @author
 * @version 1.0
 * @since 2024-04-22
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	/**
	 * Finds all accounts associated with the user with the specified ID.
	 *
	 * @param userId The ID of the user.
	 * @return A list of accounts associated with the user.
	 */
	List<Account> findByAccountUserUserId(long userId);

	/**
	 * Finds an account by its account number.
	 *
	 * @param accountNumber The account number.
	 * @return An optional Account object.
	 */
	Optional<Account> findByAccountNumber(String name);

	/**
	 * Finds an account by its ID.
	 *
	 * @param accountId The ID of the account.
	 * @return An optional Account object.
	 */
	Optional<Account> findByAccountId(Long accountId);

}
