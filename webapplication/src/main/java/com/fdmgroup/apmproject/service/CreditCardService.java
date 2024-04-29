package com.fdmgroup.apmproject.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.Status;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.repository.CreditCardRepository;

/**
 * This class is responsible for handling the business logic for credit cards
 *
 * @author
 * @version 1.0
 * @since 23/4/2024
 * @see
 */

@Service
public class CreditCardService {
	@Autowired
	private CreditCardRepository creditCardRepo;
	private static final double interestRate = 0.1;

	private static Logger logger = LogManager.getLogger(CreditCardService.class);

	/**
	 * Creates credit card entity to database
	 * <p>
	 * It checks if the credit card previously exists by calling upon the findById
	 * from creditcardRepository. If exists, it will not persist to database
	 *
	 * @param CreditCard CreditCard to create on the database
	 */
	public void persist(CreditCard creditCard) {
		Optional<CreditCard> returnedCreditCard = creditCardRepo.findById(creditCard.getCreditCardId());
		if (returnedCreditCard.isEmpty()) {
			creditCardRepo.save(creditCard);
			logger.info("Credit Card successfully created");
		} else {
			logger.warn("Credit Card already exists");
		}
	}

	/**
	 * Updates credit card entity to database.
	 * <p>
	 * It checks if the credit card previously exists by calling upon the findById
	 * from creditcardRepository. If does not exist, it will not update to database
	 *
	 * @param CreditCard CreditCard to update to database
	 */
	public void update(CreditCard creditCard) {
		Optional<CreditCard> returnedCreditCard = creditCardRepo.findById(creditCard.getCreditCardId());
		if (returnedCreditCard.isEmpty()) {
			logger.warn("Credit Card does not exist in database");
		} else {
			creditCardRepo.save(creditCard);
			logger.info("Credit Card successfully updated");
		}
	}

	/**
	 * Finds credit card by credit card ID
	 * <p>
	 * It checks if the credit card previously exists by calling upon the findById
	 * from creditcardRepository. If does not exist, it return null
	 *
	 * @param CreditCardId CreditCardId to use as a search parameter
	 * @return CreditCard Credit card entity that can be used for subsequent
	 *         operations.
	 */
	public CreditCard findById(long creditCardId) {
		Optional<CreditCard> returnedCreditCard = creditCardRepo.findById(creditCardId);
		if (returnedCreditCard.isEmpty()) {
			logger.warn("Could not find Credit Card in Database");
			return null;
		} else {
			logger.info("Returning Credit Card's details");
			return returnedCreditCard.get();
		}
	}

	/**
	 * Deletes a credit card record by its identifier.
	 * <p>
	 * This method searches for a credit card in the repository using the provided
	 * identifier. If the credit card is found, it is deleted from the repository
	 * and an informational log entry is generated. If no credit card is found with
	 * the given identifier, a warning log entry is generated to indicate the
	 * absence of the credit card in the database.
	 *
	 * @param creditCardId The unique identifier of the credit card to be deleted.
	 * @throws DataAccessException If there is a failure during database access,
	 *                             update, or any other data access related
	 *                             operations.
	 * @see CreditCardRepo#findById(long) To fetch the credit card details.
	 * @see CreditCardRepo#deleteById(long) To delete the credit card from the
	 *      database.
	 */
	public void deleteById(long creditCardId) {
		Optional<CreditCard> returnedCreditCard = creditCardRepo.findById(creditCardId);
		if (returnedCreditCard.isEmpty()) {
			logger.warn("Credit Card does not exist in database");
		} else {
			creditCardRepo.deleteById(creditCardId);
			logger.info("Credit Card deleted from Database");
		}
	}

	/**
	 * Retrieves a credit card record by its credit card number.
	 * <p>
	 * This method attempts to find a credit card in the repository using the
	 * specified number. If the credit card exists, its details are returned and an
	 * informational log is recorded. If no credit card matches the given number, a
	 * warning log is generated and null is returned. This method primarily handles
	 * data retrieval and logs the process outcomes.
	 *
	 * @param number The credit card number used to locate the credit card in the
	 *               database.
	 * @return The CreditCard object if found, or null if no such credit card
	 *         exists.
	 * @throws DataAccessException If database access fails or other issues related
	 *                             to data retrieval occur.
	 * @see CreditCardRepo#findByCreditCardNumber(String) Method to query credit
	 *      card by its number.
	 */
	public CreditCard findByCreditCardNumber(String number) {
		Optional<CreditCard> returnedCreditCard = creditCardRepo.findByCreditCardNumber(number);
		if (returnedCreditCard.isEmpty()) {
			logger.warn("Could not find Credit Card in Database");
			return null;
		} else {
			logger.info("Returning Credit Card's details");
			return returnedCreditCard.get();
		}
	}

	/**
	 * Retrieves a list of credit card records matching a specific status.
	 * <p>
	 * This method queries the database for all credit cards that have the specified
	 * status. It logs the attempt and returns the list of credit cards that match
	 * the status criteria. Useful for filtering credit cards based on their
	 * operational status, such as active, suspended, or closed.
	 *
	 * @param status The status used to filter credit cards. It determines which
	 *               credit cards are returned based on their current operational
	 *               status.
	 * @return List of CreditCard objects that match the given status; can be empty
	 *         if no matching records are found.
	 * @throws DataAccessException If database access fails or other issues related
	 *                             to data retrieval occur.
	 * @see CreditCardRepo#findByCreditCardStatus(Status) Method used for querying
	 *      credit cards by their status.
	 */
	public List<CreditCard> findCreditCardsByStatus(Status status) {
		logger.info("Finding all credit card with " + status.getStatusName() + " Status");
		return creditCardRepo.findByCreditCardStatus(status);
	}

	/**
	 * Generates a random credit card number in a standardized format.
	 * <p>
	 * This method constructs a credit card number consisting of 16 digits, divided
	 * into four groups separated by dashes for readability (e.g.,
	 * XXXX-XXXX-XXXX-XXXX). It uses a pseudorandom number generator to ensure each
	 * digit is randomly selected. The format is suitable for mock-up purposes and
	 * not for generating valid real-world credit card numbers.
	 *
	 * @return A string representing a pseudo-randomly generated credit card number
	 *         in a readable format.
	 * @throws IllegalArgumentException If the number generation process encounters
	 *                                  an issue with the formatting.
	 * @see java.util.Random#nextInt(int) Method used for generating random digits.
	 */
	public String generateCreditCardNumber() {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 16; i++) {
			sb.append(random.nextInt(10));
			if ((i + 1) % 4 == 0 && i != 15) {
				sb.append("-"); // Adds a dash after every 4 digits, except the last set of 4 digits
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("unused")
	public String generatePinNumber() {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}

	/**
	 * Retrieves all credit card records from the repository.
	 * <p>
	 * This method fetches a list of all the credit cards stored in the database. It
	 * is typically used to display all credit card entries or to perform batch
	 * operations on them. The list returned can vary in size from empty (if no
	 * credit cards are stored) to containing all records available in the database.
	 *
	 * @return List of CreditCard objects representing all credit cards stored in
	 *         the database; this list may be empty if no cards are found.
	 * @throws DataAccessException If database access fails or other issues related
	 *                             to data retrieval occur.
	 * @see CreditCardRepo#findAll() Method to fetch all records from the credit
	 *      card repository.
	 */
	public List<CreditCard> findAllCreditCards() {
		List<CreditCard> creditCards = creditCardRepo.findAll();
		return creditCards;
	}

	/**
	 * Calculates and applies interest charges to a list of approved credit cards
	 * based on their previous month's transactions.
	 * <p>
	 * This method processes each credit card in the provided list, calculating the
	 * interest payable for purchases made in the previous month, minus any cashback
	 * received. It updates the credit card's interest amount if there is a positive
	 * balance after adjustments. The interest calculation is based on the card's
	 * monthly balance and applicable interest rate. Each card's updated interest
	 * and balance are logged, and the card record is updated in the database.
	 *
	 * @param approvedCreditCards A list of CreditCard objects that have been
	 *                            approved for interest charging.
	 * @return void This method does not return a value.
	 * @throws DataAccessException If there are issues accessing the data required
	 *                             for processing interest.
	 * @see Transaction#getTransactionDate() To determine the date range for
	 *      interest calculation.
	 * @see CreditCard#getMonthlyBalance() To get the initial balance from which the
	 *      interest is calculated.
	 * @see CreditCard#setInterest(double) To update the credit card's interest
	 *      amount.
	 */

	public void chargeInterest(List<CreditCard> approvedCreditCards) {
		// Subtract one month from the current date
		LocalDate curMonth = LocalDate.now().withDayOfMonth(1);
		LocalDate previousMonth = LocalDate.now().minusMonths(1);
		LocalDate firstDayOfPreviousMonth = previousMonth.withDayOfMonth(1);
		for (CreditCard creditCard : approvedCreditCards) {
			double interestPayable = creditCard.getMonthlyBalance();
			List<Transaction> transactions = creditCard.getTransactions();
			for (Transaction transaction : transactions) {
				if (transaction.getTransactionDate().toLocalDate().isBefore(curMonth)
						&& transaction.getTransactionDate().toLocalDate().isAfter(firstDayOfPreviousMonth)
						&& transaction.getTransactionType().equals("CC Purchase")) {
					interestPayable -= (transaction.getTransactionAmount() - transaction.getCashback());
				}
			}
			if (interestPayable > 0) {
				creditCard.setInterest(interestPayable * interestRate);
				logger.info(creditCard.getCreditCardNumber() + " charged for " + interestPayable * interestRate
						+ " as interest. Balance charged for interest: " + interestPayable);
				update(creditCard);
			}
		}
	}

	/**
	 * Calculates and updates the monthly balance for each credit card based on last
	 * month's transactions.
	 * <p>
	 * This method totals purchases and payments made in the previous month for each
	 * credit card in the provided list. Purchases increase the balance due, reduced
	 * by any cashback, while payments decrease the balance. The computed monthly
	 * balance is then set for each credit card and the updated record is saved in
	 * the database. This method is critical for monthly statement generation and
	 * balance tracking.
	 *
	 * @param approvedCreditCards A list of CreditCard objects for which the monthly
	 *                            balances are to be calculated.
	 * @return void This method does not return a value, but updates each credit
	 *         card's monthly balance field.
	 * @throws DataAccessException If there are issues accessing the database or
	 *                             updating the credit cards.
	 * @see CreditCard#getTransactions() To retrieve transactions for balance
	 *      calculations.
	 * @see CreditCard#setMonthlyBalance(double) To set the calculated balance on
	 *      each credit card.
	 */
	public void calculateMonthlyBalance(List<CreditCard> approvedCreditCards) {
		// Subtract one month from the current date
		LocalDate curMonth = LocalDate.now().withDayOfMonth(1);
		for (CreditCard creditCard : approvedCreditCards) {
			double monthlyBalance = 0;
			List<Transaction> transactions = creditCard.getTransactions();
			for (Transaction transaction : transactions) {
				if (transaction.getTransactionDate().toLocalDate().isBefore(curMonth)
						&& transaction.getTransactionType().equals("CC Purchase")) {
					monthlyBalance += transaction.getTransactionAmount() - transaction.getCashback();
				} else if (transaction.getTransactionDate().toLocalDate().isBefore(curMonth)
						&& transaction.getTransactionType().equals("CC Payment")) {
					monthlyBalance -= transaction.getTransactionAmount();
				}
				creditCard.setMonthlyBalance(monthlyBalance);
				update(creditCard);
			}
		}
	}

	/**
	 * Calculates and updates the minimum balance due for each approved credit card.
	 * <p>
	 * This method determines the minimum balance that must be paid for each credit
	 * card based on the monthly balance. If the monthly balance is less than $50,
	 * the minimum balance due is set to the entire monthly balance. Otherwise, the
	 * minimum balance due is set to $50. This method ensures that each card's
	 * minimum balance requirements are met, updating the balance directly on each
	 * credit card object.
	 *
	 * @param approvedCreditCards A list of CreditCard objects to be updated with
	 *                            their minimum balance due.
	 * @return void This method does not return a value, but updates the
	 *         'MinBalancePaid' or 'MonthlyBalance' attribute of each CreditCard in
	 *         the list.
	 * @throws DataAccessException If there are issues accessing the database or
	 *                             updating the credit cards.
	 * @see CreditCard#getMonthlyBalance() To fetch the current balance for each
	 *      credit card.
	 * @see CreditCard#setMinBalancePaid(double) To update the minimum balance paid
	 *      on the credit card.
	 * @see CreditCard#setMonthlyBalance(double) To update the monthly balance to
	 *      the required minimum payment.
	 */
	public void calculateMinimumBalance(List<CreditCard> approvedCreditCards) {
		for (CreditCard creditCard : approvedCreditCards) {
			if (creditCard.getMonthlyBalance() < 50)
				creditCard.setMinBalancePaid(creditCard.getMonthlyBalance());
			else
				creditCard.setMonthlyBalance(50);
		}
	}
	
	public List<CreditCard> findAllCreditCardByUserId(long userId) {
		return creditCardRepo.findByCreditCardUserUserId(userId);
	}

}
