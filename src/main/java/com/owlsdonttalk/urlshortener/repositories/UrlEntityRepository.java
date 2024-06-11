package com.owlsdonttalk.urlshortener.repositories;

import com.owlsdonttalk.urlshortener.models.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlEntityRepository extends JpaRepository<UrlEntity, Long> {
    Optional<UrlEntity> findByShortenedUrl(String shortenedUrl);
    Optional<UrlEntity> findByOriginalUrl(String originalUrl);
    Optional<UrlEntity> findTop10ByOrderByClickCountDesc(String originalUrl);

    long countByStatus(String status);
}