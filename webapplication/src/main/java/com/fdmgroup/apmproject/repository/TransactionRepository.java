package com.fdmgroup.apmproject.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.Transaction;

/**
 * This interface extends the JpaRepository interface to provide additional methods for accessing and manipulating Transaction entities.
 * 
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds all transactions for the specified account within the specified date range.
     *
     * @param startOfMonth The start date of the range.
     * @param endOfMonth The end date of the range.
     * @param account The account to search for transactions.
     * @return A list of transactions for the specified account within the specified date range.
     */
    List<Transaction> findByTransactionDateBetweenAndTransactionAccount(LocalDateTime startOfMonth, LocalDateTime endOfMonth, Account account);

    /**
     * Finds all transactions for the specified credit card within the specified date range.
     *
     * @param startOfMonth The start date of the range.
     * @param endOfMonth The end date of the range.
     * @param creditcard The credit card to search for transactions.
     * @return A list of transactions for the specified credit card within the specified date range.
     */
    List<Transaction> findByTransactionDateBetweenAndTransactionCreditCard(LocalDateTime startOfMonth, LocalDateTime endOfMonth, CreditCard creditcard);

    /**
     * Finds all transactions for the specified account or recipient account.
     *
     * @param transactionAccount The account to search for transactions.
     * @param recipientAccount The recipient account to search for transactions.
     * @return A list of transactions for the specified account or recipient account.
     */
    List<Transaction> findByTransactionAccountOrRecipientAccount(Account transactionAccount, Account recipientAccount);
    
    /**
     * Retrieves transactions before a specified date associated with a given credit card.
     * <p>
     * This method queries the database for transactions that occurred before the specified date and were made using the provided credit card. It returns a list of transactions matching the criteria.
     *
     * @param date         The date before which transactions occurred.
     * @param creditCard   The credit card associated with the transactions.
     * @return A list of Transaction objects that meet the specified criteria.
     * @throws IllegalArgumentException if the date or credit card is null
     * @see org.example.Transaction
     */
    List<Transaction> findByTransactionDateBeforeAndTransactionCreditCard(LocalDateTime date, CreditCard creditCard);
    
    /**
     * Retrieves transactions associated with a specified credit card.
     * <p>
     * This method queries the database for transactions made using the provided credit card and returns a list of all matching transactions.
     *
     * @param creditCard The credit card associated with the transactions.
     * @return A list of Transaction objects associated with the specified credit card.
     * @throws IllegalArgumentException if the credit card is null
     * @see org.example.Transaction
     */
    List<Transaction> findByTransactionCreditCard(CreditCard creditCard);
    
    
    
}