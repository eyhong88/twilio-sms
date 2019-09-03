package com.example.twiliosms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TwilioSmsApplication  implements ApplicationRunner {

	@Autowired
	private SmsApp smsApp;

	public static void main(String[] args) {
		SpringApplication.run(TwilioSmsApplication.class, args);

	}

	@Override
	public void run(ApplicationArguments args) {
		smsApp.sms();
	}
}
