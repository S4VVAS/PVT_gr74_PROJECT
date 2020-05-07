/*
 * Skapa instans av klass, anropa sedan getPlaces med två doubles (latitud och longitud).
 * getPlaces returnerar för tillfället endast en sträng.
 */

package com.group74.HistoryGoServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.HashSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Requester {

	public Requester() {}
	
	// Tar två koordinater, returnerar JSON-objekt med platser
	public JSONObject getPlaces(double lat, double lon) {
		JSONObject places = new JSONObject();
		HashSet<JSONObject> jList = new HashSet();
		HashSet<String> names = new HashSet();
		
		String query = getURL(lat, lon);

		HttpClient client = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(5))
				.build();

		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(query))
				.header("Accept", "application/json")
				.build();
		
		try {
			// Bör kanske använda sendAsync?
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			
			JSONParser jParser = new JSONParser();	
			JSONObject one = (JSONObject) jParser.parse(response.body());
			JSONObject result = (JSONObject) one.get("result");
			JSONArray records = (JSONArray) result.get("records");
			
			for (int i = 0; i < records.size(); i++) {
				JSONObject record = (JSONObject) records.get(i);
				JSONObject graph = (JSONObject) record.get("record");
				JSONArray array = (JSONArray) graph.get("@graph");
				JSONObject plats = new JSONObject();
				
				for (int j = 0; j < array.size(); j++) {
					JSONObject current = (JSONObject) array.get(j);
					if (current.get("name") != null) {
						if (!names.contains(current.get("name"))) {
							names.add("name");
							plats.put("name", current.get("name"));	
						}
					}
					if (current.get("desc") != null) {
						plats.put("desc", current.get("desc"));
					}
					if (current.get("thumbnail") != null) {
						plats.put("thumb", current.get("thumbnail"));
					}
				}
				if (!jList.contains(plats)) {
					jList.add(plats);
					places.put(i, plats);
				}
			}
			return places;
			
		} catch (IOException | InterruptedException | ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Returnerar URL-sträng som används för geografisk sökning i API:et
	private String getURL(double lat, double lon) {
		double leftLat = lat - 0.002;
		double leftLon = lon - 0.002;
		double rightLat = lat + 0.002;
		double rightLon = lon + 0.002;
		return "http://kulturarvsdata.se/ksamsok/api?method=search&query=boundingBox=/WGS84%20%22" + leftLon + "%20"
				+ leftLat + "%20" + rightLon + "%20" + rightLat + "%22";
	}

}
