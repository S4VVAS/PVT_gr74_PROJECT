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
	static final Requester reqTEST = new Requester();
	static final JSONParser parser = new JSONParser();

	
	public static void main(String[] args) {
		reqTEST.getPlaces(59.328, 18.096);
		
		JSONArr = new JSONObject[] {parser.createJSON("kaknäs", "tall building", 
				new String[] {"https://sv.wikipedia.org/wiki/Kakn%C3%A4stornet#/media/Fil:Kakn%C3%A4stornet_2008x.jpg"
						, "https://sv.wikipedia.org/wiki/Kakn%C3%A4stornet#/media/Fil:Kakn%C3%A4stornet_bygget_1966.jpg"})};
		SpringApplication.run(HistoryGoServerApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value ="name", defaultValue ="World")String name) {
		return "Hello World";

	}
	
	
	@GetMapping("/jsonTest")
	public JSONObject hello() {
		return JSONArr[0];
	}
	
	//URL FORMAT = http://localhost:8080/getPlace?lats=56&lats=46
	@RequestMapping(value="/getPlace", method = RequestMethod.GET)
	public String handleFileUpload(Model model, @RequestParam(value = "lats", required = true)List<String> lats) throws NumberFormatException{
		try {
			return reqTEST.getPlaces(Double.parseDouble(lats.get(0)), Double.parseDouble(lats.get(1)));
		}catch(NumberFormatException e) {}
		return null;
	}
}
