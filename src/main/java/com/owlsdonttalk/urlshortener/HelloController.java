package com.owlsdonttalk.urlshortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.owlsdonttalk.urlshortener.models.UrlEntity;
import com.owlsdonttalk.urlshortener.repositories.UrlEntityRepository;

import java.util.Optional;

@RestController
public class HelloController {

    private final UrlEntityRepository urlEntityRepository;

    @Autowired
    public HelloController(UrlEntityRepository urlEntityRepository) {
        this.urlEntityRepository = urlEntityRepository;
    }



    @GetMapping("/first-url")
    public String getFirstUrl() {
        Optional<UrlEntity> firstUrlOptional = urlEntityRepository.findFirstByOrderById();
        if (firstUrlOptional.isPresent()) {
            UrlEntity firstUrl = firstUrlOptional.get();
            return "First URL: " + firstUrl.getOriginalUrl();
        } else {
            return "No URLs found.";
        }
    }
}
