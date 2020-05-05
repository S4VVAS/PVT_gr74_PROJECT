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

public class Requester {

	public Requester() {
	}

	// Returnerar tillfälligt endast en sträng, bör returnera JSON-fil eller annat
	// passande format.
	public String getPlaces(double lat, double lon) {
		String query = getURL(lat, lon);

		HttpClient client = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(5))
				.build();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(query))
				.header("Accept", "application/json")
				.build();

		try {
			// Bör kanske använda sendAsync?
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			return response.body();
		} catch (IOException | InterruptedException e) {
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
