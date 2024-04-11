package com.fdmgroup.apmproject.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.ForeignExchangeCurrency;
import com.fdmgroup.apmproject.repository.ForeignExchangeCurrencyRepository;

@Service
public class ForeignExchangeCurrencyService {
	@Autowired
	private ForeignExchangeCurrencyRepository currencyRepo;
	
	private static Logger logger = LogManager.getLogger(ForeignExchangeCurrencyService.class);
	
	public ForeignExchangeCurrencyService(ForeignExchangeCurrencyRepository currencyRepo) {
		this.currencyRepo = currencyRepo;
	}
	
	public void persist(ForeignExchangeCurrency foreignExchangeCurrency) {
		Optional<ForeignExchangeCurrency> returnedCurrency = currencyRepo.findById(foreignExchangeCurrency.getCurrencyId());
		if (returnedCurrency.isEmpty()) {
			currencyRepo.save(foreignExchangeCurrency);
			logger.info("Customer successfully registered");
		} else {
			logger.warn("Customer already exists");
		}
	}
	
	public void update(ForeignExchangeCurrency foreignExchangeCurrency) {
		Optional<ForeignExchangeCurrency> returnedCurrency = currencyRepo.findById(foreignExchangeCurrency.getCurrencyId());
		if (returnedCurrency.isEmpty()) {
			logger.warn("Customer does not exist in database");
		} else {
			currencyRepo.save(foreignExchangeCurrency);
			logger.info("Customer successfully updated");
		}
	}
	
	public ForeignExchangeCurrency findById(int currencyId) {
		Optional<ForeignExchangeCurrency> returnedCurrency = currencyRepo.findById(currencyId);
		if (returnedCurrency.isEmpty()) {
			logger.warn("Could not find Customer in Database");
			return null;
		} else {
			logger.info("Returning customer's details");
			return returnedCurrency.get();
		}
	}
	
	public void deleteById(int currencyId) {
		Optional<ForeignExchangeCurrency> returnedCurrency = currencyRepo.findById(currencyId);
		if (returnedCurrency.isEmpty()) {
			logger.warn("Customer does not exist in database");
		} else {
			currencyRepo.deleteById(currencyId);
			logger.info("Customer deleted from Database");
		}
	}
}
