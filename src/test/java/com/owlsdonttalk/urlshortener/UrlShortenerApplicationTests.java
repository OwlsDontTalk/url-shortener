package com.owlsdonttalk.urlshortener;

import com.owlsdonttalk.urlshortener.models.UrlEntity;
import com.owlsdonttalk.urlshortener.repositories.UrlEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class UrlShortenerApplicationTests {

    @Autowired
    private UrlEntityRepository urlEntityRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        urlEntityRepository.deleteAll();
    }

//    @Test
//    public void createUrlEntity() {
//        given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body("{\"originalUrl\":\"http://example.com\"}")
//                .when()
//                .post("/api/url")
//                .then()
//                .statusCode(HttpStatus.CREATED.value())
//                .body("id", notNullValue())
//                .body("originalUrl", equalTo("http://example.com"));
//    }
//
//    @Test
//    public void getUrlEntityById() {
//        UrlEntity url = new UrlEntity();
//        url.setOriginalUrl("http://example.com");
//        urlEntityRepository.save(url);
//
//        given()
//                .when()
//                .get("/api/url/" + url.getId())
//                .then()
//                .statusCode(HttpStatus.OK.value())
//                .body("originalUrl", equalTo("http://example.com"));
//    }
//
//    @Test
//    public void updateUrlEntity() {
//        UrlEntity url = new UrlEntity();
//        url.setOriginalUrl("http://example.com");
//        urlEntityRepository.save(url);
//
//        given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body("{\"originalUrl\":\"http://newexample.com\"}")
//                .when()
//                .put("/api/url/" + url.getId())
//                .then()
//                .statusCode(HttpStatus.OK.value())
//                .body("originalUrl", equalTo("http://newexample.com"));
//    }
//
//    @Test
//    public void deleteUrlEntity() {
//        UrlEntity url = new UrlEntity();
//        url.setOriginalUrl("http://example.com");
//        urlEntityRepository.save(url);
//
//        given()
//                .when()
//                .delete("/api/url/" + url.getId())
//                .then()
//                .statusCode(HttpStatus.NO_CONTENT.value());
//    }
//
//    @Test
//    public void getUrlEntityNotFound() {
//        given()
//                .when()
//                .get("/api/url/999")
//                .then()
//                .statusCode(HttpStatus.NOT_FOUND.value());
//    }


    @Test
    public void redirectToOriginalUrl() {
        UrlEntity url = new UrlEntity();
        url.setOriginalUrl("http://example.com");
        url.setShortenedUrl("shortUrl");
        urlEntityRepository.save(url);

        given()
                .port(port)
                .when()
                .get("/shortUrl")
                .then()
                .statusCode(HttpStatus.OK.value())
                .header("Location", equalTo("http://example.com"));
    }

    @Test
    public void redirectToNonexistentUrl() {
        given()
                .port(port)
                .when()
                .get("/nonexistent")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
