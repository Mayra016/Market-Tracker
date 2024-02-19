package com.Market.Tracker.Interfaces;

public interface ActivesI {
	// GETTERS AND SETTERS
	
	public void setSymbol(String newSymbol);
	public String getSymbol();
	
	public void setName(String newName);
	public String getName();
	
	public void setCurrentPrice(float newCurrentPrice);
	public float getCurrentPrice();
	
	public void setMinBuyPrice(float newMinBuyPrice);
	public float getMinBuyPrice();
	
	public void setMaxBuyPrice(float newMaxBuyPrice);
	public float getMaxBuyPrice();
	
	public void setMinSellPrice(float newMinSellPrice);
	public float getMinSellPrice();
	
	public void setMaxSellPrice(float newMaxSellPrice);
	public float getMaxSellPrice();
	
	public boolean isBuyable(float currenPrice);
	public boolean isSelleable(float currenPrice);
}
