package com.owlsdonttalk.urlshortener.controllers;

import com.owlsdonttalk.urlshortener.models.UrlEntity;
import com.owlsdonttalk.urlshortener.repositories.UrlEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatisticsController.class)
@ActiveProfiles("test")
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlEntityRepository urlEntityRepository;

    private UrlEntity urlEntity1;

    @BeforeEach
    void setUp() {
        urlEntity1 = new UrlEntity();
        urlEntity1.setShortenedUrl("short1");
        urlEntity1.setOriginalUrl("https://example1.com");
        urlEntity1.setClickCount(100);
    }

    @Test
    void testGetUrlClickCount_Found() throws Exception {
        when(urlEntityRepository.findByShortenedUrl("short1")).thenReturn(Optional.of(urlEntity1));

        mockMvc.perform(get("/api/stats/short1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(100)));
    }

    @Test
    void testGetUrlClickCount_NotFound() throws Exception {
        when(urlEntityRepository.findByShortenedUrl("nonexistent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/stats/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUrlAmount() throws Exception {
        when(urlEntityRepository.count()).thenReturn(5L);

        mockMvc.perform(get("/api/stats/overall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(5)));
    }

    @Test
    void testGetUrlAmountByStatus() throws Exception {
        when(urlEntityRepository.countByStatus("active")).thenReturn(3L);

        mockMvc.perform(get("/api/stats/status/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(3)));
    }
}
