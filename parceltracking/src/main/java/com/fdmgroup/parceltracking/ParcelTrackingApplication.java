package com.fdmgroup.parceltracking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParcelTrackingApplication {

	private static Logger logger = LogManager.getLogger(ParcelTrackingApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ParcelTrackingApplication.class, args);
		logger.warn("Application has started");
	}

}
