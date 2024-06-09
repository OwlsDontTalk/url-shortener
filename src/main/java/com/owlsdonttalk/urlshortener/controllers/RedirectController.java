package com.owlsdonttalk.urlshortener.controllers;

import com.owlsdonttalk.urlshortener.models.UrlEntity;
import com.owlsdonttalk.urlshortener.repositories.UrlEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
public class RedirectController {

    private final UrlEntityRepository urlEntityRepository;

    @Autowired
    public RedirectController(UrlEntityRepository urlEntityRepository) {
        this.urlEntityRepository = urlEntityRepository;
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> redirectToOriginalUrl(@PathVariable String shortUrl) {
        Optional<UrlEntity> urlEntity = urlEntityRepository.findByShortenedUrl(shortUrl);

        return urlEntity
                .filter(entity -> "active".equals(entity.getStatus()))
                .map(entity -> {
                    entity.setClickCount(entity.getClickCount() + 1);
                    urlEntityRepository.save(entity);
                    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(entity.getOriginalUrl())).build();
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
