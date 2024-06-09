package com.owlsdonttalk.urlshortener;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class UrlShortenerApplicationTests {

    @Autowired
    private Environment env;

    @Test
    public void shouldOverrideDefaultPropWithTestProp() {
        assertThat(env.getProperty("prop2")).isEqualTo("test_two");
    }
}