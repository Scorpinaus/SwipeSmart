package com.fdmgroup.apmproject.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.Status;
import com.fdmgroup.apmproject.model.User;
import com.fdmgroup.apmproject.repository.AccountRepository;

import jakarta.annotation.PostConstruct;

/**
 * This class is responsible for handling all business logic related to accounts.
 * 
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private StatusService statusService;

    private static final Logger logger = LogManager.getLogger(AccountService.class);

    /**
     * Constructs an AccountService with the specified account repository.
     *
     * @param accountRepo The account repository.
     */
    public AccountService(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    /**
     * Creates a new account.
     *
     * @param account The account to create.
     */
    public void persist(Account account) {
        Optional<Account> returnedAccount = accountRepo.findById(account.getAccountId());
        if (returnedAccount.isEmpty()) {
            accountRepo.save(account);
            logger.info("Account successfully created");
        } else {
            logger.warn("Account already exists");
        }
    }

    /**
     * Updates an existing account.
     *
     * @param account The account to update.
     */
    public void update(Account account) {
        Optional<Account> returnedAccount = accountRepo.findById(account.getAccountId());
        if (returnedAccount.isEmpty()) {
            logger.warn("Account does not exist in database");
        } else {
            accountRepo.save(account);
            logger.info("Account successfully updated");
        }
    }

    /**
     * Retrieves an account by its ID.
     *
     * @param accountId The ID of the account to retrieve.
     * @return The account with the specified ID, or null if no such account exists.
     */
    public Account findById(long accountId) {
        Optional<Account> returnedAccount = accountRepo.findById(accountId);
        if (returnedAccount.isEmpty()) {
            logger.warn("Could not find Account in Database");
            return null;
        } else {
            logger.info("Returning Account's details");
            return returnedAccount.get();
        }
    }

    /**
     * Deletes an account by its ID.
     *
     * @param accountId The ID of the account to delete.
     */
    public void deleteById(long accountId) {
        Optional<Account> returnedAccount = accountRepo.findById(accountId);
        if (returnedAccount.isEmpty()) {
            logger.warn("Account does not exist in database");
        } else {
            accountRepo.deleteById(accountId);
            logger.info("Account deleted from Database");
        }
    }

    /**
     * Finds a bank account according to account Number.
     *
     * @param accountNumber The account number of the account to find.
     * @return The account with the specified account number, or null if no such account exists.
     */
    public Account findAccountByAccountNumber(String accountNumber) {
        Optional<Account> retrievedAccount = accountRepo.findByAccountNumber(accountNumber);
        if (retrievedAccount.isEmpty()) {
            logger.warn("Bank account does not exist in database");
            return null;
        } else {
            Account account = retrievedAccount.get();
            return account;
        }
    }

    /**
     * Calculates the new account balance after a withdrawal.
     *
     * @param retrievedAccountBalance The current account balance.
     * @param amount The amount to withdraw.
     * @return The new account balance after the withdrawal.
     */
    public double withdrawAccountByAmount(BigDecimal retrievedAccountBalance, BigDecimal amount) {
        BigDecimal newBalance = retrievedAccountBalance.subtract(amount);
        Double newAccountBalance = newBalance.doubleValue();
        return newAccountBalance;
    }

    /**
     * Finds all bank accounts associated with a specific user.
     *
     * @param userId The ID of the user whose accounts should be retrieved.
     * @return A list of all bank accounts associated with the specified user.
     */
    public List<Account> findAllAccountsByUserId(long userId) {
        return accountRepo.findByAccountUserUserId(userId);
    }

    /**
     * Retrieves all bank accounts in the database.
     *
     * @return A list of all bank accounts.
     */
    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }

    /**
     * Generates a unique bank account number.
     *
     * @return A unique bank account number.
     */
    public String generateUniqueAccountNumber() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        do {
            sb.setLength(0); // Clear the StringBuilder before generating a new account number
            for (int i = 0; i < 9; i++) {
                sb.append(random.nextInt(10));
                if ((i + 1) % 3 == 0 && i != 8) {
                    sb.append("-"); // Adds a dash after every 3 digits, except the last set of 3 digits
                }
            }
        } while (findAccountByAccountNumber(sb.toString()) != null);

        return sb.toString();
    }

    /**
     * Initializes the database with some sample accounts.
     */
    @PostConstruct
    public void intiAccounts() {
        User userJacky = userService.findUserByUsername("jackytan");
        Status statusName = statusService.findByStatusName("Approved");
        String currencyCode = "SGD";
        Account account = new Account("Savings", 5000, "123-123-123", userJacky, statusName, currencyCode);
        Account account2 = new Account("Current", 10000, "124-124-124", userJacky, statusName, currencyCode);
        Account accountPending = new Account("pending ac", 10000, "125-125-125", userJacky, statusService.findByStatusName("Pending"), currencyCode);
        Account accountPending2 = new Account("pending ac2", 10123, "126-126-126", userJacky, statusService.findByStatusName("Pending"), currencyCode);

        persist(account);
        persist(account2);
        persist(accountPending);
        persist(accountPending2);
    }
}