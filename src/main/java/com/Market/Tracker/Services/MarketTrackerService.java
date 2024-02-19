package com.Market.Tracker.Services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.Market.Tracker.Entities.Actives;
import com.Market.Tracker.Entities.UserEntity;
import com.Market.Tracker.Repositories.MarketTrackerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * API DOCUMENTATION
 * https://www.alphavantage.co/documentation/
 * 
 * 
 * FALTA 
 * - IMPLEMENTAR UN MÉTODO DE CONVERSIÓN DE MONEDAS Y LLAMARLO ANTES
 * DE DEVOLVER EL VALOR DEL ACTIVO AL USUARIO
 */
@Service
public class MarketTrackerService {
    @Autowired
    MarketTrackerRepository repository;
    UserEntity user;
    
    private String apiUrl = "";

    // OBTAIN THE CURRENT VALUE
	public Actives searchValue(String active) {
		String apiUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="+active+"&interval=60min&apikey=HPBU98O8N1BP0M9Z";
        String apiUrlName = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords="+active+"&apikey=HPBU98O8N1BP0M9Z";
		int statusCode;
		
        
        // HTTP client creation
        HttpClient httpClient = HttpClient.newHttpClient();

        // HTTP GET petition construction
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();
        
        HttpRequest httpRequestName = HttpRequest.newBuilder()
                .uri(URI.create(apiUrlName))
                .GET()
                .build();

        try {
            // Enviar la solicitud y recibir la respuesta
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();

            // Deserializar la respuesta JSON a un objeto JsonNode
            JsonNode rootNode = objectMapper.readValue(response.body(), JsonNode.class);

            // Acceder al valor de "1. open" en la primera entrada de "Time Series (60min)"
            JsonNode timeSeriesNodeValue = rootNode.path("Time Series (60min)");
            // Obtener la primera entrada
            String firstEntryKeyValue = timeSeriesNodeValue.fieldNames().next();
            JsonNode firstEntryValue = timeSeriesNodeValue.path(firstEntryKeyValue);

            String openValue = firstEntryValue.path("1. open").asText();
            openValue = openValue.contains(",") ? openValue.replace(",", ".") : openValue;
            
            // Acceder al valor de "1. open" en la primera entrada de "Time Series (60min)"
            JsonNode timeSeriesNode = rootNode.path("Meta Data");
            // Obtener la primera entrada
            String symbol = timeSeriesNode.path("2. Symbol").asText();

            HttpResponse<String> responseName = httpClient.send(httpRequestName, HttpResponse.BodyHandlers.ofString());
            JsonNode rootNodeName = objectMapper.readValue(responseName.body(), JsonNode.class);

            // Acceder al valor de "1. open" en la primera entrada de "Time Series (60min)"
            JsonNode timeSeriesNodeName = rootNodeName.path("bestMatches");
            // Obtener la primera entrada
            JsonNode firstMatch = timeSeriesNodeName.path(1);
            String activeName = firstMatch.path("2. name").asText();
            
            // Imprimir la respuesta
            System.out.println("Código de estado: " + response.statusCode());
            System.out.println("Respuesta: " + response.body().toString());
            System.out.println("Symbol: " + symbol);
            Actives searchedActive = new Actives(symbol, openValue);

            return searchedActive;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return new Actives();
    }
	
	// SEARCHBAR RESULT
	public List<String> searchActive(String active) {
		String apiUrlName = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords="+active+"&apikey=HPBU98O8N1BP0M9Z";
		int statusCode;
		List<String> searchResult = new ArrayList<>();
		
        // HTTP client creation
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequestName = HttpRequest.newBuilder()
                .uri(URI.create(apiUrlName))
                .GET()
                .build();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            HttpResponse<String> response = httpClient.send(httpRequestName, HttpResponse.BodyHandlers.ofString());
            
   
            JsonNode rootNode = objectMapper.readValue(response.body(), JsonNode.class);

            JsonNode bestMatchesNode = rootNode.path("bestMatches");
            Iterator<JsonNode> matchesIterator = bestMatchesNode.elements();

            while (matchesIterator.hasNext()) {
                JsonNode matchNode = matchesIterator.next();
                String symbol = matchNode.path("1. symbol").asText();
                searchResult.add(symbol);
            }
        } catch (Exception e) {
        	System.out.println("Error: " + e);
            e.printStackTrace();
        }
        System.out.println("Service: " + searchResult);
        return searchResult;
    
	}

	public UserEntity getUser(String username) {
		user = (UserEntity) repository.findByUsername(username);
		return user;
	}

	public List<Actives> getWatchListInfos(List<String> watchList) {
		List<Actives> actives = new ArrayList<>();
		watchList.forEach(active -> actives.add(searchValue(active)));
		return actives;
	}
}

