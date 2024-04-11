package com.fdmgroup.apmproject.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.MerchantCategoryCode;
import com.fdmgroup.apmproject.repository.MerchantCategoryCodeRepository;

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
			logger.info("Reward successfully created");
		} else {
			logger.warn("Reward already exists");
		}
	}
	
	public void update(MerchantCategoryCode merchantCategoryCode) {
		Optional<MerchantCategoryCode> returnedMerchantCategoryCode = merchantCategoryCodeRepo.findById(merchantCategoryCode.getMerchantCategoryCodeId());
		if (returnedMerchantCategoryCode.isEmpty()) {
			logger.warn("Reward does not exist in database");
		} else {
			merchantCategoryCodeRepo.save(merchantCategoryCode);
			logger.info("Reward successfully updated");
		}
	}
	
	public MerchantCategoryCode findById(int rewardId) {
		Optional<MerchantCategoryCode> returnedMerchantCategoryCode = merchantCategoryCodeRepo.findById(rewardId);
		if (returnedMerchantCategoryCode.isEmpty()) {
			logger.warn("Could not find Reward in Database");
			return null;
		} else {
			logger.info("Returning Reward's details");
			return returnedMerchantCategoryCode.get();
		}
	}
	
	public void deleteById(int rewardId) {
		Optional<MerchantCategoryCode> returnedMerchantCategoryCode = merchantCategoryCodeRepo.findById(rewardId);
		if (returnedMerchantCategoryCode.isEmpty()) {
			logger.warn("Reward does not exist in database");
		} else {
			merchantCategoryCodeRepo.deleteById(rewardId);
			logger.info("Reward deleted from Database");
		}
	}
}
