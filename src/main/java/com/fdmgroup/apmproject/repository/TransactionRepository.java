package com.fdmgroup.apmproject.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
	List<Transaction> findByTransactionDateBetweenAndTransactionAccount(LocalDateTime startOfMonth, LocalDateTime endOfMonth, Account account);
	
	List<Transaction> findByTransactionDateBetweenAndTransactionCreditCard(LocalDateTime startOfMonth, LocalDateTime endOfMonth, CreditCard creditcard);

}
