package com.fdmgroup.apmproject.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.Status;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.repository.CreditCardRepository;

import jakarta.annotation.PostConstruct;

@Service
public class CreditCardService {
	@Autowired
	private CreditCardRepository creditCardRepo;
	@Autowired
	private UserService userService;
	@Autowired
	private StatusService statusService;
	private static final long ONE_MONTH_IN_MILLISECONDS = TimeUnit.DAYS.toMillis(30);
	private static final double interestRate = 0.1;

	private static Logger logger = LogManager.getLogger(CreditCardService.class);

	public CreditCardService(CreditCardRepository creditCardRepo) {
		this.creditCardRepo = creditCardRepo;
	}

	public void persist(CreditCard creditCard) {
		Optional<CreditCard> returnedCreditCard = creditCardRepo.findById(creditCard.getCreditCardId());
		if (returnedCreditCard.isEmpty()) {
			creditCardRepo.save(creditCard);
			logger.info("Credit Card successfully created");
		} else {
			logger.warn("Credit Card already exists");
		}
	}

	public void update(CreditCard creditCard) {
		Optional<CreditCard> returnedCreditCard = creditCardRepo.findById(creditCard.getCreditCardId());
		if (returnedCreditCard.isEmpty()) {
			logger.warn("Credit Card does not exist in database");
		} else {
			creditCardRepo.save(creditCard);
			logger.info("Credit Card successfully updated");
		}
	}

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

	public void deleteById(long creditCardId) {
		Optional<CreditCard> returnedCreditCard = creditCardRepo.findById(creditCardId);
		if (returnedCreditCard.isEmpty()) {
			logger.warn("Credit Card does not exist in database");
		} else {
			creditCardRepo.deleteById(creditCardId);
			logger.info("Credit Card deleted from Database");
		}
	}

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

	public List<CreditCard> findCreditCardsByStatus(Status status) {
		logger.info("Finding all credit card with " + status.getStatusName() + " Status");
		return creditCardRepo.findByCreditCardStatus(status);
	}

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

	public List<CreditCard> findAllCreditCards() {
		List<CreditCard> creditCards = creditCardRepo.findAll();
		return creditCards;
	}

	@PostConstruct
	public void intiCreditCards() {
		User userJacky = userService.findUserByUsername("jackytan");
		String creditCardNumber = "1234-5678-1234-5678";
		String pin = "123";
		String currencyCode = "SGD";
		Status statusName = statusService.findByStatusName("Approved");
		CreditCard createCreditCard = new CreditCard(creditCardNumber, pin, 3000, "Ultimate Cashback Card", statusName,
				0, userJacky, currencyCode);
		String creditCardNumber2 = "2345-5678-2398-5128";
		String pin2 = "124";
		CreditCard createCreditCard2 = new CreditCard(creditCardNumber2, pin2, 3000, "SwipeSmart Platinum Card",
				statusName, 0, userJacky, currencyCode);
		persist(createCreditCard);
		persist(createCreditCard2);

		String creditCardNumberPending = "3456-5678-1234-5678";
		String pinPending = "125";
		CreditCard createCreditCardPending = new CreditCard(creditCardNumberPending, pinPending, 3000,
				"SwipeSmart Platinum Card", statusService.findByStatusName("Pending"), 0, userJacky, currencyCode);
		persist(createCreditCardPending);
	}
	//Tests not implemented from this line onwards
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
				List<CreditCard> approvedCreditCards = findCreditCardsByStatus(statusName);
				calculateMonthlyBalance(approvedCreditCards);
				chargeInterest(approvedCreditCards);
			}
		}, delay, ONE_MONTH_IN_MILLISECONDS);
	}

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

}
