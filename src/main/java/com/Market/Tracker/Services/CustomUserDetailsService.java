package com.Market.Tracker.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Market.Tracker.Entities.UserEntity;
import com.Market.Tracker.Entities.UserEntity.contacts;
import com.Market.Tracker.Entities.UserEntity.currencies;
import com.Market.Tracker.Models.RegisterDTO;
import com.Market.Tracker.Repositories.MarketTrackerRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private MarketTrackerRepository repository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, IllegalStateException {
		UserEntity user = repository.findByUsername(username); 
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password");
		}
		
		return new UserEntity(user.getUsername(), user.getPassword(), user.getRole());
    }
    
    public boolean getAuth(RegisterDTO user) {
		UserDetails userCredentials = loadUserByUsername(user.username());
		if (user.username().equals(userCredentials.getUsername())) {
			if (user.password().equals(userCredentials.getPassword())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}	
    }
    
    public UserEntity save(UserEntity userRegistry) {
    	UserEntity user = new UserEntity(userRegistry.getUsername(), userRegistry.getEmail(), userRegistry.getPassword(), userRegistry.getContact(), userRegistry.getCurrency());
    	return repository.save(user);
    }
}