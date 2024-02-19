package com.Market.Tracker.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import com.Market.Tracker.Interfaces.ActivesI;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


// Load the API response to a Java object
@Entity
public class Actives implements ActivesI{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;
	private String symbol;
	private float minBuyPrice;
	private float maxBuyPrice;
	private float minSellPrice;
	private float maxSellPrice;
	private String name;
	private float currentPrice;

	// CONTRUCTORS
	public Actives() {};
	
	public Actives(String symbol, String currentPrice) {
		this.symbol = symbol;
		this.currentPrice = Float.parseFloat(currentPrice);
	}
	
	public Actives(String symbol, String name, float currentPrice, float minBuyPrice, float maxBuyPrice, float minSellPrice, float maxSellPrice) {
		this.name = name;
		this.symbol = symbol;
		this.currentPrice = currentPrice;
		this.minBuyPrice = minBuyPrice;
		this.maxBuyPrice = maxBuyPrice;
		this.minSellPrice = minSellPrice;
		this.maxSellPrice = maxSellPrice;
	}

	// GETTERS AND SETTERS
	
	@Override
	public void setSymbol(String newSymbol) {
		this.symbol = newSymbol;
		
	}


	@Override
	public String getSymbol() {
		return this.symbol;
	}


	@Override
	public void setName(String newName) {
		this.name = newName;
	}


	@Override
	public String getName() {
		return this.name;
	}


	@Override
	public void setCurrentPrice(float newCurrentPrice) {
		this.currentPrice = newCurrentPrice;
	}


	@Override
	public float getCurrentPrice() {
		return this.currentPrice;
	}


	@Override
	public void setMinBuyPrice(float newMinBuyPrice) {
		this.minBuyPrice = newMinBuyPrice;
	}


	@Override
	public float getMinBuyPrice() {
		return this.minBuyPrice;
	}


	@Override
	public void setMaxBuyPrice(float newMaxBuyPrice) {
		this.maxBuyPrice = newMaxBuyPrice;
	}


	@Override
	public float getMaxBuyPrice() {
		return this.maxBuyPrice;
	}


	@Override
	public void setMinSellPrice(float newMinSellPrice) {
		this.minSellPrice = newMinSellPrice;
	}


	@Override
	public float getMinSellPrice() {
		return this.minSellPrice;
	}


	@Override
	public void setMaxSellPrice(float newMaxSellPrice) {
		this.maxSellPrice = newMaxSellPrice;
	}


	@Override
	public float getMaxSellPrice() {
		return this.maxSellPrice;
	}

	// Verify if the current price is within the buyable range
	@Override
	public boolean isBuyable(float currenPrice) {
		if (currentPrice >= this.minBuyPrice && currentPrice <= this.maxBuyPrice) {
			return true;
		} else {
			return false;
		}		
	}

	// Verify if the current price is within the selleable range
	@Override
	public boolean isSelleable(float currenPrice) {
		if ( currentPrice >= this.minSellPrice && currentPrice <= this.maxSellPrice) {
			return true;
		} else {
			return false;
		}		
	}
	
	
}
