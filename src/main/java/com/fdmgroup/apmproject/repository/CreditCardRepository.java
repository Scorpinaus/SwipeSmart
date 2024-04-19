package com.fdmgroup.apmproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.model.User;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
	Optional<CreditCard> findByCreditCardNumber(String number);
	
	List<CreditCard> findByCreditCardUserUserId(long userId);
	
	List<CreditCard> findAll();
}
