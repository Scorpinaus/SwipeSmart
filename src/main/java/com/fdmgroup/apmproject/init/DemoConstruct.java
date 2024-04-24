package com.fdmgroup.apmproject.init;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.ForeignExchangeCurrency;
import com.fdmgroup.apmproject.model.MerchantCategoryCode;
import com.fdmgroup.apmproject.model.Status;
import com.fdmgroup.apmproject.model.Transaction;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.service.AccountService;
import com.fdmgroup.apmproject.service.CreditCardService;
import com.fdmgroup.apmproject.service.ForeignExchangeCurrencyService;
import com.fdmgroup.apmproject.service.MerchantCategoryCodeService;
import com.fdmgroup.apmproject.service.StatusService;
import com.fdmgroup.apmproject.service.TransactionService;
import com.fdmgroup.apmproject.service.UserService;

import jakarta.annotation.PostConstruct;

@Component
public class DemoConstruct {
	@Autowired
	private MerchantCategoryCodeService merchantCategoryCodeService;
	
	@Autowired
	private StatusService statusService;
	
	@Autowired
	private ForeignExchangeCurrencyService foreignExchangeCurrencyService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private TransactionService transactionService;
	
	@PostConstruct
    public void init() {
		// Initialize Status
		Status status = new Status("Pending");
		Status status2 = new Status("Approved");
		Status status3 = new Status("Disabled");

		statusService.persist(status);
		statusService.persist(status2);
		statusService.persist(status3);
		
		// Initialize User
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		User user = new User("jackytan", encoder.encode("Qwerty123"), "Sentosa", "Jacky", "Tan");
		User user2 = new User("admin", encoder.encode("Fdm123456"), "admin's house", "admin", "chan");
		user2.setRole("ROLE_ADMIN");
		userService.persist(user);
		userService.persist(user2);
				
		// Initialize MCC
		MerchantCategoryCode mccM0 = new MerchantCategoryCode(1000, "Dining");
		MerchantCategoryCode mccM1 = new MerchantCategoryCode(1001, "Shopping");
		MerchantCategoryCode mccM2 = new MerchantCategoryCode(1002, "Transport");
		MerchantCategoryCode mccM3 = new MerchantCategoryCode(1003, "Travel");
		MerchantCategoryCode mccM4 = new MerchantCategoryCode(1004, "Bill");
		MerchantCategoryCode mccM5 = new MerchantCategoryCode(1005, "Interest");
		merchantCategoryCodeService.persist(mccM0);
		merchantCategoryCodeService.persist(mccM1);
		merchantCategoryCodeService.persist(mccM2);
		merchantCategoryCodeService.persist(mccM3);
		merchantCategoryCodeService.persist(mccM4);
		merchantCategoryCodeService.persist(mccM5);
		
		// Initialize Forex rates
		foreignExchangeCurrencyService.fetchAndSaveExchangeRates();
		foreignExchangeCurrencyService.loadAndSaveForeignCurrencyJSON();
		ForeignExchangeCurrency currencyOne = new ForeignExchangeCurrency();
		currencyOne.setCode("USD");
		currencyOne.setAlphaCode("USD");
		currencyOne.setNumericCode("USD");
		currencyOne.setName("United States Dollar");
		currencyOne.setInverseRate(1.00);
		currencyOne.setRate(1.00);
		foreignExchangeCurrencyService.persist(currencyOne);
		
		// Initialize Account
		User userJacky = userService.findUserByUsername("jackytan");
		Status statusName = statusService.findByStatusName("Approved");
		String currencyCode = "SGD";
		Account account = new Account("Savings", 5000, "123-123-123", userJacky, statusName, currencyCode);
		Account accountA2 = new Account("Current", 10000, "124-124-124", userJacky, statusName, currencyCode);
		Account accountPending = new Account("pending ac", 10000, "125-125-125", userJacky,
				statusService.findByStatusName("Pending"), currencyCode);
		Account accountPending2 = new Account("pending ac2", 10123, "126-126-126", userJacky,
				statusService.findByStatusName("Pending"), currencyCode);

		accountService.persist(account);
		accountService.persist(accountA2);
		accountService.persist(accountPending);
		accountService.persist(accountPending2);
		
		// Initialize Credit Card
		String creditCardNumber = "1234-5678-1234-5678";
		String pin = "123";
		CreditCard createCreditCard = new CreditCard(creditCardNumber, pin, 3000, "Ultimate Cashback Card", statusName,
				0, userJacky, currencyCode);
		String creditCardNumber2 = "2345-5678-2398-5128";
		String pin2 = "124";
		CreditCard createCreditCard2 = new CreditCard(creditCardNumber2, pin2, 3000, "SwipeSmart Platinum Card",
				statusName, 0, userJacky, currencyCode);
		creditCardService.persist(createCreditCard);
		creditCardService.persist(createCreditCard2);

		String creditCardNumberPending = "3456-5678-1234-5678";
		String pinPending = "125";
		CreditCard createCreditCardPending = new CreditCard(creditCardNumberPending, pinPending, 3000,
				"SwipeSmart Platinum Card", statusService.findByStatusName("Pending"), 0, userJacky, currencyCode);
		creditCardService.persist(createCreditCardPending);
		
		// Initialize Transactions
		CreditCard creditCard = creditCardService.findByCreditCardNumber("1234-5678-1234-5678");
		CreditCard creditCard2 = creditCardService.findByCreditCardNumber("2345-5678-2398-5128");
		creditCard.setMinBalancePaid(50);
		creditCard2.setMinBalancePaid(50);
		MerchantCategoryCode mcc = merchantCategoryCodeService.findByMerchantCategory("Dining");
		MerchantCategoryCode mcc1 = merchantCategoryCodeService.findByMerchantCategory("Shopping");
		MerchantCategoryCode mcc2 = merchantCategoryCodeService.findByMerchantCategory("Travel");
		MerchantCategoryCode mcc3 = merchantCategoryCodeService.findByMerchantCategory("Bill");
		ForeignExchangeCurrency currency = foreignExchangeCurrencyService.getCurrencyByCode("SGD");
		ForeignExchangeCurrency currencyUSD = foreignExchangeCurrencyService.getCurrencyByCode("USD");
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
		Transaction transactionA1 = new Transaction("Initial Deposit", account, account.getBalance(), null, currency,
				currency.getCode() + " " + account.getBalance());
		Transaction transactionA2 = new Transaction("Initial Deposit", accountA2, accountA2.getBalance(), null, currency,
				currency.getCode() + " " + accountA2.getBalance());
		transactionService.persist(transactionA1);
		transactionService.persist(transactionA2);
		Double exchangeRateUSD = foreignExchangeCurrencyService.getExchangeRate(currencyUSD.getCode(), currency.getCode())
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
			transactionService.persist(t);
			transactionService.updateCreditCardBalance(t);
		}
		List<CreditCard> approvedCreditCards = creditCardService.findCreditCardsByStatus(statusName);
		transactionService.chargeMinimumBalanceFee(approvedCreditCards);
		creditCardService.calculateMonthlyBalance(approvedCreditCards);
		creditCardService.chargeInterest(approvedCreditCards);
		transactionService.updateInterest(approvedCreditCards);
		transactionService.scheduleInterestCharging();
    }
}
