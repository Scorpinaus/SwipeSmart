package com.fdmgroup.apmproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fdmgroup.apmproject.model.Account;
import com.fdmgroup.apmproject.model.User;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

Optional<Account> findByAccountNumber(String name);

Optional<Account> findByAccountId(Long accountId);

}
