package com.owlsdonttalk.urlshortener;

import com.owlsdonttalk.urlshortener.models.UrlEntity;
import com.owlsdonttalk.urlshortener.repositories.UrlEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class UrlShortenerApplicationTests {

    @Autowired
    private UrlEntityRepository urlEntityRepository;

    @BeforeEach
    public void setUp() {
        urlEntityRepository.deleteAll();
    }

    @Test
    public void createUrlEntity() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\"originalUrl\":\"http://example.com\"}")
                .when()
                .post("/api/url")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("originalUrl", equalTo("http://example.com"));
    }

    @Test
    public void getUrlEntityById() {
        UrlEntity url = new UrlEntity();
        url.setOriginalUrl("http://example.com");
        urlEntityRepository.save(url);

        given()
                .when()
                .get("/api/url/" + url.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("originalUrl", equalTo("http://example.com"));
    }

    @Test
    public void updateUrlEntity() {
        UrlEntity url = new UrlEntity();
        url.setOriginalUrl("http://example.com");
        urlEntityRepository.save(url);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\"originalUrl\":\"http://newexample.com\"}")
                .when()
                .put("/api/url/" + url.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("originalUrl", equalTo("http://newexample.com"));
    }

    @Test
    public void deleteUrlEntity() {
        UrlEntity url = new UrlEntity();
        url.setOriginalUrl("http://example.com");
        urlEntityRepository.save(url);

        given()
                .when()
                .delete("/api/url/" + url.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void getUrlEntityNotFound() {
        given()
                .when()
                .get("/api/url/999")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
