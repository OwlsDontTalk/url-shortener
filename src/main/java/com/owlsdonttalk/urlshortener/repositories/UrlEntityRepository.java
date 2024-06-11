package com.owlsdonttalk.urlshortener.repositories;

import com.owlsdonttalk.urlshortener.models.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlEntityRepository extends JpaRepository<UrlEntity, Long> {
    Optional<UrlEntity> findByShortenedUrl(String shortenedUrl);
    Optional<UrlEntity> findByOriginalUrl(String originalUrl);

    long countByStatus(String status);
}