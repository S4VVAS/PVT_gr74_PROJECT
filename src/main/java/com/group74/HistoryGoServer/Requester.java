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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Scanner;
import java.util.stream.Stream;

import org.json.simple.parser.JSONParser;

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
			
			HttpResponse<InputStream> response = client.send(request, BodyHandlers.ofInputStream());
			
			InputStreamReader isr = new InputStreamReader(response.body());
			
			char c = (char) isr.read();
			while (c != -1) {
				System.out.print(c);
				c = (char) isr.read();
			}
			
		
			
			//HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
//			System.out.println(response.body());	
		
			
//			Scanner sc = new Scanner(response.body());
//			while (sc.hasNext()) {
//				sc.next();
//				sc.findInLine("record");
//				sc.findInLine("desc\":\"");
//				StringBuilder sb = new StringBuilder();
//				
//				String current = sc.next();
//				while (!current.equals("}")) {
//					sb.append(current);
//					System.out.print(current);
//					current = sc.next();
//				}
//				System.out.println(sb.toString());
//				System.out.println(sc.nextLine());
//				
//				while (!sc.next().equals("\"")) {
//					System.out.println(sc.next());
//					sb.append(sc.next());
//				}
//				System.out.println(sb.toString());
//				}
//				sc.close();
					
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
