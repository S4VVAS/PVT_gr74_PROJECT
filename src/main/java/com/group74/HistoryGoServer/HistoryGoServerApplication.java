package com.group74.HistoryGoServer;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class HistoryGoServerApplication extends SpringBootServletInitializer {
	
	static JSONObject[] JSONArr;
	static final Requester requester = new Requester();
	static final JSONParser parser = new JSONParser();

	
	public static void main(String[] args) {
		SpringApplication.run(HistoryGoServerApplication.class, args);
	}
	
	//URL FORMAT = http://localhost:8080/getPlaces?lat=56&lon=46
	@GetMapping(value="/getPlaces")
	public JSONObject getPlaces(@RequestParam(required = true) double lat,
								@RequestParam(required = true) double lon) {
		return requester.getPlaces(lat, lon);		
	}
}
