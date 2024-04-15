package com.fdmgroup.apmproject.repository;

import java.util.List;

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
	
	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.firstName = :firstName, u.lastName = :lastName, u.address = :address WHERE u.username = :username and u.password = :password")
	void updateUserDetails(@Param("username") String username, @Param("password") String password,
			@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("address") String address);

//	SELECT * FROM listOfAccounts WHERE `FK User ID` = 9;
	
}
