package com.fdmgroup.apmproject.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fdmgroup.apmproject.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
	@Query("SELECT t FROM Transaction t WHERE t.transactionDate >= :startDate AND t.transactionAmount >= :minAmount AND t.transactionType = :transactionType")
    List<Transaction> findTransactionsByDateAmountAndType(LocalDateTime startDate, double minAmount, String transactionType);

	
}
