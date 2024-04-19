package com.fdmgroup.apmproject.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.MerchantCategoryCode;
import com.fdmgroup.apmproject.repository.MerchantCategoryCodeRepository;

import jakarta.annotation.PostConstruct;

@Service
public class MerchantCategoryCodeService {
	@Autowired
	private MerchantCategoryCodeRepository merchantCategoryCodeRepo;
	
	private static Logger logger = LogManager.getLogger(MerchantCategoryCodeService.class);
	
	public MerchantCategoryCodeService(MerchantCategoryCodeRepository rewardRepo) {
		this.merchantCategoryCodeRepo = rewardRepo;
	}
	
	public void persist(MerchantCategoryCode merchantCategoryCode) {
		Optional<MerchantCategoryCode> returnedMerchantCategoryCode = merchantCategoryCodeRepo.findById(merchantCategoryCode.getMerchantCategoryCodeId());
		if (returnedMerchantCategoryCode.isEmpty()) {
			merchantCategoryCodeRepo.save(merchantCategoryCode);
			logger.info("MCC successfully created");
		} else {
			logger.warn("MCC already exists");
		}
	}
	
	public void update(MerchantCategoryCode merchantCategoryCode) {
		Optional<MerchantCategoryCode> returnedMerchantCategoryCode = merchantCategoryCodeRepo.findById(merchantCategoryCode.getMerchantCategoryCodeId());
		if (returnedMerchantCategoryCode.isEmpty()) {
			logger.warn("MCC does not exist in database");
		} else {
			merchantCategoryCodeRepo.save(merchantCategoryCode);
			logger.info("MCC successfully updated");
		}
	}
	
	public MerchantCategoryCode findById(int id) {
		Optional<MerchantCategoryCode> returnedMerchantCategoryCode = merchantCategoryCodeRepo.findById(id);
		if (returnedMerchantCategoryCode.isEmpty()) {
			logger.warn("Could not find MCC in Database");
			return null;
		} else {
			logger.info("Returning MCC details");
			return returnedMerchantCategoryCode.get();
		}
	}
	
	public MerchantCategoryCode findByMerchantCategory(String MerchantCategory) {
		Optional<MerchantCategoryCode> returnedMerchantCategoryCode = merchantCategoryCodeRepo.findByMerchantCategory(MerchantCategory);
		if (returnedMerchantCategoryCode.isEmpty()) {
			logger.warn("Could not find MCC in Database");
			return null;
		} else {
			logger.info("Returning MCC details");
			return returnedMerchantCategoryCode.get();
		}
	}
	
	public void deleteById(int id) {
		Optional<MerchantCategoryCode> returnedMerchantCategoryCode = merchantCategoryCodeRepo.findById(id);
		if (returnedMerchantCategoryCode.isEmpty()) {
			logger.warn("MCC does not exist in database");
		} else {
			merchantCategoryCodeRepo.deleteById(id);
			logger.info("MCC deleted from Database");
		}
	}
	
	@PostConstruct
	public void initMCCs() {
		MerchantCategoryCode mcc = new MerchantCategoryCode(1000, "Dining");
		MerchantCategoryCode mcc1 = new MerchantCategoryCode(1001, "Shopping");
		MerchantCategoryCode mcc2 = new MerchantCategoryCode(1002, "Transport");
		MerchantCategoryCode mcc3 = new MerchantCategoryCode(1003, "Travel");
		MerchantCategoryCode mcc4 = new MerchantCategoryCode(1004, "Bill");
		MerchantCategoryCode mcc5 = new MerchantCategoryCode(1004, "Interest");
		persist(mcc);
		persist(mcc1);
		persist(mcc2);
		persist(mcc3);
		persist(mcc4);
		
	}
}
