package com.itsight.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService securityService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(securityService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		
		http
		.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/web/**").permitAll()
		.antMatchers("/validation/recaptcha-v2").permitAll()
		.antMatchers("/media/**").permitAll();
		
		http.authorizeRequests()
		.antMatchers("/session-expirada").permitAll()
		.antMatchers("/session-multiple").permitAll()
		.antMatchers("/imagen-captcha/nueva").permitAll();

		
		http.authorizeRequests()	
			.antMatchers(
					"/css/**",
					"/js/**",
					"/img/**",
					"/fonts/**",
					"/sound/**"
			).permitAll()
			.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/login")
		.loginProcessingUrl("/loginCheck")
		.usernameParameter("username")
		.passwordParameter("password")
		.defaultSuccessUrl("/administracion/bienvenido")
		.failureUrl("/login?error=error").permitAll()
		.and()
		.logout()
		.deleteCookies("SESSION")
		.logoutUrl("/logout")
		.logoutSuccessUrl("/login").permitAll()
		.and()
		.exceptionHandling()
		.accessDeniedPage("/deniedAccess")
		.and()
		.sessionManagement()
		.maximumSessions(5)
		.expiredUrl("/login?error=session-expired")
		.maxSessionsPreventsLogin(true)
		.sessionRegistry(sessionRegistry());
	}
	
	// Work around https://jira.spring.io/browse/SEC-2855
	@Bean
	public SessionRegistry sessionRegistry() {
	    SessionRegistry sessionRegistry = new SessionRegistryImpl();
	    return sessionRegistry;
	}
}
