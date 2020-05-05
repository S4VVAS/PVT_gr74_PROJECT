/*
 * Adam Lundberg 2020-05-04: Klass som tar två koordinater (WGS84-standard) och ger tillbaka resultat i närheten.
 */

// sökning i api:et görs i format: lon lat lon lat

// Sökning inom kvadrat
// http://kulturarvsdata.se/ksamsok/api?method=search&query=boundingBox=/WGS84%20%2218.094%2059.326%2018.098%2059.3309%22

// Sökning inom kvadrat och krav på thumbnail
// http://kulturarvsdata.se/ksamsok/api?method=search&query=boundingBox=/WGS84%20%2218.094%2059.326%2018.098%2059.3309%22%20AND%20thumbnailExists=j

// Sökning där geodata finns och där endast vissa element av resultatet visas
// http://www.kulturarvsdata.se/ksamsok/api?method=search&query=geoDataExists=j&recordSchema=xml&fields=itemLabel,itemDescription,lat,lon,thumbnail

package com.group74.HistoryGoServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

public class Requester {
	
	public Requester(double lat, double lon) {
		String query = "http://kulturarvsdata.se/ksamsok/api?method=search&query=boundingBox=/WGS84%20%2218.094%2059.326%2018.098%2059.3309%22";
		
		HttpClient client = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(5))
				.build();
		
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(query))
			.header("Accept", "application/json")
			.build();
			
		try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			System.out.println(response.body());	
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} 
	
	public static void main(String[] args) {
		double latitude = 59.328;
		double longitude = 18.096;
		Requester r = new Requester(latitude, longitude);
		
	}
	
}
