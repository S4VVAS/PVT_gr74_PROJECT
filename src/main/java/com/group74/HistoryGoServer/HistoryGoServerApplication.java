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
	
	// URL example: getPlaces?lat=56&lon=46
	@GetMapping(value="/getPlaces")
	public JSONArray getPlaces(@RequestParam(required = true) String lat,
							   @RequestParam(required = true) String lon) {
		return req.getPlaces(lat, lon);
	}
	
	// URL example: getBoundedPlaces?swLat=59.0&swLon=18.0&neLat=59.5&neLon=18.5
	@GetMapping(value="/getBoundedPlaces")
	public JSONArray getPlaces(@RequestParam(required = true) String swLat,
							   @RequestParam(required = true) String swLon,
							   @RequestParam(required = true) String neLat,
							   @RequestParam(required = true) String neLon) {
		return req.getBoundedPlaces(swLat, swLon, neLat, neLon);
	}
	
	// URL example: getCoorPlaces?coors=59.3293678848816 18.0687667553766,59.3276747180171 18.0685436339484
	@GetMapping(value="/getCoorPlaces")
	public JSONArray getCoorPlaces(@RequestParam(required = true) String[] coors) {
		return req.getCoorPlaces(coors);
	}
	
}
