package com.fdmgroup.apmproject.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.ForeignExchangeCurrency;
import com.fdmgroup.apmproject.model.MerchantCategoryCode;
import com.fdmgroup.apmproject.model.Status;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.repository.TransactionRepository;

import jakarta.annotation.PostConstruct;

/**
 * This class is responsible for handling all business logic related to Transactions.
 * 
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */

@Service
public class TransactionService {
	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private MerchantCategoryCodeService merchantCategoryCodeService;

	@Autowired
	private ForeignExchangeCurrencyService currencyService;

	@Autowired
	private StatusService statusService;

	private static final long ONE_MONTH_IN_MILLISECONDS = TimeUnit.DAYS.toMillis(30);

	private static Logger logger = LogManager.getLogger(TransactionService.class);

	/**
	 * Persists a Transaction entity into the database.
	 * <p>
	 * This method checks if the provided Transaction entity already exists in the database based on its ID. If the Transaction does not exist, it is saved into the database; otherwise, a warning is logged. 
	 *
	 * @param transaction The Transaction entity to persist.
	 * @throws IllegalArgumentException if the transaction is null
	 * @see org.example.StatusService
	 * @see org.example.Transaction
	 */
	public void persist(Transaction transaction) {
		Optional<Transaction> returnedTransaction = transactionRepo.findById(transaction.getTransactionId());
		if (returnedTransaction.isEmpty()) {
			transactionRepo.save(transaction);
			logger.info("Transaction successfully created");
		} else {
			logger.warn("Transaction already exists");
		}
	}

	/**
	 * Updates a Transaction entity in the database.
	 * <p>
	 * This method checks if the provided Transaction entity exists in the database based on its ID. If the Transaction exists, it is updated with the new data; otherwise, a warning is logged. 
	 *
	 * @param transaction The Transaction entity to update.
	 * @throws IllegalArgumentException if the transaction is null
	 * @see org.example.StatusService
	 * @see org.example.Transaction
	 */
	public void update(Transaction transaction) {
		Optional<Transaction> returnedTransaction = transactionRepo.findById(transaction.getTransactionId());
		if (returnedTransaction.isEmpty()) {
			logger.warn("Transaction does not exist in database");
		} else {
			transactionRepo.save(transaction);
			logger.info("Transaction successfully updated");
		}
	}

	/**
	 * Retrieves a Transaction entity by its ID.
	 * <p>
	 * This method queries the database to find a Transaction entity with the specified ID. If found, it returns the Transaction entity; otherwise, it returns null. It logs a warning if the Transaction entity is not found in the database.
	 *
	 * @param transactionId The ID of the transaction to retrieve.
	 * @return The Transaction entity with the specified ID if found, otherwise null.
	 * @see org.example.StatusService
	 * @see org.example.Transaction
	 */
	public Transaction findById(long transactionId) {
		Optional<Transaction> returnedTransaction = transactionRepo.findById(transactionId);
		if (returnedTransaction.isEmpty()) {
			logger.warn("Could not find Transaction in Database");
			return null;
		} else {
			logger.info("Returning Transaction's details");
			return returnedTransaction.get();
		}
	}

	/**
	 * Deletes a Transaction entity by its ID.
	 * <p>
	 * This method attempts to find a Transaction entity in the database using the provided ID. If the Transaction entity exists, it is deleted from the database; otherwise, a warning is logged. 
	 *
	 * @param transactionId The ID of the transaction to delete.
	 * @throws IllegalArgumentException if the transactionId is null
	 * @see org.example.StatusService
	 * @see org.example.Transaction
	 */
	public void deleteById(long transactionId) {
		Optional<Transaction> returnedTransaction = transactionRepo.findById(transactionId);
		if (returnedTransaction.isEmpty()) {
			logger.warn("Transaction does not exist in database");
		} else {
			transactionRepo.deleteById(transactionId);
			logger.info("Transaction deleted from Database");
		}
	}

	/**
	 * Updates the balance and cashback of a credit card based on a transaction.
	 * <p>
	 * This method calculates and updates the balance and cashback of a credit card based on the type and amount of a transaction. It considers various factors such as the credit card type, transaction type, and merchant category code to determine the cashback and balance adjustments.
	 *
	 * @param transaction The transaction used to update the credit card balance and cashback.
	 * @throws IllegalArgumentException if the transaction is null
	 * @see org.example.StatusService
	 * @see org.example.Transaction
	 */
	public void updateCreditCardBalance(Transaction transaction) {
		LocalDate previousMonth = LocalDate.now().withDayOfMonth(1);
		// ensure amount used in credit card is updated
		if (transaction.getTransactionCreditCard() != null && transaction.getTransactionType().equals("CC Purchase")) {
			CreditCard creditCard = transaction.getTransactionCreditCard();
			if (creditCard.getCardType().equals("Ultimate Cashback Card")) {
				if (transaction.getTransactionMerchantCategoryCode().getMerchantCategory().equals("Dining")) {
					transaction.setCashback(transaction.getTransactionAmount() * 0.02);
					update(transaction);
				}
			} else if (creditCard.getCardType().equals("SwipeSmart Platinum Card")) {
				if (transaction.getTransactionMerchantCategoryCode().getMerchantCategoryCodeNumber() != 1005) {
					transaction.setCashback(transaction.getTransactionAmount() * 0.015);
					update(transaction);
				}
			}
			creditCard.addTransaction(transaction.getTransactionAmount() - transaction.getCashback());
			LocalDate transactionDateAsLocalDate = transaction.getTransactionDate().toLocalDate();
//			if (transactionDateAsLocalDate.isBefore(previousMonth)) {
//				creditCard.addTransactionMonthly(transaction.getTransactionAmount() - transaction.getCashback());
//			}
			creditCardService.update(creditCard);

		} else if (transaction.getTransactionCreditCard() != null
				&& transaction.getTransactionType().equals("CC Payment")) {
			CreditCard creditCard = transaction.getTransactionCreditCard();
			creditCard.addTransaction(-transaction.getTransactionAmount());
			LocalDate transactionDateAsLocalDate = transaction.getTransactionDate().toLocalDate();
			creditCard.setMinBalancePaid(creditCard.getMinBalancePaid() - transaction.getTransactionAmount());
			creditCardService.update(creditCard);
		}
	}

	/**
	 * Retrieves transactions within a specified month and year associated with a given account.
	 * <p>
	 * This method queries the database for transactions that occurred within the specified month and year and were made using the provided account. It returns a list of transactions matching the criteria.
	 *
	 * @param year       The year of the transactions.
	 * @param monthValue The month of the transactions.
	 * @param account    The account associated with the transactions.
	 * @return A list of Transaction objects within the specified month and year associated with the provided account.
	 * @throws IllegalArgumentException if the account is null
	 * @see org.example.StatusService
	 * @see org.example.Transaction
	 */
	public List<Transaction> getTransactionsByMonthAndYearAndTransactionAccount(int year, int monthValue,
			Account account) {
		YearMonth yearMonth = YearMonth.of(year, monthValue);
		LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
		LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

		return transactionRepo.findByTransactionDateBetweenAndTransactionAccount(startOfMonth, endOfMonth, account);
	}

	/**
	 * Retrieves transactions within a specified month and year associated with a given account.
	 * <p>
	 * This method queries the database for transactions that occurred within the specified month and year and were made using the provided account. It returns a list of transactions matching the criteria.
	 *
	 * @param year       The year of the transactions.
	 * @param monthValue The month of the transactions.
	 * @param account    The account associated with the transactions.
	 * @return A list of Transaction objects within the specified month and year associated with the provided account.
	 * @throws IllegalArgumentException if the account is null
	 * @see org.example.StatusService
	 * @see org.example.Transaction
	 */
	public List<Transaction> findTransactionsBeforeDateAndCreditCard(LocalDateTime date, CreditCard creditCard) {
		return transactionRepo.findByTransactionDateBeforeAndTransactionCreditCard(date, creditCard);
	}

	/**
	 * Retrieves transactions associated with a specific credit card.
	 * <p>
	 * This method queries the database for transactions made using the provided credit card and returns a list of transactions associated with it.
	 *
	 * @param creditCard The credit card for which transactions are to be retrieved.
	 * @return A list of Transaction objects associated with the provided credit card.
	 * @throws IllegalArgumentException if the creditCard is null
	 * @see org.example.StatusService
	 * @see org.example.Transaction
	 */
	public List<Transaction> findTransactionsByCreditCard(CreditCard creditCard) {
		return transactionRepo.findByTransactionCreditCard(creditCard);
	}

	/**
	 * Retrieves transactions within a specified month and year associated with a given credit card.
	 * <p>
	 * This method queries the database for transactions that occurred within the specified month and year and were made using the provided credit card. It returns a list of transactions matching the criteria.
	 *
	 * @param year        The year of the transactions.
	 * @param monthValue  The month of the transactions.
	 * @param creditCard  The credit card associated with the transactions.
	 * @return A list of Transaction objects within the specified month and year associated with the provided credit card.
	 * @throws IllegalArgumentException if the creditCard is null
	 * @see org.example.StatusService
	 * @see org.example.Transaction
	 */
	public List<Transaction> getTransactionsByMonthAndYearAndTransactionCreditCard(int year, int monthValue,
			CreditCard creditcard) {
		YearMonth yearMonth = YearMonth.of(year, monthValue);
		LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
		LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);
		return transactionRepo.findByTransactionDateBetweenAndTransactionCreditCard(startOfMonth, endOfMonth,
				creditcard);
	}

	/**
	 * Retrieves transactions associated with either a transaction account or a recipient account.
	 * <p>
	 * This method queries the database for transactions associated with either the provided transaction account or the recipient account. It returns a list of transactions that match the criteria. The method also masks sensitive credential information before returning the transactions.
	 *
	 * @param transactionAccount The transaction account to search for.
	 * @param recipientAccount   The recipient account to search for.
	 * @return A list of Transaction objects associated with either the transaction account or recipient account.
	 * @throws IllegalArgumentException if both transactionAccount and recipientAccount are null
	 * @see org.example.StatusService
	 * @see org.example.Transaction
	 */
	public List<Transaction> findByTransactionAccountOrRecipientAccount(Account transactionAccount,
			Account recipientAccount) {
		List<Transaction> Transactions = new ArrayList<>();

		Transactions = transactionRepo.findByTransactionAccountOrRecipientAccount(transactionAccount, recipientAccount);

		Transactions = maskCredentialInfo(Transactions);

		return Transactions;
	}

	/**
	 * Masks sensitive credential information in a list of transactions.
	 * <p>
	 * This method iterates through the list of transactions and masks sensitive credential information, such as account numbers, for both the transaction account and the recipient account. It then returns the updated list of transactions with the masked information.
	 *
	 * @param Transactions The list of transactions to mask credential information for.
	 * @return A list of Transaction objects with sensitive credential information masked.
	 * @throws IllegalArgumentException if Transactions is null
	 * @see org.example.Transaction
	 */
	private List<Transaction> maskCredentialInfo(List<Transaction> Transactions) {
		for (Transaction transaction : Transactions) {

			if (transaction.getTransactionAccount() != null) {
				String accountFromNumber = transaction.getTransactionAccount().getAccountNumber();
				String maskedAccountFromNumber = "***-***-"
						+ accountFromNumber.substring(accountFromNumber.length() - 3);
				transaction.getTransactionAccount().setAccountNumber(maskedAccountFromNumber);
			}
			if (transaction.getRecipientAccount() != null) {

				String accountToNumber = transaction.getRecipientAccount().getAccountNumber();
				String maskedAccountToNumber = "***-***-" + accountToNumber.substring(accountToNumber.length() - 3);
				transaction.getRecipientAccount().setAccountNumber(maskedAccountToNumber);
			}
		}

		return Transactions;
	}

	/**
	 * Updates the interest for approved credit cards.
	 * <p>
	 * This method calculates and updates the interest for each approved credit card. For each credit card with non-zero interest, it generates a transaction representing the interest payment and updates the credit card's balance accordingly.
	 *
	 * @param approvedCreditCards The list of approved credit cards for which interest needs to be updated.
	 * @throws IllegalArgumentException if approvedCreditCards is null
	 * @see org.example.CreditCard
	 * @see org.example.Transaction
	 */
	public void updateInterest(List<CreditCard> approvedCreditCards) {
		MerchantCategoryCode mcc4 = merchantCategoryCodeService.findByMerchantCategory("Interest");
		ForeignExchangeCurrency currency = currencyService.getCurrencyByCode("SGD");
		for (CreditCard creditCard : approvedCreditCards) {
			if (creditCard.getInterest() > 0) {
				Transaction transaction = new Transaction(LocalDateTime.now(), "CC Purchase", creditCard.getInterest(),
						null, 0.00, creditCard, null, mcc4, currency);
				persist(transaction);
				updateCreditCardBalance(transaction);
			}
		}
	}

	/**
	 * Charges a minimum balance fee for approved credit cards.
	 * <p>
	 * This method charges a minimum balance fee for each approved credit card that has an unpaid minimum balance. For each credit card with an unpaid minimum balance, it generates a transaction representing the fee and updates the credit card's balance accordingly.
	 *
	 * @param approvedCreditCards The list of approved credit cards for which the minimum balance fee needs to be charged.
	 * @throws IllegalArgumentException if approvedCreditCards is null
	 * @see org.example.CreditCard
	 * @see org.example.Transaction
	 */
	public void chargeMinimumBalanceFee(List<CreditCard> approvedCreditCards) {
		MerchantCategoryCode mcc4 = merchantCategoryCodeService.findByMerchantCategory("Interest");
		ForeignExchangeCurrency currency = currencyService.getCurrencyByCode("SGD");
		for (CreditCard creditCard : approvedCreditCards) {
			if (creditCard.getMinBalancePaid() > 0) {
				Transaction transaction = new Transaction(LocalDateTime.now(), "CC Purchase", 100, null, 0.00,
						creditCard, null, mcc4, currency);
				transaction.setDescription("Unpaid Minimum Balance Fee");
				persist(transaction);
				updateCreditCardBalance(transaction);
			}
		}
	}

	/**
	 * Calculates the delay until the start of the next month.
	 * <p>
	 * This method calculates the delay in milliseconds until the start of the next month from the current date and time. It's typically used to schedule tasks or operations that need to occur at the beginning of each month.
	 *
	 * @return The delay in milliseconds until the start of the next month.
	 * @see java.time.LocalDate
	 * @see java.time.LocalDateTime
	 * @see java.time.Duration
	 */
	private long calculateDelayToNextMonth() {
		LocalDate currentDate = LocalDate.now();
		LocalDate nextMonth = currentDate.plusMonths(1).withDayOfMonth(1);
		LocalDateTime nextMonthStartOfDay = nextMonth.atStartOfDay();
		Duration duration = Duration.between(LocalDateTime.now(), nextMonthStartOfDay);
		return duration.toMillis();
	}

	public void scheduleInterestCharging() {
		Timer timer = new Timer();

		// Calculate the delay until the next 1st day of the month
		long delay = calculateDelayToNextMonth();

		// Schedule the task to run every month, starting from the next 1st day of the
		// month
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Status statusName = statusService.findByStatusName("Approved");
				List<CreditCard> approvedCreditCards = creditCardService.findCreditCardsByStatus(statusName);
				creditCardService.calculateMonthlyBalance(approvedCreditCards);
				creditCardService.chargeInterest(approvedCreditCards);
				chargeMinimumBalanceFee(approvedCreditCards);
				creditCardService.calculateMinimumBalance(approvedCreditCards);
			}
		}, delay, ONE_MONTH_IN_MILLISECONDS);
	}
	
	public List<Transaction> getAllTransactions() {
		return transactionRepo.findAll();
	}

}
