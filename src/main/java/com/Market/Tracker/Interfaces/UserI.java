package com.Market.Tracker.Interfaces;

import java.util.List;

import com.Market.Tracker.Entities.Actives;
import com.Market.Tracker.Entities.UserEntity.contacts;
import com.Market.Tracker.Entities.UserEntity.currencies;

public interface UserI {
	
	public Long getId();
	
	public void setUsername(String newUsername);
	public String getUsername();
	
	public void setEmail(String newEmail);
	public String getEmail();
	
	public void setPassword(String newPassword);
	public String getPassword();
	
	public void setContact(contacts newContact);
	public contacts getContact();
	
	public void setWatchList(String newActive);
	public List<String> getWatchList();
	
	public void setCurrency(currencies newCurrency);
	public currencies getCurrency();
	
	void setUserActives(List<Actives> userActives);
	List<Actives> getUserActives();	
	
	public String getRole();
}