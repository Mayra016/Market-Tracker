package com.Market.Tracker.Entities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.Market.Tracker.Interfaces.UserI;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;


@Entity
@Table(name="users")
public class UserEntity implements UserI, UserDetails{
	
	// VARIABLES DECLARATION
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true)
	private String username;
	private String email;
	@Column(name = "pass")
	private String password;
	@Enumerated(EnumType.STRING)
	private contacts contact;
	@Enumerated(EnumType.STRING)
	private currencies currency; 
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Actives> userActives = new ArrayList<>();
	
	@Transient
	private List<String> watchList = new ArrayList<>();

	public enum contacts {
		EMAIL, WHATTSAPP, BOTH;
	}

	public enum currencies {
		EURO, DOLAR, REAL;
	}
	
	private String role = "USER";
	
	// CONSTRUCTORS
	
	public UserEntity() {};
	
	public UserEntity(String username, String password, String role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public UserEntity(String username, String email, String password, contacts contact, currencies currency) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.contact = contact;
		this.currency = currency;
	}

	// GETTERS AND SETTERS
	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setUsername(String newUsername) {
		this.username = newUsername;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public void setEmail(String newEmail) {
		this.email = newEmail;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public void setPassword(String newPassword) {
		this.password = newPassword;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public void setContact(contacts newContact) {
		this.contact = newContact;
	}

	@Override
	public contacts getContact() {
		return this.contact;
	}

	@Override
	public void setWatchList(String newActive) {
		this.watchList.add(newActive);
	}

	@Override
	public List<String> getWatchList() {
		return this.watchList;
	}
	
	@Override
	public void setCurrency(currencies newCurrency) {
		this.currency = newCurrency;
	}
	
	@Override
	public currencies getCurrency() {
		return this.currency;
	}

	@Override
	public void setUserActives(List<Actives> userActives) {
		this.userActives = userActives;
	}
	
	@Override	
	public List<Actives> getUserActives() {
		return userActives;
	}
	
	@Override
	public String getRole() {
		return this.role;
	}
	
	// USER DETAILS CONFIG

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {	
		return List.of(new SimpleGrantedAuthority("USER"));
	}


	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}	
}