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
		
	// Tar två koordinater, returnerar JSONArray med platser
	public JSONArray getPlaces(double lat, double lon) {
		String url = makeURL(lat, lon);

		HttpClient client = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(5))
				.build();

		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(url))
				.header("Accept", "application/json")
				.build();
		
		try {
			// Bör kanske använda sendAsync?
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			JSONArray records = parseBody(response.body());
			if (records == null) {
				return null;
			}
			JSONArray places = new JSONArray();
			for (int i = 0; i < records.size(); i++) {
				JSONObject record = (JSONObject) records.get(i);
				JSONObject graph = (JSONObject) record.get("record");
				JSONArray array = (JSONArray) graph.get("@graph");
				JSONObject plats = new JSONObject();
				for (int j = 0; j < array.size(); j++) {
					JSONObject current = (JSONObject) array.get(j);
					if (current.get("name") != null) {
						plats.put("name", current.get("name"));	
					}
					if (current.get("desc") != null) {
						plats.put("desc", current.get("desc"));
					}
					if (current.get("coordinates") != null) {
						plats.put("coor", current.get("coordinates"));
					}
					if (current.get("fromTime") != null) {
						plats.put("date", current.get("fromTime"));
					}
					if (current.get("lowresSource") != null) {
						plats.put("img", current.get("lowresSource"));
					}
				}
				places.add(plats);
			}
			return places;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Returnerar URI-sträng som används för geografisk sökning i API:et
	private String makeURL(double lat, double lon) {
		double leftLat = lat - 0.003;
		double leftLon = lon - 0.003;
		double rightLat = lat + 0.003;
		double rightLon = lon + 0.003;
		return "http://kulturarvsdata.se/ksamsok/api?method=search&query=boundingBox=/WGS84%20%22" + leftLon + "%20"
				+ leftLat + "%20" + rightLon + "%20" + rightLat + "%22%20AND%20itemType=%22Foto%22";
	}
	
	private JSONArray parseBody(String body) {
		try {
			JSONParser parser = new JSONParser();	
			JSONObject response = (JSONObject) parser.parse(body);
			JSONObject result = (JSONObject) response.get("result");
			return (JSONArray) result.get("records");
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
