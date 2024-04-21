package com.example.paydaytrade.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("google.com");
        mailSender.setPort(587);
        mailSender.setUsername("alimamedov.m@gmail.com");
        mailSender.setPassword("testpassword");

        return mailSender;
    }
}
