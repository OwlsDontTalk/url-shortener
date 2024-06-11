package com.owlsdonttalk.urlshortener.controllers;

import com.owlsdonttalk.urlshortener.dto.UrlRequest;
import com.owlsdonttalk.urlshortener.models.UrlEntity;
import com.owlsdonttalk.urlshortener.repositories.UrlEntityRepository;
import com.owlsdonttalk.urlshortener.services.UrlEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlEntityController.class)
@ActiveProfiles("test")
public class UrlEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlEntityRepository urlEntityRepository;

    @MockBean
    private UrlEntityService urlEntityService;

    @Value("${base.url}")
    private String baseUrl;

    private UrlEntity urlEntity;

    @BeforeEach
    public void setUp() {
        urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl("http://example.com");
        urlEntity.setShortenedUrl("shortUrl");
        urlEntity.setStatus("active");
    }

    @Test
    public void testCreateShortUrl_ValidUrl() throws Exception {
        UrlRequest request = new UrlRequest();
        request.setOriginalUrl("http://example.com");

        Mockito.when(urlEntityService.isValidUrl(anyString())).thenReturn(true);
        Mockito.when(urlEntityRepository.findByOriginalUrl(anyString())).thenReturn(Optional.empty());
        Mockito.when(urlEntityService.generateUniqueShortUrl()).thenReturn("shortUrl");
        Mockito.when(urlEntityRepository.save(any(UrlEntity.class))).thenReturn(urlEntity);

        mockMvc.perform(post("/api/url/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"originalUrl\":\"http://example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(baseUrl + "shortUrl"));
    }

    @Test
    public void testCreateShortUrl_InvalidUrl() throws Exception {
        UrlRequest request = new UrlRequest();
        request.setOriginalUrl("invalid_url");

        Mockito.when(urlEntityService.isValidUrl(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/url/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"originalUrl\":\"invalid_url\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid URL"));
    }

    @Test
    public void testGetUrlEntityByShortUrl_Found() throws Exception {
        Mockito.when(urlEntityRepository.findByShortenedUrl(anyString())).thenReturn(Optional.of(urlEntity));

        mockMvc.perform(get("/api/url/shortUrl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl").value("http://example.com"))
                .andExpect(jsonPath("$.shortenedUrl").value("shortUrl"));
    }

    @Test
    public void testGetUrlEntityByShortUrl_NotFound() throws Exception {
        Mockito.when(urlEntityRepository.findByShortenedUrl(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/url/shortUrl"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUrlEntity_Found() throws Exception {
        Mockito.when(urlEntityRepository.findByShortenedUrl(anyString())).thenReturn(Optional.of(urlEntity));

        mockMvc.perform(delete("/api/url/shortUrl"))
                .andExpect(status().isNoContent());

        Mockito.verify(urlEntityRepository).save(urlEntity);
        assert urlEntity.getStatus().equals("deleted");
    }

    @Test
    public void testDeleteUrlEntity_NotFound() throws Exception {
        Mockito.when(urlEntityRepository.findByShortenedUrl(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/url/shortUrl"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateUrlEntity_Found() throws Exception {
        UrlRequest request = new UrlRequest();
        request.setOriginalUrl("http://new-example.com");

        Mockito.when(urlEntityRepository.findByShortenedUrl(anyString())).thenReturn(Optional.of(urlEntity));

        mockMvc.perform(put("/api/url/shortUrl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"originalUrl\":\"http://new-example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl").value("http://new-example.com"));

        Mockito.verify(urlEntityRepository).save(urlEntity);
    }

    @Test
    public void testUpdateUrlEntity_NotFound() throws Exception {
        Mockito.when(urlEntityRepository.findByShortenedUrl(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/url/shortUrl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"originalUrl\":\"http://new-example.com\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPatchUrlEntity_Found() throws Exception {
        UrlRequest request = new UrlRequest();
        request.setOriginalUrl("http://new-example.com");

        Mockito.when(urlEntityRepository.findByShortenedUrl(anyString())).thenReturn(Optional.of(urlEntity));

        mockMvc.perform(patch("/api/url/shortUrl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"originalUrl\":\"http://new-example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl").value("http://new-example.com"));

        Mockito.verify(urlEntityRepository).save(urlEntity);
    }

    @Test
    public void testPatchUrlEntity_NotFound() throws Exception {
        Mockito.when(urlEntityRepository.findByShortenedUrl(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/url/shortUrl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"originalUrl\":\"http://new-example.com\"}"))
                .andExpect(status().isNotFound());
    }
}
