package com.Market.Tracker.Controllers;

import java.util.List;
import com.Market.Tracker.Services.MarketTrackerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// MANAGE API PETITIONS TO SEND INFORMATIONS TO THE FRONTEND

@RestController
@RequestMapping("/search")
public class APIController {
	
	@Autowired
	MarketTrackerService service;
	
    @GetMapping("/autocomplete")
    public List<String> autocomplete(@RequestParam("term") String term) {
        List<String> autocompleteOptions = service.searchActive(term);
        System.out.println(autocompleteOptions);
        return autocompleteOptions;
    }
}