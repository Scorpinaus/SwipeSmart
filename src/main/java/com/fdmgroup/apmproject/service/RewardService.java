package com.fdmgroup.apmproject.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.apmproject.model.Reward;
import com.fdmgroup.apmproject.repository.RewardRepository;

@Service
public class RewardService {
	@Autowired
	private RewardRepository rewardRepo;
	
	private static Logger logger = LogManager.getLogger(RewardService.class);
	
	public RewardService(RewardRepository rewardRepo) {
		this.rewardRepo = rewardRepo;
	}
	
	public void persist(Reward reward) {
		Optional<Reward> returnedReward = rewardRepo.findById(reward.getRewardId());
		if (returnedReward.isEmpty()) {
			rewardRepo.save(reward);
			logger.info("Reward successfully created");
		} else {
			logger.warn("Reward already exists");
		}
	}
	
	public void update(Reward reward) {
		Optional<Reward> returnedReward = rewardRepo.findById(reward.getRewardId());
		if (returnedReward.isEmpty()) {
			logger.warn("Reward does not exist in database");
		} else {
			rewardRepo.save(reward);
			logger.info("Reward successfully updated");
		}
	}
	
	public Reward findById(int rewardId) {
		Optional<Reward> returnedReward = rewardRepo.findById(rewardId);
		if (returnedReward.isEmpty()) {
			logger.warn("Could not find Reward in Database");
			return null;
		} else {
			logger.info("Returning Reward's details");
			return returnedReward.get();
		}
	}
	
	public void deleteById(int rewardId) {
		Optional<Reward> returnedReward = rewardRepo.findById(rewardId);
		if (returnedReward.isEmpty()) {
			logger.warn("Reward does not exist in database");
		} else {
			rewardRepo.deleteById(rewardId);
			logger.info("Reward deleted from Database");
		}
	}
}
