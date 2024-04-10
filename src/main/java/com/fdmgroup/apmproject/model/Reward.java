package com.fdmgroup.apmproject.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "listOfRewards")
public class Reward {
	private int rewardId;
	private String rewardName;
	private double cashbackAmount;
	private double milesAmount;

	@OneToMany(mappedBy = "transactionReward", fetch = FetchType.EAGER)
	private List<Transaction> transactions = new ArrayList<>();
}
