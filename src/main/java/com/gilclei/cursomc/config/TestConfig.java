package com.gilclei.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.gilclei.cursomc.services.DBService;
import com.gilclei.cursomc.services.EmailService;
import com.gilclei.cursomc.services.SmtpEmailService;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException  {
		dbService.instantiateTestDatabase();
		return true;
	}
	
	@Bean
	public EmailService emailService() {
//		return new MockEmailService();
		return new SmtpEmailService();
	}

	
	@Override
	public void run(String... args) throws Exception {

		//metodo roda automaticamente

	}

}
