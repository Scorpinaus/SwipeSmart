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

	private void updateInterest(List<CreditCard> approvedCreditCards) {
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

	@PostConstruct
	public void initTransactions() {
		CreditCard creditCard = creditCardService.findByCreditCardNumber("1234-5678-1234-5678");
		CreditCard creditCard2 = creditCardService.findByCreditCardNumber("2345-5678-2398-5128");
		creditCard.setMinBalancePaid(50);
		creditCard2.setMinBalancePaid(50);
		MerchantCategoryCode mcc = merchantCategoryCodeService.findByMerchantCategory("Dining");
		MerchantCategoryCode mcc1 = merchantCategoryCodeService.findByMerchantCategory("Shopping");
		MerchantCategoryCode mcc2 = merchantCategoryCodeService.findByMerchantCategory("Travel");
		MerchantCategoryCode mcc3 = merchantCategoryCodeService.findByMerchantCategory("Bill");
		ForeignExchangeCurrency currency = currencyService.getCurrencyByCode("SGD");
		ForeignExchangeCurrency currencyUSD = currencyService.getCurrencyByCode("USD");
		Status statusName = statusService.findByStatusName("Approved");
		Transaction transaction = new Transaction(LocalDateTime.of(2024, 4, 15, 12, 34, 56), "CC Purchase", 20.5, null,
				0.00, creditCard, null, mcc, currency);
		Transaction transaction1 = new Transaction(LocalDateTime.of(2024, 4, 12, 12, 34, 56), "CC Purchase", 10, null,
				0.00, creditCard, null, mcc, currency);
		Transaction transaction2 = new Transaction(LocalDateTime.of(2024, 3, 12, 11, 33, 56), "CC Purchase", 10, null,
				0.00, creditCard, null, mcc1, currencyUSD);
		Transaction transaction3 = new Transaction(LocalDateTime.of(2024, 3, 28, 11, 33, 56), "CC Purchase", 50, null,
				0.00, creditCard, null, mcc2, currency);
		Transaction transaction4 = new Transaction(LocalDateTime.of(2024, 3, 9, 11, 33, 56), "CC Purchase", 150.10,
				null, 0.00, creditCard, null, mcc2, currencyUSD);
		Transaction transaction5 = new Transaction(LocalDateTime.of(2024, 2, 12, 11, 33, 56), "CC Purchase", 20, null,
				0.00, creditCard, null, mcc, currency);
		Transaction transaction6 = new Transaction(LocalDateTime.of(2024, 2, 13, 11, 33, 56), "CC Payment", 20, null,
				0.00, creditCard, null, mcc3, currency);
		Transaction transaction7 = new Transaction(LocalDateTime.of(2024, 4, 13, 11, 33, 56), "CC Payment", 100, null,
				0.00, creditCard, null, mcc3, currency);
		Transaction transaction8 = new Transaction(LocalDateTime.of(2024, 3, 9, 11, 33, 56), "CC Purchase", 1100.10,
				null, 0.00, creditCard2, null, mcc1, currency);
		Transaction transaction9 = new Transaction(LocalDateTime.of(2024, 2, 9, 11, 33, 56), "CC Purchase", 100.10,
				null, 0.00, creditCard2, null, mcc2, currency);
		Transaction transaction10 = new Transaction(LocalDateTime.of(2024, 4, 13, 11, 12, 26), "CC Purchase", 100.10,
				null, 0.00, creditCard2, null, mcc2, currency);
		Transaction transaction11 = new Transaction(LocalDateTime.of(2024, 4, 23, 9, 35, 26), "CC Purchase", 1200, null,
				0.00, creditCard2, null, mcc1, currency);
		Account account1 = accountService.findAccountByAccountNumber("123-123-123");
		Account account2 = accountService.findAccountByAccountNumber("124-124-124");
		Transaction transactionA1 = new Transaction("Initial Deposit", account1, account1.getBalance(), null, currency,
				currency.getCode() + " " + account1.getBalance());
		Transaction transactionA2 = new Transaction("Initial Deposit", account2, account2.getBalance(), null, currency,
				currency.getCode() + " " + account2.getBalance());
		persist(transactionA1);
		persist(transactionA2);
		Double exchangeRateUSD = currencyService.getExchangeRate(currencyUSD.getCode(), currency.getCode())
				.doubleValue();
		transaction.setCreditCardDescription("Astons", 1);
		transaction1.setCreditCardDescription("Kopitiam", 1);
		transaction2.setCreditCardDescription("Amzaon", exchangeRateUSD);
		transaction3.setCreditCardDescription("SIA", 1);
		transaction4.setCreditCardDescription("United", exchangeRateUSD);
		transaction5.setCreditCardDescription("Collins", 1);
		transaction8.setCreditCardDescription("Hermes", 1);
		transaction9.setCreditCardDescription("SIA", 1);
		transaction10.setCreditCardDescription("SCOOT", 1);
		transaction11.setCreditCardDescription("Rolex", 1);

		Transaction[] transactions = { transaction, transaction1, transaction2, transaction3, transaction4,
				transaction5, transaction6, transaction7, transaction8, transaction9, transaction10, transaction11 };
		for (Transaction t : transactions) {
			persist(t);
			updateCreditCardBalance(t);
		}
		List<CreditCard> approvedCreditCards = creditCardService.findCreditCardsByStatus(statusName);
		chargeMinimumBalanceFee(approvedCreditCards);
		creditCardService.calculateMonthlyBalance(approvedCreditCards);
		creditCardService.chargeInterest(approvedCreditCards);
		updateInterest(approvedCreditCards);

	}
	
	public List<Transaction> getAllTransactions() {
		return transactionRepo.findAll();
	}

}
