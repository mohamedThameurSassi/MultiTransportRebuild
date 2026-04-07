package com.mtsassi.mtl_transit_api.controller;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
	@GetMapping(path = "/gethealth", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getHealth() {
		Instant now = Instant.now();
		Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("status", "UP");
		payload.put("epochSeconds", now.getEpochSecond());
		payload.put("timestamp", now.toString());
		return payload;
	}
}
