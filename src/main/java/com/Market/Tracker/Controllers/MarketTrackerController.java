package com.Market.Tracker.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.Market.Tracker.Entities.Actives;
import com.Market.Tracker.Entities.UserEntity;
import com.Market.Tracker.Entities.UserEntity.contacts;
import com.Market.Tracker.Entities.UserEntity.currencies;
import com.Market.Tracker.Models.RegisterDTO;
import com.Market.Tracker.Repositories.MarketTrackerRepository;
import com.Market.Tracker.Services.CustomUserDetailsService;
import com.Market.Tracker.Services.EmailService;
import com.Market.Tracker.Services.MarketTrackerService;


@Controller
public class MarketTrackerController {


	@Autowired
	private CustomUserDetailsService userService;
	
	@Autowired
	private MarketTrackerRepository repository;
	@Autowired
	private MarketTrackerService service;
	
	@Autowired
	private EmailService emailService;
	
	private UserEntity user = new UserEntity();
	
	// App presentation	
	@GetMapping({"/home", "/"})
	public String home() {
		return "index";
	}
	  
	// Login and create a new account
	@GetMapping("/login")
	public String login(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication.isAuthenticated()) {
	        Object principal = authentication.getPrincipal();

	       
	        if (principal instanceof UserDetails) {
	            UserDetails userDetails = (UserDetails) principal;

	            String username = userDetails.getUsername();
	            user = service.getUser(username);
	        }
	        
	        if (user == null) {
	        	
	        }
		}    
		model.addAttribute("user", user);
		return "login";
	}
	
	  
	// Create new user
	@PostMapping("/new-user")
	public String createNewUser(@Valid UserEntity user, Model model) {
		userService.save(user);
		return "redirect:/search";		  
	}
	
	// User authentication
	@PostMapping("/user-auth")
	public String userAuth(@Valid RegisterDTO user, Model model) {
		boolean auth = userService.getAuth(user);
		if (auth) {
			return "redirect:/search";
		} else {
			return "redirect:/login";
		}
	}
	
	// Save watchlist values
	@PostMapping("/save/watchlist")
	public String saveWatchList(Model model) {
		repository.save(user);
		return "redirect:/search";		  
	}
	
	// Add a new active to user watchlist
	@PostMapping("/save-active/{active}")
	public String save(@PathVariable String active, Model model) {
		user.setWatchList(active);
		repository.save(user);
		emailService.updateUsersInfo(user.getEmail(), user);
		return "redirect:/search";	  
	}
	  
	// Search actives and add then to the watchlist
	@GetMapping("/search")
	public String search(Model model) {
		return "search";
	}
	
	// Search actives and add then to the watchlist
	@GetMapping("/search/{active}")
	public String search(@PathVariable String active, Model model) {
		List<String> searchResult = service.searchActive(active);
		model.addAttribute("searchResult", searchResult);
		return "search";
	}	
  
  	// Form to set min and max buyable and selleable values
  	@GetMapping("/edit-actives")
  	public String editValues(Model model) {
  		List<Actives> userWatchList = service.getWatchListInfos(user.getWatchList());
  		model.addAttribute("user", user);
  		model.addAttribute("userWatchList", userWatchList);
  		return "edit";
  	}
  
  	// Profile infos
  	@GetMapping("/profile")
  	public String getProfile(Model model) {
  		model.addAttribute("user", user);
  		return "profile";
  	}
}
