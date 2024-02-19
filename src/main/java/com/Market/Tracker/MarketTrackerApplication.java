package com.Market.Tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "com.Market.Tracker.Repositories")
@ComponentScan(basePackages = "com.Market.Tracker")
@EntityScan(basePackages = "com.Market.Tracker.Entities")
@EnableScheduling
public class MarketTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketTrackerApplication.class, args);
	}

}
