package com.fdmgroup.apmproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.apmproject.model.MerchantCategoryCode;

@Repository
public interface MerchantCategoryCodeRepository extends JpaRepository<MerchantCategoryCode, Integer> {
	
	public Optional<MerchantCategoryCode> findByMerchantCategory(String merchantCategory);
}
