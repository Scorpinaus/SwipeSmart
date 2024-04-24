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

	public void persist(Transaction transaction) {
		Optional<Transaction> returnedTransaction = transactionRepo.findById(transaction.getTransactionId());
		if (returnedTransaction.isEmpty()) {
			transactionRepo.save(transaction);
			logger.info("Transaction successfully created");
		} else {
			logger.warn("Transaction already exists");
		}
	}

	public void update(Transaction transaction) {
		Optional<Transaction> returnedTransaction = transactionRepo.findById(transaction.getTransactionId());
		if (returnedTransaction.isEmpty()) {
			logger.warn("Transaction does not exist in database");
		} else {
			transactionRepo.save(transaction);
			logger.info("Transaction successfully updated");
		}
	}

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

	public void deleteById(long transactionId) {
		Optional<Transaction> returnedTransaction = transactionRepo.findById(transactionId);
		if (returnedTransaction.isEmpty()) {
			logger.warn("Transaction does not exist in database");
		} else {
			transactionRepo.deleteById(transactionId);
			logger.info("Transaction deleted from Database");
		}
	}

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

	public List<Transaction> getTransactionsByMonthAndYearAndTransactionAccount(int year, int monthValue,
			Account account) {
		YearMonth yearMonth = YearMonth.of(year, monthValue);
		LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
		LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

		return transactionRepo.findByTransactionDateBetweenAndTransactionAccount(startOfMonth, endOfMonth, account);
	}

	public List<Transaction> findTransactionsBeforeDateAndCreditCard(LocalDateTime date, CreditCard creditCard) {
		return transactionRepo.findByTransactionDateBeforeAndTransactionCreditCard(date, creditCard);
	}

	public List<Transaction> findTransactionsByCreditCard(CreditCard creditCard) {
		return transactionRepo.findByTransactionCreditCard(creditCard);
	}

	public List<Transaction> getTransactionsByMonthAndYearAndTransactionCreditCard(int year, int monthValue,
			CreditCard creditcard) {
		YearMonth yearMonth = YearMonth.of(year, monthValue);
		LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
		LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);
		return transactionRepo.findByTransactionDateBetweenAndTransactionCreditCard(startOfMonth, endOfMonth,
				creditcard);
	}

	public List<Transaction> findByTransactionAccountOrRecipientAccount(Account transactionAccount,
			Account recipientAccount) {
		List<Transaction> Transactions = new ArrayList<>();

		Transactions = transactionRepo.findByTransactionAccountOrRecipientAccount(transactionAccount, recipientAccount);

		Transactions = maskCredentialInfo(Transactions);

		return Transactions;
	}

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

	// Tests not implemented from this line onwards
	// run this method at the start of every month
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
