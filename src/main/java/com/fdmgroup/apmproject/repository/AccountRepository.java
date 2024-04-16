package com.fdmgroup.apmproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fdmgroup.apmproject.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	
	
	List<Account> findByAccountUserUserId(long userId);
	
	
	Optional<Account> findByAccountNumber(String name);

	Optional<Account> findByAccountId(Long accountId);
	
	

}
