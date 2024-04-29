package com.fdmgroup.apmproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.Status;

/**
 * This interface extends the JpaRepository interface to provide additional
 * methods for accessing and manipulating CreditCard entities.
 * 
 * @author
 * @version 1.0
 * @since 2024-04-22
 */
@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

	/**
	 * Finds a credit card by its credit card number.
	 *
	 * @param number The credit card number.
	 * @return An optional CreditCard object.
	 */
	Optional<CreditCard> findByCreditCardNumber(String number);

	/**
	 * Finds all credit cards associated with the user with the specified ID.
	 *
	 * @param userId The ID of the user.
	 * @return A list of credit cards associated with the user.
	 */
	List<CreditCard> findByCreditCardUserUserId(long userId);

	/**
	 * Finds all credit cards.
	 *
	 * @return A list of all credit cards.
	 */
	List<CreditCard> findAll();

	/**
	 * Finds all credit cards with the specified status.
	 *
	 * @param status The status of the credit cards.
	 * @return A list of credit cards with the specified status.
	 */
	List<CreditCard> findByCreditCardStatus(Status status);
}