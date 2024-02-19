package com.Market.Tracker.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.Market.Tracker.Services.EmailService;

@Controller 
public class MailController {

	@Autowired
	private EmailService emailService;
	
	@PostMapping("/sendEmail/{symbol}/{price}/{userEmail}/{status}")
	public void sendEmail(@PathVariable String symbol, @PathVariable String price,
			@PathVariable String userEmail, @PathVariable String status) {
		emailService.sendEmail(symbol, price, userEmail, status);
	}
}
