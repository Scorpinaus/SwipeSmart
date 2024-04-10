package com.fdmgroup.apmproject.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "listOfRewards")
public class Reward {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Reward ID")
	private int rewardId;
	@Column(name = "Reward Name")
	private String rewardName;
	@Column(name = "Cashback Amount")
	private double cashbackAmount;
	@Column(name = "Miles Amount")
	private double milesAmount;

	@OneToMany(mappedBy = "transactionReward", fetch = FetchType.EAGER)
	private List<Transaction> transactions = new ArrayList<>();

	public Reward(int rewardId, String rewardName, double cashbackAmount) {
		setRewardId(rewardId);
		setRewardName(rewardName);
		setCashbackAmount(cashbackAmount);
	}

	public int getRewardId() {
		return rewardId;
	}

	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}

	public String getRewardName() {
		return rewardName;
	}

	public void setRewardName(String rewardName) {
		this.rewardName = rewardName;
	}

	public double getCashbackAmount() {
		return cashbackAmount;
	}

	public void setCashbackAmount(double cashbackAmount) {
		this.cashbackAmount = cashbackAmount;
	}

	public double getMilesAmount() {
		return milesAmount;
	}

	public void setMilesAmount(double milesAmount) {
		this.milesAmount = milesAmount;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Transaction transaction) {
		this.transactions.add(transaction);
	}
}
