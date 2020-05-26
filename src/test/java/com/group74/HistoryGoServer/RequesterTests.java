package com.group74.HistoryGoServer;

import static org.junit.Assert.assertEquals;

import org.json.simple.JSONArray;
import org.junit.jupiter.api.Test;

public class RequesterTests {

	@Test
	public void getPlacesZeroResults() {
		Requester req = new Requester();
		JSONArray result = req.getPlaces(0, 0); 
		assertEquals(0, result.size());
	}
	
	@Test
	public void getPlacesManyResults() {
		Requester req = new Requester();
		JSONArray result = req.getPlaces(59.332, 18.064); // Koordinater för Sergels Torg
		assertEquals(25, result.size());
	}
	
	@Test
	public void getBoundedPlacesZeroResults() {
		Requester req = new Requester();
		JSONArray result = req.getPlaces(0, 0, 5, 5);
		assertEquals(0, result.size());
	}
	
	@Test
	public void getBoundedPlacesManyResults() {
		Requester req = new Requester(); 
		JSONArray result = req.getPlaces(59.336, 18.086, 59.340, 18.093); // Koordinater för Karlaplan
		assertEquals(2, result.size());
	}
	
}
