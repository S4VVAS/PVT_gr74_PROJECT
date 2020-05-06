package com.group74.HistoryGoServer;

import java.util.Arrays;

import org.json.simple.JSONObject;

public class JSONParser {
	
	//JSON objects for places structure( name , pictureURI)
	public JSONObject parsePlaces(String str) {
		
		
		
		return null;
		
	}
	
	
	public JSONObject createJSON(String name, String desc, String[] picURLs) {
		JSONObject jObj = new JSONObject();
		jObj.put("name,", name);
		jObj.put("description,", desc);
		jObj.put("pictures,", Arrays.toString(picURLs));
		
		return jObj;
	}

}
