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
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/url")
public class UrlEntityController {

    @Autowired
    private UrlEntityRepository urlEntityRepository;

    // TODO: Move to .ENV or change by hand.
    private final String BASE_URL = "http://localhost:8080/";

    @PostMapping("/create")
    public ResponseEntity<String> createShortUrl(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("originalUrl");
        boolean force = Boolean.parseBoolean(request.getOrDefault("force", "false"));

        if (!isValidUrl(originalUrl)) {
            return new ResponseEntity<>("Invalid URL", HttpStatus.BAD_REQUEST);
        }

        if (!force && !urlRespondsOk(originalUrl)) {
            return new ResponseEntity<>("URL does not respond with 200 OK. Pass force: true to ignore it.", HttpStatus.BAD_REQUEST);
        }

        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl(originalUrl);
        urlEntity.setCreatedAt(LocalDateTime.now());
        urlEntity.setUpdatedAt(LocalDateTime.now());
        urlEntity.setShortenedUrl(generateUniqueShortUrl());

        urlEntityRepository.save(urlEntity);

        return new ResponseEntity<>(BASE_URL + urlEntity.getShortenedUrl(), HttpStatus.CREATED);
    }


    @GetMapping("/{shortUrl}")
    public ResponseEntity<UrlEntity> getUrlEntityByShortUrl(@PathVariable String shortUrl) {
        Optional<UrlEntity> urlEntity = urlEntityRepository.findByShortenedUrl(shortUrl);
        return urlEntity.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{shortUrl}")
    public ResponseEntity<Void> deleteUrlEntity(@PathVariable String shortUrl) {
        Optional<UrlEntity> urlEntity = urlEntityRepository.findByShortenedUrl(shortUrl);
        if (urlEntity.isPresent()) {
            urlEntityRepository.delete(urlEntity.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{shortUrl}")
    public ResponseEntity<UrlEntity> updateUrlEntity(@PathVariable String shortUrl, @RequestBody UrlEntity updatedUrlEntity) {
        Optional<UrlEntity> urlEntity = urlEntityRepository.findByShortenedUrl(shortUrl);
        return urlEntity.map(entity -> {
            entity.setOriginalUrl(updatedUrlEntity.getOriginalUrl());
            entity.setUpdatedAt(LocalDateTime.now());
            urlEntityRepository.save(entity);
            return new ResponseEntity<>(entity, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{shortUrl}")
    public ResponseEntity<UrlEntity> patchUrlEntity(@PathVariable String shortUrl, @RequestBody Map<String, String> updates) {
        Optional<UrlEntity> urlEntity = urlEntityRepository.findByShortenedUrl(shortUrl);
        return urlEntity.map(entity -> {
            if (updates.containsKey("originalUrl")) {
                entity.setOriginalUrl(updates.get("originalUrl"));
            }
            entity.setUpdatedAt(LocalDateTime.now());
            urlEntityRepository.save(entity);
            return new ResponseEntity<>(entity, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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

    private String generateUniqueShortUrl() {
        String shortUrl;
        do {
            shortUrl = generateShortUrl();
        } while (urlEntityRepository.findByShortenedUrl(shortUrl).isPresent());
        return shortUrl;
    }

    private String generateShortUrl() {
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < 5; i++) {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }

        return shortUrl.toString();
    }
}