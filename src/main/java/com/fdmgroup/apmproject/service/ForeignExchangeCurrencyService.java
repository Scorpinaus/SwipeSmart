package com.fdmgroup.apmproject.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
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

	public ForeignExchangeCurrency getCurrencyByCode(String currencyCode) {
		logger.info("Currency obtained");
		return currencyRepo.findByCurrencyCode(currencyCode);
	}

	public List<ForeignExchangeCurrency> getAllCurrencies() {
		return currencyRepo.findAll();
	}

	public List<ForeignExchangeCurrency> getSupportedCurrencies() {
		List<String> supportedCurrencyCodes = List.of("SGD", "USD", "HKD");
		List<ForeignExchangeCurrency> supportedCurrencies = getAllCurrencies().stream()
				.filter(currency -> supportedCurrencyCodes.contains(currency.getCode())).collect(Collectors.toList());
		return supportedCurrencies;
	}

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

	public void fetchAndSaveExchangeRates() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map<String, ForeignExchangeCurrency>> response = restTemplate.exchange(URL, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, ForeignExchangeCurrency>>() {});
		Map<String, ForeignExchangeCurrency> foreignCurrencies = response.getBody();
		logger.info("Foreign Currencies Object ready for fetching");
		
		try {
			String json = new ObjectMapper().writeValueAsString(foreignCurrencies);
			Files.write(Paths.get("src/main/resources/fx_rates.json"), json.getBytes());
			logger.info("fx_rates.json file successfully updated");
		}	catch (Exception e) {
			e.printStackTrace();
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
