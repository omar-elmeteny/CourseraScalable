package com.guctechie.datainsertions.runners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Order(0)
public class SchemaResetter implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(SchemaResetter.class);
    private final JdbcTemplate jdbcTemplate;

    public SchemaResetter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // load schema.sql from resources
        logger.info("Resetting schema");

        try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream("drop.sql")) {
            if (inputStream != null) {
                String sql = new String(inputStream.readAllBytes());
                jdbcTemplate.execute(sql);
            }
            logger.info("Schema reset complete");
        } catch (Exception e) {
            logger.error("Error resetting schema: " + e.getMessage());
            throw e;
        }
    }
}
