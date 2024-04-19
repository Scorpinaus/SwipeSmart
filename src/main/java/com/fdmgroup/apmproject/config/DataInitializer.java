package com.fdmgroup.apmproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.fdmgroup.apmproject.service.ForeignExchangeCurrencyService;

/**
 * Component responsible for initializing data at application startup.
 * This class is a listener for {@link ApplicationReadyEvent}, which is published after the application
 * context has been refreshed and all beans are completely initialized. This class uses this event to
 * perform initial data loading tasks specifically designed to populate the database with foreign currency
 * exchange rate data from a JSON file.
 *
 * <p>This is typically used to perform any necessary startup tasks that are required before the application
 * becomes available to accept HTTP requests. In this case, it ensures that the foreign exchange currency
 * data is loaded and saved to the database automatically at startup, making the data immediately available
 * for use across the application.</p>
 *
 * <h2>Process Flow</h2>
 * <ol>
 *   <li>The Spring context raises an {@link ApplicationReadyEvent} when the application is fully loaded.</li>
 *   <li>This class listens to that event and triggers the foreign currency loading process.</li>
 *   <li>The {@link ForeignExchangeCurrencyService#loadAndSaveForeignCurrencyJSON} method is called to
 *       handle the actual loading and persisting of currency data from the JSON source.</li>
 * </ol>
 *
 * <h2>Dependencies</h2>
 * <ul>
 *   <li>{@link ForeignExchangeCurrencyService} - Autowired service that contains the logic to
 *       load and persist currency data. This service encapsulates all data handling logic specific to
 *       foreign exchange currencies.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <p>This component is automatically detected and registered by Spring's component scanning because
 * it is annotated with {@link org.springframework.stereotype.Component}. It does not need to be manually
 * instantiated or called, as Spring handles its lifecycle based on the application events.</p>
 *
 * @author Hafiz
 * @version 1.0
 * @see ForeignExchangeCurrencyService
 * @see ApplicationReadyEvent
 */

//@Component
//public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
//	
//	@Autowired
//	private ForeignExchangeCurrencyService currencyService;
//	
//	@Override
//	public void onApplicationEvent(ApplicationReadyEvent event) {
//		currencyService.loadAndSaveForeignCurrencyJSON();
//	}
//
//}
