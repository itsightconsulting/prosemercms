package com.itsight.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.itsight.service.EmailService;

@Component
public class EmailServiceImpl implements EmailService {
	
    public JavaMailSender emailSender;
    
    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
		// TODO Auto-generated constructor stub
    	this.emailSender = emailSender;
	}
 
    @Override
    public void sendSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("contoso.peru@gmail.com");
        message.setTo("peter.carranza@itsight.pe"); 
        message.setSubject("Scheduler Demo"); 
        message.setText("Demo");
        emailSender.send(message);
    }
}