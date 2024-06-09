package com.owlsdonttalk.urlshortener.controllers;

import com.owlsdonttalk.urlshortener.models.UrlEntity;
import com.owlsdonttalk.urlshortener.repositories.UrlEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Random;

@RestController
@RequestMapping("/api/url")
public class UrlEntityController {

    @Autowired
    private UrlEntityRepository urlEntityRepository;

    // TODO: Move to .ENV or change by hand.
    private final String BASE_URL = "http://localhost:8080/";

    @PostMapping("/create")
    public ResponseEntity<String> createShortUrl(@RequestBody String originalUrl) {
        if (!isValidUrl(originalUrl)) {
            return new ResponseEntity<>("Invalid URL", HttpStatus.BAD_REQUEST);
        }

        if (!urlRespondsOk(originalUrl)) {
            return new ResponseEntity<>("URL does not respond with 200 OK", HttpStatus.BAD_REQUEST);
        }

        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl(originalUrl);
        urlEntity.setCreatedAt(LocalDateTime.now());
        urlEntity.setUpdatedAt(LocalDateTime.now());
        urlEntity.setShortenedUrl(generateShortUrl());

        urlEntityRepository.save(urlEntity);

        return new ResponseEntity<>(BASE_URL + urlEntity.getShortenedUrl(), HttpStatus.CREATED);
    }


    private boolean isValidUrl(String url) {
        return StringUtils.hasText(url) && url.matches("^(http|https)://.*$");
    }

    private boolean urlRespondsOk(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();
            return code == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private String generateShortUrl() {
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < 6; i++) {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }

        return shortUrl.toString();
    }
}
