package com.caiomoreno.cursomc.config;

import com.caiomoreno.cursomc.services.DBService;
import com.caiomoreno.cursomc.services.EmailService;
import com.caiomoreno.cursomc.services.MockEmailService;
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
    public boolean instantiateDatabase() throws ParseException {
        dbService.instantiateTestDataBase();
        return true;
    }

    @Bean
    public EmailService emailService(){
        return new MockEmailService();
    }
}
