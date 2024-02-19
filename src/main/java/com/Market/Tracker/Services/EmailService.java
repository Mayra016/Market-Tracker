package com.Market.Tracker.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.Market.Tracker.Entities.Actives;
import com.Market.Tracker.Entities.UserEntity;
import com.Market.Tracker.Models.UserDTO;
import com.Market.Tracker.Repositories.MarketTrackerRepository;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Service
public class EmailService {

	@Autowired
	MarketTrackerService marketService;
	@Autowired
	MarketTrackerRepository marketRepository;
	
	List<UserDTO> users;
	
	public void sendEmail(String symbol, String price, String userEmail, String status) {
			Email from = new Email("test@example.com");
		    String subject = symbol + " reached a tracked price";
		    Email to = new Email(userEmail);
		    Content content;
		    
		    if (status == "selleable") {
		    	content = new Content("text/plain", "The active ".concat(symbol).concat(" has reached an selleable value: ").concat(price));
		    }	else {
		    	content = new Content("text/plain", "The active ".concat(symbol).concat(" has reached an buyable value: ").concat(price));
		    }
		    
		    Mail mail = new Mail(from, subject, to, content);

		    SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
		    Request request = new Request();
		    try {
		      request.setMethod(Method.POST);
		      request.setEndpoint("mail/send");
		      request.setBody(mail.build());
		      Response response = sg.api(request);
		      System.out.println(response.getStatusCode());
		      System.out.println(response.getBody());
		      System.out.println(response.getHeaders());
		    } catch (IOException ex) {
		      ex.printStackTrace();
		    }

	}
	
	// From monday to friday at 6 am this method will obtain the users info to track the prices
	@Scheduled(cron = "0 0 6 * * MON-FRI")
	public void getUsersInfo() {
		users = marketRepository.findUsers();
	}
	
	// Each 30 minutes from monday to friday between 4 am and 5:59 pm the app will verify if the active has reached an desired value
	@Scheduled(cron = "0 0/30 4-17 * * MON-FRI")
	public void watchValues() {
		
		for ( UserDTO user : users ) {
			List<Actives> watchList = user.userActives();
			for ( Actives active : watchList ) {
				 Actives searchPrice = marketService.searchValue(active.getSymbol());
				 if (searchPrice.isBuyable(searchPrice.getCurrentPrice())) {
					 sendEmail(searchPrice.getSymbol(), String.valueOf(searchPrice.getCurrentPrice()), user.getEmail(), "buyable");
				 }
				 if(searchPrice.isSelleable(searchPrice.getCurrentPrice())) {
					 sendEmail(searchPrice.getSymbol(), String.valueOf(searchPrice.getCurrentPrice()), user.getEmail(), "selleable");
				 }
			}			
		}

	}
	
	// If an user changes its watchlist, this method will update the users info
	public void updateUsersInfo( String email, UserEntity user) {
		// get user index in users list
	    Byte[] array = new Byte[users.size()];
	    users.toArray(array);	        
	    Arrays.sort(array);        
	    int index = Arrays.binarySearch(array, email);	
	    
	    // update value
	    List<Actives> newActives = user.getUserActives();
	    users.remove(index);
	    users.add(new UserDTO(email, newActives));	        
	}
}
