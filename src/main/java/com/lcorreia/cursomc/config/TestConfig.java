package com.lcorreia.cursomc.config;

import com.lcorreia.cursomc.services.DBService;
import com.lcorreia.cursomc.services.EmailService;
import com.lcorreia.cursomc.services.MockEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
@Profile("test")
public class TestConfig {

    @Autowired
    private DBService dbService;

    @Bean
    public boolean instatiateDatabase() throws ParseException {

        dbService.instantiateTestDabaBase();
        return true;
    }

    @Bean
    public EmailService emailService(){
        return new MockEmailService();
    }
}
