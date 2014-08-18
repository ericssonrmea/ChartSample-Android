package com.ericsson.json;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.annotation.SuppressLint;

import com.ericsson.classes.WeatherJSON;

public class JSONParser {

	private String urlString = null;
	public WeatherJSON weatherJSON;

	public volatile boolean parsingComplete = true;

	public JSONParser(String url) {
		this.urlString = url;
		weatherJSON = new WeatherJSON();
	}

	@SuppressLint("NewApi")
	public void readAndParseJSON(String in) {
		try {
			JSONObject reader = new JSONObject(in);

			JSONObject sys = reader.getJSONObject("sys");
			weatherJSON.setCountry(sys.getString("country"));

			JSONObject main = reader.getJSONObject("main");
			weatherJSON.setTemp(main.getString("temp"));
			weatherJSON.setTemp_max(main.getString("temp_max"));
			weatherJSON.setTemp_min(main.getString("temp_min"));

			weatherJSON.setName(reader.getString("name"));

			parsingComplete = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fetchJSON() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL(urlString);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(10000);
					conn.setConnectTimeout(15000);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					conn.connect();
					InputStream stream = conn.getInputStream();
					String data = convertStreamToString(stream);
					readAndParseJSON(data);
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}

	@SuppressWarnings("resource")
	static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}