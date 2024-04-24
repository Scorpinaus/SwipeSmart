package com.fdmgroup.apmproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
		currency.setCurrencyId(1);
	}
	
	@Test
	@DisplayName("1. Test persist function for new currency")
	void testPersistOne() {
		//Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findById(1)).thenReturn(Optional.empty());
		
		//Act
		currencyService.persist(currencyOne);
		
		//Assert
		verify(currencyRepo, times(1)).save(currencyOne);
	}
	
	@Test
	@DisplayName("2. Test persist function for existing currency")
	void testPersistTwo() {
		//Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findById(1)).thenReturn(Optional.of(currencyOne));
		
		//Act
		currencyService.persist(currencyOne);
		
		//Assert
		verify(currencyRepo, never()).save(any(ForeignExchangeCurrency.class));
	}
	
	@Test
	@DisplayName("3. Test update function for non-existing currency")
	void testUpdateOne() {
		//Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findById(1)).thenReturn(Optional.empty());
		
		//Act
		currencyService.update(currencyOne);
		
		//Assert
		verify(currencyRepo, never()).save(any(ForeignExchangeCurrency.class));
	}
	
	@Test
	@DisplayName("4. Test update function for existing currency")
	void testUpdateTwo() {
		//Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findById(1)).thenReturn(Optional.of(currencyOne));
		
		//Act
		currencyService.update(currencyOne);
		
		//Assert
		verify(currencyRepo).save(currencyOne);
	}
	
	@Test
	@DisplayName("5. Test findById function for non-existing currency")
	void testFindByIdOne() {
		//Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findById(currencyOne.getCurrencyId())).thenReturn(Optional.empty());
		
		//Act
		ForeignExchangeCurrency result = currencyService.findById(currencyOne.getCurrencyId());
		
		//Assert
		assertNull(result);
	}
	
	@Test
	@DisplayName("6. Test findById function for existing currency")
	void testFindByIdTwo() {
		//Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findById(currencyOne.getCurrencyId())).thenReturn(Optional.of(currencyOne));
		
		//Act
		ForeignExchangeCurrency result = currencyService.findById(currencyOne.getCurrencyId());
		
		//Assert
		assertNotNull(result);
		assertEquals(currencyOne, result);
	}
	
	@Test
	@DisplayName("7. Test deleteById function for non-existing currency")
	void testDeleteByIdOne() {
		//Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findById(currencyOne.getCurrencyId())).thenReturn(Optional.empty());
		
		//Act
		currencyService.deleteById(currencyOne.getCurrencyId());
		
		//Assert
		verify(currencyRepo, never()).deleteById(currencyOne.getCurrencyId());
	}
	
	@Test
	@DisplayName("8. Test deleteById function for existing currency")
	void testDeleteByIdTwo() {
		//Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findById(currencyOne.getCurrencyId())).thenReturn(Optional.of(currencyOne));
		
		//Act
		currencyService.deleteById(currencyOne.getCurrencyId());
		
		//Assert
		verify(currencyRepo).deleteById(currencyOne.getCurrencyId());
	}
	
	@Test
	@DisplayName("9. Test for getCurrencyByCode to retrieve specified currency")
	void testGetCurrencyByCodeOne() {
		//Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findByCurrencyCode(currencyOne.getCode())).thenReturn(currencyOne);
		
		//Act
		ForeignExchangeCurrency result = currencyService.getCurrencyByCode(currencyOne.getCode());
		
		//Assert
		assertNotNull(result, "The result should not be null");
		assertEquals(currencyOne, result, "Actual currency should match currencyOne");
	}
	
	@Test
	@DisplayName("9. Test for getAllCurrency to retrieve specified currency")
	void testAllCurrencyOne() {
		//Arrange
		ForeignExchangeCurrency currencyOne = currency;
		ForeignExchangeCurrency currencyTwo = currency;
		List<ForeignExchangeCurrency> expected = new ArrayList<>();
		expected.add(currencyOne);
		expected.add(currencyTwo);
		when(currencyRepo.findAll()).thenReturn(expected);
		
		//Act
		List<ForeignExchangeCurrency> result = currencyService.getAllCurrencies();
		
		//Assert
		assertEquals(expected, result, "Returned list of currencies should match actual");
		
	}
	
	@Test
	@DisplayName("Test getSupportedCurrencies to return only supported currency entries") 
	void testSupportedCurrenciesOne() {
		//Arrange
		ForeignExchangeCurrency currencyOne = currency;
		ForeignExchangeCurrency currencyTwo = currency;
		currencyTwo.setCode("HKD");
		ForeignExchangeCurrency currencyThree = currency;
		currencyTwo.setCode("SGD");
		ForeignExchangeCurrency currencyFour = currency;
		currencyTwo.setCode("CAD");
		ForeignExchangeCurrency currencyFive = currency;
		currencyTwo.setCode("MYR");
		List<ForeignExchangeCurrency> allCurrencies = new ArrayList<>();
		allCurrencies.add(currencyOne);
		allCurrencies.add(currencyTwo);
		allCurrencies.add(currencyThree);
		allCurrencies.add(currencyFour);
		allCurrencies.add(currencyFive);
		when(currencyService.getAllCurrencies()).thenReturn(allCurrencies);
		List<ForeignExchangeCurrency> expectedCurrencies = allCurrencies.stream().filter(currency ->List.of("SGD", "USD", "HKD").contains(currency.getCode())).collect(Collectors.toList());
		
		//Act
		List<ForeignExchangeCurrency> result = currencyService.getSupportedCurrencies();
		
		//Assert
		assertEquals(expectedCurrencies, result, "The returned list should be equal to the expected list");
	}
	
	@Test
	@DisplayName("Test currency conversion when both currencies are the same")
	void testCurrencyConversionOne() {
		//Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findByCurrencyCode(currencyOne.getCode())).thenReturn(currencyOne);
		
		//Act
		BigDecimal rate = currencyService.getExchangeRate(currencyOne.getCode(), currencyOne.getCode());
	;
	
		//Assert
		assertEquals(BigDecimal.ONE, rate);
}	
	@Test
	@DisplayName("Test currency conversion when directly convert to USD")
	void testCurrencyConversionTwo() {
		//Arrange
		ForeignExchangeCurrency currencyOne = currency;
		ForeignExchangeCurrency currencyTwo = new ForeignExchangeCurrency();
		currencyTwo.setCode("SGD");
		currencyTwo.setRate(1.34);
		when(currencyRepo.findByCurrencyCode(currencyOne.getCode())).thenReturn(currencyOne);
		when(currencyRepo.findByCurrencyCode(currencyTwo.getCode())).thenReturn(currencyTwo);
		
		//Act
		Double rate = currencyService.getExchangeRate(currencyOne.getCode(), currencyTwo.getCode()).doubleValue();
	;
	
		//Assert
		assertEquals(currencyTwo.getRate(), rate);

}
	@Test
	@DisplayName("Test currency conversion when convert from USD")
	void testCurrencyConversionThree() {
		//Arrange
		ForeignExchangeCurrency currencyOne = currency;
		ForeignExchangeCurrency currencyTwo = new ForeignExchangeCurrency();
		currencyTwo.setCode("SGD");
		currencyTwo.setRate(1.34);
		when(currencyRepo.findByCurrencyCode(currencyOne.getCode())).thenReturn(currencyOne);
		when(currencyRepo.findByCurrencyCode(currencyTwo.getCode())).thenReturn(currencyTwo);
		
		//Act
		Double rate = currencyService.getExchangeRate(currencyOne.getCode(), currencyTwo.getCode()).doubleValue();
	;
	
		//Assert
		assertEquals(currencyTwo.getRate(), rate);

} @Test
	@DisplayName("Test currency conversion when convert 2 non-USD currencies")
	void testCurrencyConversionFour() {
		//Arrange
	ForeignExchangeCurrency currencyUSD = currency;
		ForeignExchangeCurrency currencyOne = new ForeignExchangeCurrency();
		currencyOne.setCode("EUR");
		currencyOne.setRate(1.11);
		currencyOne.setInverseRate(0.9);
		ForeignExchangeCurrency currencyTwo = new ForeignExchangeCurrency();
		currencyTwo.setCode("JPY");
		currencyTwo.setRate(110);
		when(currencyRepo.findByCurrencyCode(currencyOne.getCode())).thenReturn(currencyOne);
		when(currencyRepo.findByCurrencyCode("USD")).thenReturn(currencyUSD);
		when(currencyRepo.findByCurrencyCode(currencyTwo.getCode())).thenReturn(currencyTwo);
		Double expectedRate = (currencyTwo.getRate() / currencyOne.getRate());
		DecimalFormat df = new DecimalFormat("#");
		String formattedRate = df.format(expectedRate);
		
		//Act
		Double rate = currencyService.getExchangeRate(currencyOne.getCode(), currencyTwo.getCode()).doubleValue();
		String resultRate = df.format(rate);
	
	
		//Assert
		assertEquals(formattedRate, resultRate);

}
	
}