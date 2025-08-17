package com.challenge.javatechnicalchallenge;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=" +
        "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
        "spring.main.lazy-initialization=true"
})
@Import(JavaTechnicalChallengeApplicationTests.TestConfig.class)
class JavaTechnicalChallengeApplicationTests {

    @TestConfiguration
    static class TestConfig {
        @Bean
        JdbcTemplate jdbcTemplate() {
            return Mockito.mock(JdbcTemplate.class);
        }
    }

    @Test
    void contextLoads() {}

}
