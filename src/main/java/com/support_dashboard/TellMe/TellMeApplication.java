package com.support_dashboard.TellMe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//1. @SpringBootApplication
//
//This is the brain of your app
//
//It does 3 things:
//
//Enables auto-configuration
//Scans your packages
//Registers beans
//
//👉 Without this, nothing works.

public class TellMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TellMeApplication.class, args);
	}

}
