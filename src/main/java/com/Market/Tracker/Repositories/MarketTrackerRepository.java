package com.Market.Tracker.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import com.Market.Tracker.Entities.UserEntity;
import com.Market.Tracker.Models.UserDTO;

@Repository
public interface MarketTrackerRepository extends JpaRepository<UserEntity, Long> {
	public UserEntity findByUsername(String username);
	public UserEntity findByEmail(String email);
	
	@Query(value = "SELECT email, userActives FROM user WHERE userActives IS NOT NULL", nativeQuery = true)
	public List<UserDTO> findUsers();
}