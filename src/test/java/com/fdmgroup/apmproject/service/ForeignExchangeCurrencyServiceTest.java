package com.fdmgroup.apmproject.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.apmproject.model.ForeignExchangeCurrency;
import com.fdmgroup.apmproject.repository.ForeignExchangeCurrencyRepository;

@ExtendWith(MockitoExtension.class)
public class ForeignExchangeCurrencyServiceTest {
	
	@Mock
	private ForeignExchangeCurrencyRepository currencyRepo;
	
	@Mock
	private Logger logger;
	
	@InjectMocks
	private ForeignExchangeCurrencyService currencyService;
	
	private ForeignExchangeCurrency currency;
	
	@BeforeEach
	public void setUp() {
		currency = new ForeignExchangeCurrency();
		currency.setCode("USD");
		currency.setAlphaCode("USD");
		currency.setNumericCode("USD");
		currency.setName("United States Dollar");
		currency.setInverseRate(1.00);
		currency.setRate(1.00);
	}
	
	@Test
	@DisplayName("1. Test persist function for new currency")
	void testPersistOne() {
		//Arrange
		ForeignExchangeCurrency currencyOne = currency;
		currencyOne.setCurrencyId(1);
		when(currencyRepo.findById(1)).thenReturn(Optional.empty());
		
		//Act
		currencyService.persist(currencyOne);
		
		//Assert
		verify(currencyRepo, times(1)).save(currencyOne);
	}
	
	

}
