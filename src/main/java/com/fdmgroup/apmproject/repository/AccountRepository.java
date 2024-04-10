package com.fdmgroup.apmproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.apmproject.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
