package com.owlsdonttalk.urlshortener.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class StatusController {

    @GetMapping("/")
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
