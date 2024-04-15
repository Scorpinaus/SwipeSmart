package com.fdmgroup.apmproject.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.CreditCard;
import com.fdmgroup.apmproject.repository.CreditCardRepository;

@Service
public class CreditCardService {
	@Autowired
	private CreditCardRepository creditCardRepo;
	private List<String> numList;

	private static Logger logger = LogManager.getLogger(CreditCardService.class);
	
	public CreditCardService(CreditCardRepository creditCardRepo) {
		this.creditCardRepo = creditCardRepo;
	}
	
	public void persist(CreditCard creditCard) {
		Optional<CreditCard> returnedCreditCard = creditCardRepo.findById(creditCard.getCreditCardId());
		if (returnedCreditCard.isEmpty()) {
			creditCardRepo.save(creditCard);
			logger.info("Credit Card successfully created");
		} else {
			logger.warn("Credit Card already exists");
		}
	}
	
	public void update(CreditCard creditCard) {
		Optional<CreditCard> returnedCreditCard = creditCardRepo.findById(creditCard.getCreditCardId());
		if (returnedCreditCard.isEmpty()) {
			logger.warn("Credit Card does not exist in database");
		} else {
			creditCardRepo.save(creditCard);
			logger.info("Credit Card successfully updated");
		}
	}
	
	public CreditCard findById(long creditCardId) {
		Optional<CreditCard> returnedCreditCard = creditCardRepo.findById(creditCardId);
		if (returnedCreditCard.isEmpty()) {
			logger.warn("Could not find Credit Card in Database");
			return null;
		} else {
			logger.info("Returning Credit Card's details");
			return returnedCreditCard.get();
		}
	}
	
	public void deleteById(long creditCardId) {
		Optional<CreditCard> returnedCreditCard = creditCardRepo.findById(creditCardId);
		if (returnedCreditCard.isEmpty()) {
			logger.warn("Credit Card does not exist in database");
		} else {
			creditCardRepo.deleteById(creditCardId);
			logger.info("Credit Card deleted from Database");
		}
	}
	
	public CreditCard findByCreditCardNumber(String number) {
		Optional<CreditCard> returnedCreditCard = creditCardRepo.findByCreditCardNumber(number);
		if (returnedCreditCard.isEmpty()) {
			logger.warn("Could not find Credit Card in Database");
			return null;
		} else {
			logger.info("Returning Credit Card's details");
			return returnedCreditCard.get();
		}
	}
	
	public String generateCreditCardNumber() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
            if ((i + 1) % 4 == 0 && i != 15) {
                sb.append("-"); // Adds a dash after every 4 digits, except the last set of 4 digits
            }
        }
        return sb.toString();
    }
	
	@SuppressWarnings("unused")
	public String validCreditCardNumber() {
		String ccNumber = generateCreditCardNumber();
		if (numList.contains(ccNumber)) {
			return validCreditCardNumber();
		} else {
			numList.add(ccNumber);
			return ccNumber;
		}
	}
	
	@SuppressWarnings("unused")
	public String generatePinNumber() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
