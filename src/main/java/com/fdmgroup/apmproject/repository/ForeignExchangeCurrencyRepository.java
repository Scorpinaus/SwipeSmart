package com.fdmgroup.apmproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.apmproject.model.ForeignExchangeCurrency;

/**
 * This interface extends the JpaRepository interface to provide additional methods for accessing and manipulating ForeignExchangeCurrency entities.
 * 
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
@Repository
public interface ForeignExchangeCurrencyRepository extends JpaRepository<ForeignExchangeCurrency, Integer> {

    /**
     * Finds a foreign exchange currency by its currency code.
     *
     * @param currencyCode The currency code.
     * @return A ForeignExchangeCurrency object.
     */
    ForeignExchangeCurrency findByCurrencyCode(String currencyCode);
}