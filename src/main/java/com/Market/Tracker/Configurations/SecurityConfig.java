package com.Market.Tracker.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.Market.Tracker.Repositories.MarketTrackerRepository;
import com.Market.Tracker.Services.CustomUserDetailsService;



@EnableWebSecurity
@Configuration
@Order(1) 
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfiguration {
 
    @SuppressWarnings({ "removal", "deprecation" })
	@Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
        	.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions
                    .disable()
                )
            )
        	.cors().configurationSource(corsConfigurationSource())
        	.and()
        	.formLogin()
        		.loginPage("/login")
        		.defaultSuccessUrl("/search", true)
        		.permitAll()
        	.and()
            .requiresChannel()
            .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
            .requiresSecure()
	        .and()
	        .authorizeRequests()
	        .and() 
	        .authorizeRequests(requests -> requests
                    .requestMatchers(new AntPathRequestMatcher("/menu/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/{level}/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/lost")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/infos")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/languages")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/levels")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/updateData/**")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/update/**")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/add/**")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/delete/**")).authenticated()       
                    .anyRequest().authenticated())
                .formLogin()
                    .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
      	            .permitAll()
      	            .and()
      	        .logout()
      	      	    .logoutSuccessUrl("/login?logout")
      	            .permitAll()
      	            .and()
      	        .exceptionHandling()
      	      	    .accessDeniedPage("/login");
        	  

            return http.build();
          }

    @Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService()); 
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(); 
    }

    

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    private static CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8080"); 
        configuration.addAllowedOrigin("http://localhost:8443"); 
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedHeader("Access-Control-Allow-Headers");
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("X-Token");

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    

    

}   
    