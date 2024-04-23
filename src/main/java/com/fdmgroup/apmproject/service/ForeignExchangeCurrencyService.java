package com.fdmgroup.apmproject.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fdmgroup.apmproject.config.CurrencyDeserializer;
import com.fdmgroup.apmproject.model.ForeignExchangeCurrency;
import com.fdmgroup.apmproject.repository.ForeignExchangeCurrencyRepository;

import jakarta.annotation.PostConstruct;

@Service
public class ForeignExchangeCurrencyService {
	@Autowired
	private ForeignExchangeCurrencyRepository currencyRepo;

	@Autowired
	private ResourceLoader resourceLoader;

	private static Logger logger = LogManager.getLogger(ForeignExchangeCurrencyService.class);

	private static final String URL = "http://www.floatrates.com/daily/usd.json";

	public ForeignExchangeCurrencyService(ForeignExchangeCurrencyRepository currencyRepo) {
		this.currencyRepo = currencyRepo;
	}

	/**
	 * Registers a new foreign exchange currency or logs a warning if it already exists.
	 * <p>
	 * This method checks the repository to determine if a foreign exchange currency with the same identifier already exists. If the currency does not exist, it is saved to the repository, and an informational message is logged indicating successful registration. If the currency is already registered, a warning message is logged, and no further action is taken. This method helps in maintaining an accurate and non-duplicate list of available foreign exchange currencies.
	 *
	 * @param foreignExchangeCurrency The ForeignExchangeCurrency object to be registered or checked.
	 * @return void This method does not return a value but logs the outcome of the registration attempt.
	 * @throws DataAccessException If there are issues accessing the database or during the save operation.
	 * @see CurrencyRepo#findById(Object) Method to check if a foreign exchange currency already exists.
	 * @see CurrencyRepo#save(Object) Method to save a new foreign exchange currency into the database.
	 */
	public void persist(ForeignExchangeCurrency foreignExchangeCurrency) {
		Optional<ForeignExchangeCurrency> returnedCurrency = currencyRepo
				.findById(foreignExchangeCurrency.getCurrencyId());
		if (returnedCurrency.isEmpty()) {
			currencyRepo.save(foreignExchangeCurrency);
			logger.info("Foreign currency successfully registered");
		} else {
			logger.warn("Foreign currency already exists");
		}
	}

	/**
	 * Updates an existing foreign exchange currency's information in the database.
	 * <p>
	 * This method first checks if a foreign exchange currency with the specified identifier exists in the repository. If no such currency is found, a warning is logged indicating its absence. If the currency exists, it is updated with the provided details, and a confirmation message is logged. This process ensures that only existing currencies are updated, preventing the inadvertent creation of duplicate entries.
	 *
	 * @param foreignExchangeCurrency The ForeignExchangeCurrency object containing updated information to be saved.
	 * @return void This method does not return a value, but logs the outcome of the update operation.
	 * @throws DataAccessException If there are issues accessing the database or saving the updated currency data.
	 * @see CurrencyRepo#findById(Object) Method to verify the existence of the currency based on its identifier.
	 * @see CurrencyRepo#save(Object) Method to save the updated currency data into the database.
	 */
	public void update(ForeignExchangeCurrency foreignExchangeCurrency) {
		Optional<ForeignExchangeCurrency> returnedCurrency = currencyRepo
				.findById(foreignExchangeCurrency.getCurrencyId());
		if (returnedCurrency.isEmpty()) {
			logger.warn("Foreign Currency does not exist in database");
		} else {
			currencyRepo.save(foreignExchangeCurrency);
			logger.info("Foreign Currency successfully updated");
		}
	}

	/**
	 * Retrieves a foreign exchange currency from the database by its identifier.
	 * <p>
	 * This method searches the repository for a foreign exchange currency using the provided ID. If the currency is found, it is returned along with an informational log message. If no such currency is found, a warning is logged and the method returns null. This method is typically used to verify the existence and retrieve details of specific currencies.
	 *
	 * @param currencyId The unique identifier for the foreign exchange currency to be retrieved.
	 * @return ForeignExchangeCurrency object if found; null if no currency matches the given ID.
	 * @throws DataAccessException If there are issues accessing the database or retrieving the currency data.
	 * @see CurrencyRepo#findById(int) Method used to locate the currency by its identifier.
	 */
	public ForeignExchangeCurrency findById(int currencyId) {
		Optional<ForeignExchangeCurrency> returnedCurrency = currencyRepo.findById(currencyId);
		if (returnedCurrency.isEmpty()) {
			logger.warn("Could not find foreign currency in database");
			return null;
		} else {
			logger.info("Foreign currency exists in database");
			return returnedCurrency.get();
		}
	}

	/**
	 * Deletes a foreign exchange currency from the database by its identifier.
	 * <p>
	 * This method first checks if a foreign exchange currency with the specified identifier exists in the repository. If the currency is found, it is removed from the database and a confirmation message is logged. If no such currency exists, a warning is logged indicating that the currency could not be found, and no further action is taken. This method ensures precise management of currency data by preventing the deletion of non-existent entries.
	 *
	 * @param currencyId The unique identifier of the foreign exchange currency to be deleted.
	 * @return void This method does not return a value, but logs the outcome of the deletion process.
	 * @throws DataAccessException If there are issues accessing the database or executing the delete operation.
	 * @see CurrencyRepo#findById(int) Method to verify the existence of the currency based on its identifier.
	 * @see CurrencyRepo#deleteById(int) Method to remove the currency from the database.
	 */
	public void deleteById(int currencyId) {
		Optional<ForeignExchangeCurrency> returnedCurrency = currencyRepo.findById(currencyId);
		if (returnedCurrency.isEmpty()) {
			logger.warn("Currency does not exist in database");
		} else {
			currencyRepo.deleteById(currencyId);
			logger.info("Currency deleted from Database");
		}
	}

	/**
	 * Loads JSON data from a file and saves it into the database. This method is
	 * designed to load foreign currency exchange rate data from a JSON file located
	 * in the classpath, deserialize it into a list of
	 * {@link ForeignExchangeCurrency} objects, and then persist these objects into
	 * the database.
	 *
	 * The method utilizes Jackson's data binding features to parse and convert the
	 * JSON data. A custom deserializer, {@link CurrencyDeserializer}, is used to
	 * handle complex cases where the JSON structure does not directly map to the
	 * {@link ForeignExchangeCurrency} class structure. This deserializer is
	 * registered with Jackson's {@link ObjectMapper} through a
	 * {@link SimpleModule}.
	 *
	 * <p>
	 * Workflow:
	 * </p>
	 * <ol>
	 * <li>Fetches the JSON file named 'fx_rates.json' from the classpath.</li>
	 * <li>Creates an instance of {@link ObjectMapper} and registers the
	 * {@link CurrencyDeserializer}.</li>
	 * <li>Reads the JSON data into a list of {@link ForeignExchangeCurrency}
	 * objects using Jackson's {@link ObjectMapper}.</li>
	 * <li>Persists the list of currencies to the database using
	 * {@link JpaRepository#saveAll(Iterable)}.</li>
	 * </ol>
	 *
	 * <p>
	 * Exception Handling:
	 * </p>
	 * <ul>
	 * <li>{@link JsonParseException}: If the JSON file has an invalid format, a
	 * warning is logged, and a {@link RuntimeException} is thrown with a message
	 * indicating the JSON parsing error.</li>
	 * <li>{@link JsonMappingException}: If the JSON cannot be mapped to the
	 * {@link ForeignExchangeCurrency} class due to missing or incompatible
	 * attributes, a warning is logged, and a {@link RuntimeException} is thrown
	 * with a message indicating the mapping error.</li>
	 * <li>{@link IOException}: If there are issues reading the file, such as the
	 * file not existing, being inaccessible, or other I/O errors, a warning is
	 * logged, and a {@link RuntimeException} is thrown with a message indicating
	 * the I/O error.</li>
	 * </ul>
	 *
	 * @throws RuntimeException Thrown if an error occurs during the parsing,
	 *                          mapping, or reading of the JSON data. This exception
	 *                          wraps the underlying exception with a contextual
	 *                          message for clarity.
	 */
	public void loadAndSaveForeignCurrencyJSON() {
		try {
			InputStream inputStream = resourceLoader.getResource("classpath:fx_rates.json").getInputStream();
			ObjectMapper mapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addDeserializer(List.class, new CurrencyDeserializer());
			mapper.registerModule(module);
			logger.info("Mapper module ready for Json deserialization");

			List<ForeignExchangeCurrency> currencies = mapper.readValue(inputStream,
					new TypeReference<List<ForeignExchangeCurrency>>() {
					});
			currencyRepo.saveAll(currencies);
			logger.info("Currencies list is successfully updated to " + currencies.get(1).getDate());
		} catch (JsonParseException e) {
			logger.warn("Failed to parse JSON file: Invalid JSON format");
			throw new RuntimeException("Failed to parse JSON file: Invalid JSON format.", e);
		} catch (JsonMappingException e) {
			logger.warn("Failed to map JSON to Entity: Incompatible or missing attribute.");
			throw new RuntimeException("Failed to map JSON to Entity: Incompatible or missing attribute.", e);
		} catch (IOException e) {
			logger.warn("Failed to read JSON file: I/O error.");
			throw new RuntimeException("Failed to read JSON file: I/O error.", e);
		}
	}

	/**
	 * Retrieves a foreign exchange currency from the database by its currency code.
	 * <p>
	 * This method searches the repository for a currency using the provided code. If the currency is found, it is returned, and an informational message is logged stating that the currency was obtained. This method is commonly used to access currency details necessary for financial transactions and reporting.
	 *
	 * @param currencyCode The code of the foreign exchange currency to be retrieved, such as "USD" or "EUR".
	 * @return ForeignExchangeCurrency object if a currency with the specified code exists; otherwise, null.
	 * @throws DataAccessException If there are issues accessing the database or retrieving the currency data.
	 * @see CurrencyRepo#findByCurrencyCode(String) Method used to locate the currency by its code.
	 */
	public ForeignExchangeCurrency getCurrencyByCode(String currencyCode) {
		logger.info("Currency obtained");
		return currencyRepo.findByCurrencyCode(currencyCode);
	}

	/**
	 * Retrieves all foreign exchange currencies from the database.
	 * <p>
	 * This method fetches a list of all the currencies stored in the currency repository. It is typically used to provide an overview of available currencies for trading, analysis, or reporting purposes. The returned list may vary in size based on the number of currencies currently stored.
	 *
	 * @return List of ForeignExchangeCurrency objects representing all the currencies in the database; this list can be empty if no currencies are stored.
	 * @throws DataAccessException If there are issues accessing the database or retrieving the currencies.
	 * @see CurrencyRepo#findAll() Method to fetch all records from the currency repository.
	 */
	public List<ForeignExchangeCurrency> getAllCurrencies() {
		return currencyRepo.findAll();
	}

	/**
	 * Retrieves a list of supported foreign exchange currencies from the database.
	 * <p>
	 * This method filters the complete list of currencies to include only those with codes specified as supported (SGD, USD, HKD). It is used to provide a subset of currencies that are actively traded or recognized by this service. The method leverages streaming and filtering operations to efficiently gather the supported currencies based on pre-defined codes.
	 *
	 * @return List of ForeignExchangeCurrency objects that are supported, filtered by specific currency codes. This list may be empty if none of the specified currencies are stored.
	 * @throws DataAccessException If there are issues accessing the database or processing the currency data.
	 * @see CurrencyRepo#findAll() Indirectly used through getAllCurrencies to fetch all currency records.
	 */
	public List<ForeignExchangeCurrency> getSupportedCurrencies() {
		List<String> supportedCurrencyCodes = List.of("SGD", "USD", "HKD");
		List<ForeignExchangeCurrency> supportedCurrencies = getAllCurrencies().stream()
				.filter(currency -> supportedCurrencyCodes.contains(currency.getCode())).collect(Collectors.toList());
		return supportedCurrencies;
	}

	/**
	 * Calculates the exchange rate between two specified currencies.
	 * <p>
	 * This method retrieves the exchange rates for the base and target currencies from the database. If both currencies are the same, the exchange rate is set to 1. For conversions involving USD, it directly uses the stored rate for the non-USD currency. For other currency pairs, this method might require additional logic to calculate cross rates not shown here. This function ensures that accurate and up-to-date exchange rates are used for financial calculations and transactions.
	 *
	 * @param baseCurrencyCode The code of the currency from which to convert.
	 * @param targetCurrencyCode The code of the currency to which to convert.
	 * @return BigDecimal representing the exchange rate from the base currency to the target currency.
	 * @throws CurrencyNotFoundException If either the base or target currency does not exist in the database.
	 * @see CurrencyRepo#findByCurrencyCode(String) Method used to fetch currency details by code.
	 */
	public BigDecimal getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
		ForeignExchangeCurrency localCurrency = currencyRepo.findByCurrencyCode(baseCurrencyCode);
		ForeignExchangeCurrency foreignCurrency = currencyRepo.findByCurrencyCode(targetCurrencyCode);
		ForeignExchangeCurrency USCurrency = currencyRepo.findByCurrencyCode("USD");

		// If both currencies same, no conversion is required
		if (localCurrency.getCode().equals(foreignCurrency.getCode())) {
			BigDecimal exchangeRate = BigDecimal.valueOf(1);
			logger.info("No conversion required! BaseCurrencyCode" + baseCurrencyCode + " & targetCurrencyCode: "
					+ targetCurrencyCode);
			logger.info("The exchange rate used for this transaction is : " + exchangeRate);
			return exchangeRate;
		}

		// Direct conversion to USD
		if (targetCurrencyCode.equals(USCurrency.getCode())) {
			BigDecimal exchangeRate = BigDecimal.valueOf(localCurrency.getRate());
			logger.info("Conversion to USD: BaseCurrencyCode" + baseCurrencyCode + " & targetCurrencyCode: "
					+ targetCurrencyCode);
			logger.info("The exchange rate used for this transaction is : " + exchangeRate);
			return exchangeRate;
		}

		// Conversion from USD to another currency
		if (baseCurrencyCode.equals(USCurrency.getCode())) {
			BigDecimal exchangeRate = BigDecimal.valueOf(foreignCurrency.getRate());
			logger.info("Conversion from USD to another currency: BaseCurrencyCode" + baseCurrencyCode
					+ " & targetCurrencyCode: " + targetCurrencyCode);
			logger.info("The exchange rate used for this transaction is : " + exchangeRate);
			return exchangeRate;
		}

		// Conversion between two non-USD currencies using USD as an intermediary
		BigDecimal localCurrencyToUSD = BigDecimal.valueOf(localCurrency.getInverseRate()); // USD per Local Currency
		BigDecimal usdToForeignCurrency = BigDecimal.valueOf(foreignCurrency.getRate()); // USD per Foreign Currency

		// Calculate the exchange rate from local currency to foreign currency
		BigDecimal exchangeRate = usdToForeignCurrency.multiply(localCurrencyToUSD);

		logger.info("Conversion between 2 non-USD currencies: BaseCurrencyCode" + baseCurrencyCode
				+ " & targetCurrencyCode: " + targetCurrencyCode);
		logger.info("The conversion of local currency to USD is: " + localCurrencyToUSD
				+ " and the conversion of usd to Target Currency is " + usdToForeignCurrency);
		logger.info("The exchange rate used for this transaction is : " + exchangeRate);
		return exchangeRate;
	}

	/**
	 * Fetches and stores the latest foreign exchange rates from an external API.
	 * <p>
	 * This method checks for the existence of a local JSON file containing exchange rates. If the file exists, the update is skipped, and a log entry is made. If the file does not exist, the method fetches exchange rates from a predefined URL using an HTTP GET request. The fetched data is parsed into a map of currency codes to `ForeignExchangeCurrency` objects. If successful, this data is serialized to JSON and written to the local file system. If any part of the fetch or save process fails, appropriate warnings are logged.
	 *
	 * @return void This method does not return a value but logs the outcome of fetching and saving operations.
	 * @throws DataAccessException If there are issues accessing the API or local file system.
	 * @throws Exception If there are issues during the HTTP request, data parsing, or file writing processes.
	 * @see RestTemplate#exchange(String, HttpMethod, HttpEntity, ParameterizedTypeReference) For fetching data from the external API.
	 * @see ObjectMapper#writeValueAsString(Object) For serializing the currency data to JSON.
	 */
	public void fetchAndSaveExchangeRates() {
		// Check if fx_rates file exists
		if (Files.exists(Paths.get("src/main/resources/fx_rates.json"))) {
			logger.info("fx_rates.json file already exists and will not be updated.");
			return;
		}

		// Proceed to fetch data from the API
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map<String, ForeignExchangeCurrency>> response;
		try {
			response = restTemplate.exchange(URL, HttpMethod.GET, null,
				new ParameterizedTypeReference<Map<String, ForeignExchangeCurrency>>() {
				});
			if (response.getBody() == null) {
				throw new Exception("Failed to fetch data: No data received");
			}
		} catch (Exception e) {
			logger.warn("Error fetching currency data: " + e.getMessage());
			return;
		}
		
		Map<String, ForeignExchangeCurrency> foreignCurrencies = response.getBody();
		logger.info("Foreign Currencies Object ready for fetching");

		try {
			String json = new ObjectMapper().writeValueAsString(foreignCurrencies);
			Files.write(Paths.get("src/main/resources/fx_rates.json"), json.getBytes());
			logger.info("fx_rates.json file successfully updated");
		} catch (Exception e) {
			logger.warn("Error saving currency data: ", e.getMessage());
			return;
		}
	}

	@PostConstruct
	public void initCurrency() {
		fetchAndSaveExchangeRates();
		loadAndSaveForeignCurrencyJSON();
		ForeignExchangeCurrency currencyOne = new ForeignExchangeCurrency();
		currencyOne.setCode("USD");
		currencyOne.setAlphaCode("USD");
		currencyOne.setNumericCode("USD");
		currencyOne.setName("United States Dollar");
		currencyOne.setInverseRate(1.00);
		currencyOne.setRate(1.00);
		persist(currencyOne);

	}

}
