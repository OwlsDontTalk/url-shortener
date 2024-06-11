package com.owlsdonttalk.urlshortener.controllers;

import com.owlsdonttalk.urlshortener.models.UrlEntity;
import com.owlsdonttalk.urlshortener.repositories.UrlEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RedirectController.class)
@ActiveProfiles("test")
public class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlEntityRepository urlEntityRepository;

    private UrlEntity urlEntity;

    @BeforeEach
    void setUp() {
        urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl("http://example.com");
        urlEntity.setShortenedUrl("shortUrl");
        urlEntity.setStatus("active");
        urlEntity.setClickCount(0);
    }

    @Test
    void testRedirectToOriginalUrl_Found() throws Exception {
        when(urlEntityRepository.findByShortenedUrl(anyString())).thenReturn(Optional.of(urlEntity));

        MvcResult result = mockMvc.perform(get("/shortUrl"))
                .andExpect(status().isFound())
                .andReturn();

        Mockito.verify(urlEntityRepository).save(urlEntity);
        assert urlEntity.getClickCount() == 1;
        assert result.getResponse().getHeader("Location").equals("http://example.com");
    }

    @Test
    void testRedirectToOriginalUrl_NotFound() throws Exception {
        when(urlEntityRepository.findByShortenedUrl(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/shortUrl"))
                .andExpect(status().isNotFound());
    }
}
