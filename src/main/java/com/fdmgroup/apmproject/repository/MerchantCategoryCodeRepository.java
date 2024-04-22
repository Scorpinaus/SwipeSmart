package com.fdmgroup.apmproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdmgroup.apmproject.model.MerchantCategoryCode;

/**
 * This interface extends the JpaRepository interface to provide additional methods for accessing and manipulating MerchantCategoryCode entities.
 * 
 * @author Bard
 * @version 1.0
 * @since 2024-04-22
 */
@Repository
public interface MerchantCategoryCodeRepository extends JpaRepository<MerchantCategoryCode, Integer> {

    /**
     * Finds a merchant category code by its merchant category.
     *
     * @param merchantCategory The merchant category.
     * @return An optional MerchantCategoryCode object.
     */
    public Optional<MerchantCategoryCode> findByMerchantCategory(String merchantCategory);
}