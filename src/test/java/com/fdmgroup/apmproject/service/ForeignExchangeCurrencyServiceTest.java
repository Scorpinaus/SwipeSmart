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

/**
 * Tests the functionality of the ForeignExchangeCurrencyService class methods.
 * This class contains unit tests to verify the behavior of methods in the
 * ForeignExchangeCurrencyService class.
 *
 * @see ForeignExchangeCurrencyService
 * @see ForeignExchangeCurrencyRepository
 */

@ExtendWith(MockitoExtension.class)
public class ForeignExchangeCurrencyServiceTest {

	@Mock
	private ForeignExchangeCurrencyRepository currencyRepo;

	@Mock
	private Logger logger;

	@InjectMocks
	private ForeignExchangeCurrencyService currencyService;

	private ForeignExchangeCurrency currency;

	/**
	 * Sets up the test environment by initializing a ForeignExchangeCurrency object
	 * with sample data. This method is executed before each test case to ensure a
	 * clean test environment.
	 */
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

	/**
	 * Tests the persist function of the ForeignExchangeCurrencyService class for
	 * adding a new currency. This method verifies that the persist function
	 * correctly adds a new currency to the repository.
	 *
	 * @param currencyOne The ForeignExchangeCurrency object representing the new
	 *                    currency to be added.
	 * @throws ExceptionType if there are any issues with the repository operation.
	 * @see ForeignExchangeCurrencyService#persist(ForeignExchangeCurrency)
	 * @see ForeignExchangeCurrencyRepository#findById(Object)
	 */
	@Test
	@DisplayName("1. Test persist function for new currency")
	void testPersistOne() {
		// Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findById(1)).thenReturn(Optional.empty());

		// Act
		currencyService.persist(currencyOne);

		// Assert
		verify(currencyRepo, times(1)).save(currencyOne);
	}

	/**
	 * Tests the persist function of the ForeignExchangeCurrencyService class for an
	 * existing currency. This method verifies that the persist function does not
	 * add an existing currency to the repository.
	 *
	 * @param currencyOne The ForeignExchangeCurrency object representing the
	 *                    existing currency.
	 * @throws ExceptionType if there are any issues with the repository operation.
	 * @see ForeignExchangeCurrencyService#persist(ForeignExchangeCurrency)
	 * @see ForeignExchangeCurrencyRepository#findById(Object)
	 */
	@Test
	@DisplayName("2. Test persist function for existing currency")
	void testPersistTwo() {
		// Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findById(1)).thenReturn(Optional.of(currencyOne));

		// Act
		currencyService.persist(currencyOne);

		// Assert
		verify(currencyRepo, never()).save(any(ForeignExchangeCurrency.class));
	}

	/**
	 * Tests the update function of the ForeignExchangeCurrencyService class for a
	 * non-existing currency. This method verifies that the update function does not
	 * save a non-existing currency to the repository.
	 *
	 * @param currencyOne The ForeignExchangeCurrency object representing the
	 *                    non-existing currency.
	 * @throws ExceptionType if there are any issues with the repository operation.
	 * @see ForeignExchangeCurrencyService#update(ForeignExchangeCurrency)
	 * @see ForeignExchangeCurrencyRepository#findById(Object)
	 */
	@Test
	@DisplayName("3. Test update function for non-existing currency")
	void testUpdateOne() {
		// Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findById(1)).thenReturn(Optional.empty());

		// Act
		currencyService.update(currencyOne);

		// Assert
		verify(currencyRepo, never()).save(any(ForeignExchangeCurrency.class));
	}

	/**
	 * Tests the update function of the ForeignExchangeCurrencyService class for an
	 * existing currency. This method verifies that the update function saves an
	 * existing currency to the repository.
	 *
	 * @param currencyOne The ForeignExchangeCurrency object representing the
	 *                    existing currency.
	 * @throws ExceptionType if there are any issues with the repository operation.
	 * @see ForeignExchangeCurrencyService#update(ForeignExchangeCurrency)
	 * @see ForeignExchangeCurrencyRepository#findById(Object)
	 */
	@Test
	@DisplayName("4. Test update function for existing currency")
	void testUpdateTwo() {
		// Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findById(1)).thenReturn(Optional.of(currencyOne));

		// Act
		currencyService.update(currencyOne);

		// Assert
		verify(currencyRepo).save(currencyOne);
	}

	/**
	 * Tests the findById function of the ForeignExchangeCurrencyService class for a
	 * non-existing currency. This method ensures that when the findById function is
	 * called with a non-existing currency ID, it returns null as expected.
	 *
	 * @param currencyOne The ForeignExchangeCurrency object representing the
	 *                    currency.
	 * @return null if the currency with the specified ID is not found.
	 * @throws ExceptionType if there are any issues with the repository operation.
	 * @see ForeignExchangeCurrencyService#findById(Object)
	 * @see ForeignExchangeCurrencyRepository#findById(Object)
	 */
	@Test
	@DisplayName("5. Test findById function for non-existing currency")
	void testFindByIdOne() {
		// Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findById(currencyOne.getCurrencyId())).thenReturn(Optional.empty());

		// Act
		ForeignExchangeCurrency result = currencyService.findById(currencyOne.getCurrencyId());

		// Assert
		assertNull(result);
	}

	/**
	 * Tests the findById function of the ForeignExchangeCurrencyService class for
	 * an existing currency. This method ensures that when the findById function is
	 * called with an existing currency ID, it returns the corresponding currency
	 * object.
	 *
	 * @param currencyOne The ForeignExchangeCurrency object representing the
	 *                    currency.
	 * @return The ForeignExchangeCurrency object representing the currency with the
	 *         specified ID.
	 * @throws ExceptionType if there are any issues with the repository operation.
	 * @see ForeignExchangeCurrencyService#findById(Object)
	 * @see ForeignExchangeCurrencyRepository#findById(Object)
	 */
	@Test
	@DisplayName("6. Test findById function for existing currency")
	void testFindByIdTwo() {
		// Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findById(currencyOne.getCurrencyId())).thenReturn(Optional.of(currencyOne));

		// Act
		ForeignExchangeCurrency result = currencyService.findById(currencyOne.getCurrencyId());

		// Assert
		assertNotNull(result);
		assertEquals(currencyOne, result);
	}

	/**
	 * Tests the deleteById function of the ForeignExchangeCurrencyService class for
	 * a non-existing currency. This method verifies that when the deleteById
	 * function is called with the ID of a non-existing currency, it does not
	 * perform any deletion operation.
	 *
	 * @param currencyOne The ForeignExchangeCurrency object representing the
	 *                    currency.
	 * @throws ExceptionType if there are any issues with the repository operation.
	 * @see ForeignExchangeCurrencyService#deleteById(Object)
	 * @see ForeignExchangeCurrencyRepository#deleteById(Object)
	 */
	@Test
	@DisplayName("7. Test deleteById function for non-existing currency")
	void testDeleteByIdOne() {
		// Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findById(currencyOne.getCurrencyId())).thenReturn(Optional.empty());

		// Act
		currencyService.deleteById(currencyOne.getCurrencyId());

		// Assert
		verify(currencyRepo, never()).deleteById(currencyOne.getCurrencyId());
	}

	/**
	 * Tests the deleteById function of the ForeignExchangeCurrencyService class for
	 * an existing currency. This method verifies that when the deleteById function
	 * is called with the ID of an existing currency, it performs the deletion
	 * operation.
	 *
	 * @param currencyOne The ForeignExchangeCurrency object representing the
	 *                    currency.
	 * @throws ExceptionType if there are any issues with the repository operation.
	 * @see ForeignExchangeCurrencyService#deleteById(Object)
	 * @see ForeignExchangeCurrencyRepository#deleteById(Object)
	 */
	@Test
	@DisplayName("8. Test deleteById function for existing currency")
	void testDeleteByIdTwo() {
		// Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findById(currencyOne.getCurrencyId())).thenReturn(Optional.of(currencyOne));

		// Act
		currencyService.deleteById(currencyOne.getCurrencyId());

		// Assert
		verify(currencyRepo).deleteById(currencyOne.getCurrencyId());
	}

	/**
	 * Tests the getCurrencyByCode function of the ForeignExchangeCurrencyService
	 * class to retrieve a specified currency by its code. This method verifies that
	 * when getCurrencyByCode is called with a currency code, it returns the
	 * corresponding currency object.
	 *
	 * @param code The currency code used to retrieve the currency.
	 * @return The ForeignExchangeCurrency object representing the retrieved
	 *         currency.
	 * @throws ExceptionType if there are any issues with the repository operation.
	 * @see ForeignExchangeCurrencyService#getCurrencyByCode(String)
	 * @see ForeignExchangeCurrencyRepository#findByCurrencyCode(String)
	 */
	@Test
	@DisplayName("9. Test for getCurrencyByCode to retrieve specified currency")
	void testGetCurrencyByCodeOne() {
		// Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findByCurrencyCode(currencyOne.getCode())).thenReturn(currencyOne);

		// Act
		ForeignExchangeCurrency result = currencyService.getCurrencyByCode(currencyOne.getCode());

		// Assert
		assertNotNull(result, "The result should not be null");
		assertEquals(currencyOne, result, "Actual currency should match currencyOne");
	}

	/**
	 * Tests the getAllCurrencies function of the ForeignExchangeCurrencyService
	 * class to retrieve all currencies. This method verifies that when
	 * getAllCurrencies is called, it returns a list of all currencies.
	 *
	 * @return A list of all ForeignExchangeCurrency objects representing all
	 *         currencies.
	 * @throws ExceptionType if there are any issues with the repository operation.
	 * @see ForeignExchangeCurrencyService#getAllCurrencies()
	 * @see ForeignExchangeCurrencyRepository#findAll()
	 */
	@Test
	@DisplayName("10. Test for getAllCurrency to retrieve specified currency")
	void testAllCurrencyOne() {
		// Arrange
		ForeignExchangeCurrency currencyOne = currency;
		ForeignExchangeCurrency currencyTwo = currency;
		List<ForeignExchangeCurrency> expected = new ArrayList<>();
		expected.add(currencyOne);
		expected.add(currencyTwo);
		when(currencyRepo.findAll()).thenReturn(expected);

		// Act
		List<ForeignExchangeCurrency> result = currencyService.getAllCurrencies();

		// Assert
		assertEquals(expected, result, "Returned list of currencies should match actual");

	}

	/**
	 * Tests the getSupportedCurrencies function of the
	 * ForeignExchangeCurrencyService class to retrieve only supported currencies.
	 * This method verifies that when getSupportedCurrencies is called, it returns a
	 * list of only supported currencies.
	 *
	 * @return A list of supported ForeignExchangeCurrency objects representing only
	 *         supported currencies.
	 * @throws ExceptionType if there are any issues with the repository operation.
	 * @see ForeignExchangeCurrencyService#getSupportedCurrencies()
	 * @see ForeignExchangeCurrencyService#getAllCurrencies()
	 */
	@Test
	@DisplayName("Test getSupportedCurrencies to return only supported currency entries")
	void testSupportedCurrenciesOne() {
		// Arrange
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
		List<ForeignExchangeCurrency> expectedCurrencies = allCurrencies.stream()
				.filter(currency -> List.of("SGD", "USD", "HKD").contains(currency.getCode()))
				.collect(Collectors.toList());

		// Act
		List<ForeignExchangeCurrency> result = currencyService.getSupportedCurrencies();

		// Assert
		assertEquals(expectedCurrencies, result, "The returned list should be equal to the expected list");
	}

	/**
	 * Tests currency conversion when both currencies are the same in the
	 * ForeignExchangeCurrencyService class. This method verifies that when
	 * getExchangeRate is called with the same source and target currencies, it
	 * returns a conversion rate of 1.
	 *
	 * @param sourceCurrencyCode The code of the source currency.
	 * @param targetCurrencyCode The code of the target currency.
	 * @return The conversion rate between the source and target currencies.
	 * @throws ExceptionType if there are any issues with the repository operation.
	 * @see ForeignExchangeCurrencyService#getExchangeRate(String, String)
	 * @see ForeignExchangeCurrencyRepository#findByCurrencyCode(String)
	 */
	@Test
	@DisplayName("Test currency conversion when both currencies are the same")
	void testCurrencyConversionOne() {
		// Arrange
		ForeignExchangeCurrency currencyOne = currency;
		when(currencyRepo.findByCurrencyCode(currencyOne.getCode())).thenReturn(currencyOne);

		// Act
		BigDecimal rate = currencyService.getExchangeRate(currencyOne.getCode(), currencyOne.getCode());
		;

		// Assert
		assertEquals(BigDecimal.ONE, rate);
	}

	/**
	 * Tests currency conversion when directly converting to USD in the
	 * ForeignExchangeCurrencyService class. This method verifies that when
	 * getExchangeRate is called with a source currency directly converting to USD,
	 * it returns the correct conversion rate based on the USD rate of the source
	 * currency.
	 *
	 * @param sourceCurrencyCode The code of the source currency.
	 * @param targetCurrencyCode The code of the target currency.
	 * @return The conversion rate between the source and target currencies.
	 * @throws ExceptionType if there are any issues with the repository operation.
	 * @see ForeignExchangeCurrencyService#getExchangeRate(String, String)
	 * @see ForeignExchangeCurrencyRepository#findByCurrencyCode(String)
	 */
	@Test
	@DisplayName("Test currency conversion when directly convert to USD")
	void testCurrencyConversionTwo() {
		// Arrange
		ForeignExchangeCurrency currencyOne = currency;
		ForeignExchangeCurrency currencyTwo = new ForeignExchangeCurrency();
		currencyTwo.setCode("SGD");
		currencyTwo.setRate(1.34);
		when(currencyRepo.findByCurrencyCode(currencyOne.getCode())).thenReturn(currencyOne);
		when(currencyRepo.findByCurrencyCode(currencyTwo.getCode())).thenReturn(currencyTwo);

		// Act
		Double rate = currencyService.getExchangeRate(currencyOne.getCode(), currencyTwo.getCode()).doubleValue();
		;

		// Assert
		assertEquals(currencyTwo.getRate(), rate);

	}

	/**
	 * Tests currency conversion when converting from USD in the
	 * ForeignExchangeCurrencyService class. This method ensures that when
	 * getExchangeRate is called with USD as the source currency and another
	 * currency as the target currency, it correctly returns the exchange rate based
	 * on the rate of the target currency against USD.
	 *
	 * @param sourceCurrencyCode The code of the source currency.
	 * @param targetCurrencyCode The code of the target currency.
	 * @return The conversion rate between the source and target currencies.
	 * @throws ExceptionType if there are any issues with the repository operation.
	 * @see ForeignExchangeCurrencyService#getExchangeRate(String, String)
	 * @see ForeignExchangeCurrencyRepository#findByCurrencyCode(String)
	 */
	@Test
	@DisplayName("Test currency conversion when convert from USD")
	void testCurrencyConversionThree() {
		// Arrange
		ForeignExchangeCurrency currencyOne = currency;
		ForeignExchangeCurrency currencyTwo = new ForeignExchangeCurrency();
		currencyTwo.setCode("SGD");
		currencyTwo.setRate(1.34);
		when(currencyRepo.findByCurrencyCode(currencyOne.getCode())).thenReturn(currencyOne);
		when(currencyRepo.findByCurrencyCode(currencyTwo.getCode())).thenReturn(currencyTwo);

		// Act
		Double rate = currencyService.getExchangeRate(currencyOne.getCode(), currencyTwo.getCode()).doubleValue();
		;

		// Assert
		assertEquals(currencyTwo.getRate(), rate);
	}

	/**
	 * Tests currency conversion between two non-USD currencies in the
	 * ForeignExchangeCurrencyService class. This method verifies that when
	 * getExchangeRate is invoked with two non-USD currencies, it correctly
	 * calculates the exchange rate based on the rates of the respective currencies.
	 *
	 * @param sourceCurrencyCode The code of the source currency.
	 * @param targetCurrencyCode The code of the target currency.
	 * @return The conversion rate between the source and target currencies.
	 * @throws ExceptionType if there are any issues with the repository operation.
	 * @see ForeignExchangeCurrencyService#getExchangeRate(String, String)
	 * @see ForeignExchangeCurrencyRepository#findByCurrencyCode(String)
	 */
	@Test
	@DisplayName("Test currency conversion when convert 2 non-USD currencies")
	void testCurrencyConversionFour() {
		// Arrange
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