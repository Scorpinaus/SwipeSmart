package com.fdmgroup.apmproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.apmproject.model.ForeignExchangeCurrency;

@Repository
public interface ForeignExchangeCurrencyRepository extends JpaRepository<ForeignExchangeCurrency, Long> {

}
