package com.owlsdonttalk.urlshortener.controllers;

import com.owlsdonttalk.urlshortener.dto.UrlRequest;
import com.owlsdonttalk.urlshortener.models.UrlEntity;
import com.owlsdonttalk.urlshortener.repositories.UrlEntityRepository;
import com.owlsdonttalk.urlshortener.services.UrlEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/url")
public class UrlEntityController {

    @Autowired
    private UrlEntityRepository urlEntityRepository;

    @Autowired
    private UrlEntityService urlEntityService;

    @Value("${base.url}")
    private String baseUrl;

    @PostMapping("/create")
    public ResponseEntity<String> createShortUrl(@RequestBody UrlRequest request) {
        String originalUrl = request.getOriginalUrl();

        if (!urlEntityService.isValidUrl(originalUrl)) {
            return new ResponseEntity<>("Invalid URL", HttpStatus.BAD_REQUEST);
        }

        Optional<UrlEntity> existingUrlEntity = urlEntityRepository.findByOriginalUrl(originalUrl);
        if (existingUrlEntity.isPresent()) {
            return new ResponseEntity<>(baseUrl + existingUrlEntity.get().getShortenedUrl(), HttpStatus.OK);
        }

        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl(originalUrl);
        urlEntity.setShortenedUrl(urlEntityService.generateUniqueShortUrl());

        urlEntityRepository.save(urlEntity);

        return new ResponseEntity<>(baseUrl + urlEntity.getShortenedUrl(), HttpStatus.CREATED);
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
            UrlEntity entity = urlEntity.get();
            entity.setStatus("deleted");
            entity.setUpdatedAt(LocalDateTime.now());
            urlEntityRepository.save(entity);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{shortUrl}")
    public ResponseEntity<UrlEntity> updateUrlEntity(@PathVariable String shortUrl, @RequestBody UrlRequest updatedUrlRequest) {
        Optional<UrlEntity> urlEntity = urlEntityRepository.findByShortenedUrl(shortUrl);
        return urlEntity.map(entity -> {
            entity.setOriginalUrl(updatedUrlRequest.getOriginalUrl());
            entity.setUpdatedAt(LocalDateTime.now());
            urlEntityRepository.save(entity);
            return new ResponseEntity<>(entity, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{shortUrl}")
    public ResponseEntity<UrlEntity> patchUrlEntity(@PathVariable String shortUrl, @RequestBody UrlRequest updates) {
        Optional<UrlEntity> urlEntity = urlEntityRepository.findByShortenedUrl(shortUrl);
        return urlEntity.map(entity -> {
            if (StringUtils.hasText(updates.getOriginalUrl())) {
                entity.setOriginalUrl(updates.getOriginalUrl());
            }
            entity.setUpdatedAt(LocalDateTime.now());
            urlEntityRepository.save(entity);
            return new ResponseEntity<>(entity, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
