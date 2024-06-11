package com.owlsdonttalk.urlshortener.services;

import com.owlsdonttalk.urlshortener.repositories.UrlEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

@Service
public class UrlEntityService {

    @Autowired
    private UrlEntityRepository urlEntityRepository;

    @Value("${short.url.length}")
    private int shortUrlLength;

    public boolean isValidUrl(String url) {
        try {
            // TODO: This can be adjusted based on application requirements.
            URI uri = new URI(url);
            return uri.getScheme() != null && uri.getHost() != null;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public String generateUniqueShortUrl() {
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

        for (int i = 0; i < shortUrlLength; i++) {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }

        return shortUrl.toString();
    }
}
