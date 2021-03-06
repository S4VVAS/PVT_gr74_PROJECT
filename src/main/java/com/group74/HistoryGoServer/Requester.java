/*
 * Skapa instans av klass, anropa sedan getPlaces med två doubles (latitud och longitud).
 */

package com.group74.HistoryGoServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Requester {
	
	private static final int MAX_HITS_PER_PAGE = 500;
	
	public Requester() {}
	
	//TODO: Refaktorisera: finns duplicerad kod, organisera om klassen (gör eventuellt klasshierarki)
	
	// Tar koordinat, returnerar JSONArray med platser i en area kring koordinaten
	public JSONArray getPlaces(String lat, String lon) {
		int start = 0; 
		JSONArray apiRecords = new JSONArray();
		do {
			String url = makeURL(lat, lon, 60, 60, start + 1); // API:ets index för första entry är 1.
			apiRecords.addAll(sendRequest(url));
			start += MAX_HITS_PER_PAGE;
		} while (apiRecords.size() != 0 && apiRecords.size() % MAX_HITS_PER_PAGE == 0);
		JSONObject trimmedJSON = trimJSON(apiRecords);
		JSONObject adjustedPositions = uniteClosePlaces(trimmedJSON);
		return makeArray(adjustedPositions);
	}
	
	// Tar två koordinater, returnerar JSONArray med platser i en area inom koordinaterna 
	public JSONArray getBoundedPlaces(String swLat, String swLon, String neLat, String neLon) {
		int start = 0;
		JSONArray apiRecords = new JSONArray();
		do {
			String url = makeBoundedURL(swLat, swLon, neLat, neLon, start + 1); // API:ets index för första entry är 1.
			apiRecords.addAll(sendRequest(url));
			start += MAX_HITS_PER_PAGE;
		} while (apiRecords.size() != 0 && apiRecords.size() % MAX_HITS_PER_PAGE == 0);
		JSONObject trimmedJSON = trimJSON(apiRecords);
		JSONObject adjustedPositions = uniteClosePlaces(trimmedJSON);
		return makeArray(adjustedPositions);
	}
	
	public JSONArray getCoorPlaces(String[] coors) {
		JSONObject result = new JSONObject();
		for (String c : coors) {
			String url = makeURL(getLat(c), getLon(c), 3, 3, 0);
			JSONArray apiRecords = sendRequest(url);
			JSONObject trimmedJSON = trimJSON(apiRecords);
			JSONObject place = uniteClosePlaces(trimmedJSON);
			if (place.size() != 0) {
				place.forEach((k,v) -> {
					result.put(k, v);
				});
			}
		}
		return makeArray(result);
		
	}
	
	private String getLat(String s) {
		return s.substring(0, s.indexOf(" "));
	}
	
	private String getLon(String s) {
		return s.substring(s.indexOf(" ") + 1);
	}
	
	// Tar en koordinat och returnerar URL-sträng som används för geografisk sökning i API:et
	public String makeURL(String lat, String lon, double w, double h, int start) {
		double latDo = Double.parseDouble(lat);
		double lonDo = Double.parseDouble(lon);
		double leftLat = latDo - normalizeMeasure(w); 
		double leftLon = lonDo - normalizeMeasure(h);
		double rightLat = latDo + normalizeMeasure(w);
		double rightLon = lonDo + normalizeMeasure(h);
		return "http://kulturarvsdata.se/ksamsok/api?method=search&query=boundingBox=/WGS84%20%22" + 
				leftLon + "%20" + leftLat + "%20" + rightLon + "%20" + rightLat + 
				"%22%20AND%20itemType=%22Foto%22&hitsPerPage=500&startRecord=" + start;
	}
	
	private double normalizeMeasure(double m) {
		return (m / 2) * 0.0001; 
	}
	
	// Tar två koordinater och returnerar URL-sträng som används för geografisk sökning i API:et
	private String makeBoundedURL(String swLat, String swLon, String neLat, String neLon, int start) {
		return "http://kulturarvsdata.se/ksamsok/api?method=search&query=boundingBox=/WGS84%20%22" + 
				swLon + "%20" + swLat + "%20" + neLon + "%20" + neLat + 
				"%22%20AND%20itemType=%22Foto%22&hitsPerPage=500&startRecord=" + start;
	}
	
	// Skickar request till API
	private JSONArray sendRequest(String url) {
		HttpClient client = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(5))
				.build();
		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(url))
				.header("Accept", "application/json")
				.build();
		try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			JSONArray records = parseBody(response.body());
			return records;
		} catch (InterruptedException | IOException e) {
			return null;
		}
	}
	
	// Skalar bort onödig info från JSON-filen som fås från API:et
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
	
	// Skalar bort onödig data som fås ifrån API
	private JSONObject trimJSON(JSONArray records) {
		JSONObject places = new JSONObject();
		for (int i = 0; i < records.size(); i++) {
			JSONObject record = (JSONObject) records.get(i);
			JSONObject graph = (JSONObject) record.get("record");
			JSONArray array = (JSONArray) graph.get("@graph");
			JSONObject place = new JSONObject();
			String position = "No data";
			for (int j = 0; j < array.size(); j++) {
				JSONObject current = (JSONObject) array.get(j);
				if (current.get("name") != null) {
					place.put("name", current.get("name"));	
				}
				if (current.get("desc") != null) {
					place.put("desc", current.get("desc"));
				}
				if (current.get("coordinates") != null) {
					String coordinates = trimCoordinates((String) current.get("coordinates"));
					position = coordinates;
				}
				if (current.get("fromTime") != null) {
					place.put("date", current.get("fromTime"));
				}
				if (current.get("lowresSource") != null) {
					place.put("img", current.get("lowresSource"));
				}
				if (current.get("itemLabel") != null) {
					place.put("title", current.get("itemLabel"));
				}
			}
			JSONArray placeArray = (JSONArray) places.get(position);
			if (placeArray == null) {
				placeArray = new JSONArray();
			}
			placeArray.add(place);
			places.put(position, placeArray);
		}
		return places;
	}
	
	// Skalar bort onödiga tecken från koordinaterna som fås från API:et
	private String trimCoordinates(String coor) {
		StringBuilder sb = new StringBuilder(coor);
		sb.delete(0, 113);
		int lonEnd = sb.indexOf(",");
		String lon = sb.substring(0, lonEnd);
		int latEnd = sb.indexOf("<");
		String lat = sb.substring(lonEnd + 1, latEnd);
		return lat + " " + lon;
	}
	
	// Lägger närliggande platser på samma position. 
	private JSONObject uniteClosePlaces(JSONObject places) {
		Set<String> coorSet = places.keySet();
		ArrayList<String> coorList = new ArrayList<String>();
		coorSet.forEach((c) -> coorList.add(c));
		for (int i = 0; i < coorList.size() - 1; i++) {
			String current = coorList.get(i);
			String currentRounded = roundCoordinates(current);
			for (int j = i + 1; j < coorList.size(); j++) {
				String other = coorList.get(j);
				String otherRounded = roundCoordinates(other);
				int ws = currentRounded.indexOf(" ");
				double latDiff = Double.parseDouble(currentRounded.substring(0, ws)) - 
								 Double.parseDouble(otherRounded.substring(0, ws));
				double lonDiff = Double.parseDouble(currentRounded.substring(ws + 1)) - 
						 		 Double.parseDouble(otherRounded.substring(ws + 1));
				if (latDiff >= -0.00015 && latDiff <= 0.00015 &&
					lonDiff >= -0.00015 && lonDiff <= 0.00015) {
					JSONArray currentArr = (JSONArray) places.get(current);
					JSONArray otherArr = (JSONArray) places.get(other);
					// Platsen med flest bilder flyttas ej.
					if (currentArr.size() >= otherArr.size()) {
						places.remove(other);
						for (int k = 0; k < otherArr.size(); k++) {
							currentArr.add(otherArr.get(k));
						}
						coorList.remove(j);
						j--;
					} else {
						places.remove(current);
						for (int k = 0; k < currentArr.size(); k++) {
							otherArr.add(currentArr.get(k));
						}
						break;
					}
				}
			}
		}
		return places;
	} 

	// Avrundar koordinater till fyra decimaler.
	private String roundCoordinates(String coordinates) {
		StringBuilder sb = new StringBuilder(coordinates);
		sb.delete(7, sb.indexOf(" "));
		sb.delete(15, sb.length());
		return sb.toString();
	}
	
	// Omformatterar JSON-objekt till array
	private JSONArray makeArray(JSONObject places) {
		JSONArray array = new JSONArray();
		places.forEach((pos, arr) -> { 
			JSONObject entry = new JSONObject();
			String coordinates = (String) pos;
			int latEnd = coordinates.indexOf(" ");
			entry.put("lat", coordinates.substring(0, latEnd));
			entry.put("lon", coordinates.substring(latEnd + 1));
			entry.put("entries", places.get(coordinates));
			array.add(entry);
		});
		return array;
	}
	
}
