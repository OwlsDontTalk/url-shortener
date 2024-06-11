package com.owlsdonttalk.urlshortener.controllers;

import com.owlsdonttalk.urlshortener.models.UrlEntity;
import com.owlsdonttalk.urlshortener.repositories.UrlEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/stats")
public class StatisticsController {

    private final UrlEntityRepository urlEntityRepository;

    @Autowired
    public StatisticsController(UrlEntityRepository urlEntityRepository) {
        this.urlEntityRepository = urlEntityRepository;
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Integer> getUrlClickCount(@PathVariable String shortUrl) {
        Optional<UrlEntity> urlEntity = urlEntityRepository.findByShortenedUrl(shortUrl);

        return urlEntity.map(entity -> new ResponseEntity<>(entity.getClickCount(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/overall")
    public ResponseEntity<Long> getUrlAmount() {
        return new ResponseEntity<>(urlEntityRepository.count(), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Long> getUrlAmountByStatus(@PathVariable String status) {
        return new ResponseEntity<>(urlEntityRepository.countByStatus(status), HttpStatus.OK);
    }
}
