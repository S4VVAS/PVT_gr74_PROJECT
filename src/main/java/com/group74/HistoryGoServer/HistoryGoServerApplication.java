package com.group74.HistoryGoServer;

import org.json.simple.JSONArray;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HistoryGoServerApplication extends SpringBootServletInitializer {
	
	static final Requester req = new Requester();

	public static void main(String[] args) {
		SpringApplication.run(HistoryGoServerApplication.class, args);
	}
	
	//URL FORMAT = http://localhost:8080/getPlaces?lat=56&lon=46
	@GetMapping(value="/getPlaces")
	public JSONArray getPlaces(@RequestParam(required = true) double lat,
								@RequestParam(required = true) double lon) {
		return req.getPlaces(lat, lon);		
	}
}
