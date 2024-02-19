package com.Market.Tracker;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.Market.Tracker.Entities.*;
import com.Market.Tracker.Services.MarketTrackerService;


public class MarketTrackerServiceTest {
	
	@Autowired
	MarketTrackerService service = new MarketTrackerService();
	
	@Test
	public void searchActiveValueTest() {
		Actives response = service.searchValue("IBM");
		System.out.println("Symbol: " + response.getSymbol() + " Value: " + response.getCurrentPrice());
	}
	
	@Test
	public void searchActiveTest() {
		List<String> response = service.searchActive("IBM");
		System.out.println(response);
	}
}
