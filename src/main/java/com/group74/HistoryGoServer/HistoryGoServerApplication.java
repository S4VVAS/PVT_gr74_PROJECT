package com.group74.HistoryGoServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class HistoryGoServerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(HistoryGoServerApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value ="name", defaultValue ="World")String name) {
		return String.format("Hello %s", name);

	}
	
	@GetMapping("/bruh")
	public String bruh(@RequestParam(value ="name", defaultValue ="bruh")String name) {
		return String.format("Sup %s", name);

	}

}
