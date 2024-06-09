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
        Optional<UrlEntity> urlEntityOptional = urlEntityRepository.findByShortenedUrl(shortUrl);

        if (urlEntityOptional.isPresent()) {
            UrlEntity urlEntity = urlEntityOptional.get();
            urlEntity.setClickCount(urlEntity.getClickCount() + 1);
            urlEntityRepository.save(urlEntity);
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(urlEntity.getOriginalUrl())).build();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}