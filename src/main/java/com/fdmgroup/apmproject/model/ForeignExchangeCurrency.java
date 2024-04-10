package com.fdmgroup.apmproject.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "listOfForeignCurrencies")
public class ForeignExchangeCurrency {
	private int currencyId;
	private String currencyName;
	private int currencyValue;

	@OneToMany(mappedBy = "transactionCurrency", fetch = FetchType.EAGER)
	private List<Transaction> transactions = new ArrayList<>();
}
